package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import ru.otus.homework.dto.VehicleBrandReqDTO;
import ru.otus.homework.dto.VehicleBrandResDTO;

import java.util.List;

public interface VehicleBrandService {

    Iterable<VehicleBrandResDTO> findByParams(Predicate predicate, Pageable pageable);

    List<VehicleBrandResDTO> findByParams(Predicate predicate);

    VehicleBrandResDTO findById(Long id);

    VehicleBrandResDTO create(VehicleBrandReqDTO vehicleBrandReqDTO);

    VehicleBrandResDTO modify(Long id, VehicleBrandReqDTO vehicleBrandReqDTO);

    void delete(Long id);

    List<VehicleBrandResDTO> findAllVehicleBrands(String name, Long productionKindId);

}
