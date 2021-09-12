package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import ru.otus.homework.dto.VehicleBrandResDTO;
import ru.otus.homework.dto.VehicleModelReqDTO;
import ru.otus.homework.dto.VehicleModelResDTO;

import java.util.List;

public interface VehicleModelService {

    Iterable<VehicleModelResDTO> findByParams(Predicate predicate, Pageable pageable);

    List<VehicleModelResDTO> findByParams(Predicate predicate);

    VehicleModelResDTO findById(Long id);

    VehicleModelResDTO create(VehicleModelReqDTO vehicleModelReqDTO);

    VehicleModelResDTO modify(Long id, VehicleModelReqDTO vehicleModelReqDTO);

    void delete(Long id);

    List<VehicleModelResDTO> findAllVehicleModels(Long vehicleBrandId, String name, Long kindId, Long propertyFormId, Long productionKindId);

}
