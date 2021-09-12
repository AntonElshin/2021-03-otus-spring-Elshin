package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import ru.otus.homework.dto.VehicleSetYearReqDTO;
import ru.otus.homework.dto.VehicleSetYearResDTO;

import java.util.List;

public interface VehicleSetYearService {

    Iterable<VehicleSetYearResDTO> findByParams(Predicate predicate, Pageable pageable);

    VehicleSetYearResDTO findById(Long id);

    VehicleSetYearResDTO create(VehicleSetYearReqDTO vehicleSetYearReqDTO);

    VehicleSetYearResDTO modify(Long id, VehicleSetYearReqDTO vehicleSetYearReqDTO);

    void delete(Long id);

    List<VehicleSetYearResDTO> findAllVehicleSetYears(
            Long setId,
            Long yearId
    );

}
