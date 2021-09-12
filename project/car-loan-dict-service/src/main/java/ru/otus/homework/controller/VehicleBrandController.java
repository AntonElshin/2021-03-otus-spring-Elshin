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
import ru.otus.homework.api.VehicleBrandApi;
import ru.otus.homework.dto.VehicleBrandReqDTO;
import ru.otus.homework.dto.VehicleBrandResDTO;
import ru.otus.homework.entity.VehicleBrandEntity;
import ru.otus.homework.service.VehicleBrandService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleBrandController implements VehicleBrandApi {

    private final VehicleBrandService vehicleBrandService;

    @Override
    public ResponseEntity<List<VehicleBrandResDTO>> getAllVehicleBrands(String name, Long productionKindId) {
        return ResponseEntity.ok(vehicleBrandService.findAllVehicleBrands(name, productionKindId));
    }

    @ApiOperation(value = "Получение списка марок по параметрам")
    @GetMapping(value = "/vehicle-brand/vehiclebrands", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<VehicleBrandResDTO> getVehicleBrands(
            @QuerydslPredicate(root = VehicleBrandEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return vehicleBrandService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<VehicleBrandResDTO> getVehicleBrand(Long vehicleBrandId) {
        return ResponseEntity.ok(vehicleBrandService.findById(vehicleBrandId));
    }

    @Override
    public ResponseEntity<VehicleBrandResDTO> createVehicleBrand(VehicleBrandReqDTO vehicleBrandReqDTO) {
        return ResponseEntity.ok(vehicleBrandService.create(vehicleBrandReqDTO));
    }

    @Override
    public ResponseEntity<VehicleBrandResDTO> modifyVehicleBrand(Long vehicleBrandId, VehicleBrandReqDTO vehicleBrandReqDTO) {
        return ResponseEntity.ok(vehicleBrandService.modify(vehicleBrandId, vehicleBrandReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteVehicleBrand(Long vehicleBrandId) {
        vehicleBrandService.delete(vehicleBrandId);
        return ResponseEntity.ok(null);
    }
}
