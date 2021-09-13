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
import ru.otus.homework.api.VehicleModelApi;
import ru.otus.homework.dto.VehicleModelReqDTO;
import ru.otus.homework.dto.VehicleModelResDTO;
import ru.otus.homework.entity.VehicleModelEntity;
import ru.otus.homework.service.VehicleModelService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleModelController implements VehicleModelApi {

    private final VehicleModelService vehicleModelService;

    @Override
    public ResponseEntity<List<VehicleModelResDTO>> getAllVehicleModels(Long brandId, String name, Long kindId, Long propertyFormId, Long productionKindId) {
        return ResponseEntity.ok(vehicleModelService.findAllVehicleModels(brandId, name, kindId, propertyFormId, productionKindId));
    }

    @ApiOperation(value = "Получение списка моделей по параметрам")
    @GetMapping(value = "/vehicle-model/vehiclemodels", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<VehicleModelResDTO> getVehicleModels(
            @QuerydslPredicate(root = VehicleModelEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return vehicleModelService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<VehicleModelResDTO> getVehicleModel(Long vehicleModelId) {
        return ResponseEntity.ok(vehicleModelService.findById(vehicleModelId));
    }

    @Override
    public ResponseEntity<VehicleModelResDTO> createVehicleModel(VehicleModelReqDTO vehicleModelReqDTO) {
        return ResponseEntity.ok(vehicleModelService.create(vehicleModelReqDTO));
    }

    @Override
    public ResponseEntity<VehicleModelResDTO> modifyVehicleModel(Long vehicleModelId, VehicleModelReqDTO vehicleModelReqDTO) {
        return ResponseEntity.ok(vehicleModelService.modify(vehicleModelId, vehicleModelReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteVehicleModel(Long vehicleModelId) {
        vehicleModelService.delete(vehicleModelId);
        return ResponseEntity.ok(null);
    }

}
