package ru.otus.homework.controller;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.api.VehicleSetYearApi;
import ru.otus.homework.dto.VehicleSetYearReqDTO;
import ru.otus.homework.dto.VehicleSetYearResDTO;
import ru.otus.homework.entity.VehicleSetYearEntity;
import ru.otus.homework.service.VehicleSetYearService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleSetYearController implements VehicleSetYearApi {

    private final VehicleSetYearService vehicleSetYearService;

    @Override
    public ResponseEntity<List<VehicleSetYearResDTO>> getAllVehicleSetYears(Long setId, Long yearId) {
        return ResponseEntity.ok(vehicleSetYearService.findAllVehicleSetYears(
                setId,
                yearId));
    }

    @ApiOperation(value = "Получение списка годов комплектаций по параметрам")
    @GetMapping(value = "/vehicle-set-year/vehiclesetyears", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<VehicleSetYearResDTO> getVehicleSetYears(
            @QuerydslPredicate(root = VehicleSetYearEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return vehicleSetYearService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<VehicleSetYearResDTO> getVehicleSetYear(Long vehicleSetYearId) {
        return ResponseEntity.ok(vehicleSetYearService.findById(vehicleSetYearId));
    }

    @Override
    public ResponseEntity<VehicleSetYearResDTO> createVehicleSetYear(VehicleSetYearReqDTO vehicleSetYearReqDTO) {
        return ResponseEntity.ok(vehicleSetYearService.create(vehicleSetYearReqDTO));
    }

    @Override
    public ResponseEntity<VehicleSetYearResDTO> modifyVehicleSetYear(Long vehicleSetYearId, VehicleSetYearReqDTO vehicleSetYearReqDTO) {
        return ResponseEntity.ok(vehicleSetYearService.modify(vehicleSetYearId, vehicleSetYearReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteVehicleSetYear(Long vehicleSetYearId) {
        vehicleSetYearService.delete(vehicleSetYearId);
        return ResponseEntity.ok(null);
    }
}
