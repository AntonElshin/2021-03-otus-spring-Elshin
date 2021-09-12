package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.homework.dto.FileUploadHistoryResDTO;
import ru.otus.homework.entity.FileUploadHistoryEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.entity.VehicleModelEntity;
import ru.otus.homework.entity.VehicleSetEntity;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.mappers.FileUploadHistoryMapper;
import ru.otus.homework.parsers.Parser;
import ru.otus.homework.repository.FileUploadHistoryRepository;
import ru.otus.homework.repository.ReferenceItemRepository;
import ru.otus.homework.repository.VehicleModelRepository;
import ru.otus.homework.dto.FileUploadHistoryResIdDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Loader loader;
    private final Parser parser;
    private final FileUploadHistoryRepository fileUploadHistoryRepository;
    private final ReferenceItemRepository referenceItemRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleSetService vehicleSetService;

    private static final String FILE_UPLOAD_STATUS_LOADING = "LOADING";
    private static final String FILE_UPLOAD_STATUS_DONE = "DONE";
    private static final String REFERENCE_SYSNAME_UPLOAD_FILE_TYPE = "uploadFileType";
    private static final String FILE_UPLOAD_TYPE_PASSENGER_USED = "PassengerUsed";

    @Override
    public Iterable<FileUploadHistoryResDTO> findByParams(Predicate predicate, Pageable pageable) {
        return fileUploadHistoryRepository.findAll(predicate, pageable).map(FileUploadHistoryMapper.INSTANCE::toDto);
    }

    @Override
    public FileUploadHistoryResIdDTO uploadPassengerUsedVehicleModelsAndSets(MultipartFile file) {

        ReferenceItemEntity passengerUsedUploadFileType = referenceItemRepository.findByReferenceSysNameAndReferenceItemCodeEquals(
                REFERENCE_SYSNAME_UPLOAD_FILE_TYPE,
                FILE_UPLOAD_TYPE_PASSENGER_USED
        );

        Integer versionNumber = fileUploadHistoryRepository.getMaxVersionNumberByFileTypeId(passengerUsedUploadFileType.getId());

        if(versionNumber == null) {
            versionNumber = 1;
        }
        else {
            versionNumber++;
        }

        FileUploadHistoryEntity fileUploadHistoryEntity = new FileUploadHistoryEntity(
                null,
                LocalDateTime.now(),
                null,
                FILE_UPLOAD_STATUS_LOADING,
                "admin",
                versionNumber,
                passengerUsedUploadFileType,
                file.getOriginalFilename(),
                null,
                null
        );

        fileUploadHistoryRepository.save(fileUploadHistoryEntity);

        Thread myThread = new Thread(() -> {

            List<String> vehicleSets = loader.loadVehiclePassangerUsedSets(file);
            List<VehicleSetEntity> vehicleSetEntities = parser.getPassengerUsedVehicleSets(vehicleSets, fileUploadHistoryEntity);

            List<VehicleModelEntity> vehicleModelEntities = new ArrayList<>();

            for(VehicleSetEntity vehicleSetEntity : vehicleSetEntities) {
                vehicleModelEntities.add(vehicleSetEntity.getModel());
            }

            fileUploadHistoryEntity.setFileContent(vehicleSets.stream().collect(Collectors.joining("\n")));
            fileUploadHistoryEntity.setStatus(FILE_UPLOAD_STATUS_DONE);
            fileUploadHistoryEntity.setUploadDateEnd(LocalDateTime.now());
            vehicleSetService.saveVehicleSetsFromFile(vehicleSetEntities);

            fileUploadHistoryRepository.save(fileUploadHistoryEntity);

        });

        myThread.start();

        FileUploadHistoryResIdDTO fileUploadHistoryResIdDTO = new FileUploadHistoryResIdDTO();
        fileUploadHistoryResIdDTO.setId(fileUploadHistoryEntity.getId());

        return fileUploadHistoryResIdDTO;
    }
}
