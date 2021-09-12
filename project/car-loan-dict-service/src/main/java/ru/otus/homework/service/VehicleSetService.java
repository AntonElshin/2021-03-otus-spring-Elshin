package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.homework.dto.CheckVehicleSetPriceResDTO;
import ru.otus.homework.dto.VehicleSetReqDTO;
import ru.otus.homework.dto.VehicleSetResDTO;
import ru.otus.homework.entity.VehicleSetEntity;

import java.util.List;

public interface VehicleSetService {

    Iterable<VehicleSetResDTO> findByParams(Predicate predicate, Pageable pageable);

    List<VehicleSetResDTO> findByParams(Predicate predicate);

    VehicleSetResDTO findById(Long id);

    VehicleSetResDTO create(VehicleSetReqDTO vehicleSetReqDTO);

    VehicleSetResDTO modify(Long id, VehicleSetReqDTO vehicleSetReqDTO);

    void delete(Long id);

    List<VehicleSetResDTO> findAllVehicleSets(
            Long modelId,
            Long bodyId,
            Long engineSizeId,
            Long engineTypeId,
            Long powerId,
            Long transmissionId,
            Long yearId
    );

    CheckVehicleSetPriceResDTO checkVehicleSetPrice(
            Long modelId,
            String characteristicSysName,
            Long bodyId,
            Long engineSizeId,
            Long engineTypeId,
            Long powerId,
            Long transmissionId,
            Long yearId
    );

    void saveVehicleSetsFromFile(List<VehicleSetEntity> vehicleSetEntities);

}
