package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.homework.dto.FileUploadHistoryResIdDTO;
import ru.otus.homework.dto.FileUploadHistoryResDTO;

public interface FileUploadService {

    Iterable<FileUploadHistoryResDTO> findByParams(Predicate predicate, Pageable pageable);

    FileUploadHistoryResIdDTO uploadPassengerUsedVehicleModelsAndSets(MultipartFile file);

}
