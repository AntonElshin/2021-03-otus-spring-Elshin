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
import ru.otus.homework.api.VehicleSetApi;
import ru.otus.homework.dto.CheckVehicleSetPriceResDTO;
import ru.otus.homework.dto.VehicleSetReqDTO;
import ru.otus.homework.dto.VehicleSetResDTO;
import ru.otus.homework.entity.VehicleSetEntity;
import ru.otus.homework.service.VehicleSetService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleSetController implements VehicleSetApi {

    private final VehicleSetService vehicleSetService;

    @Override
    public ResponseEntity<List<VehicleSetResDTO>> getAllVehicleSets(
            Long modelId,
            Long bodyId,
            Long engineSizeId,
            Long engineTypeId,
            Long powerId,
            Long transmissionId,
            Long yearId) {
        return ResponseEntity.ok(vehicleSetService.findAllVehicleSets(
                modelId,
                bodyId,
                engineSizeId,
                engineTypeId,
                powerId,
                transmissionId,
                yearId));
    }

    @ApiOperation(value = "Получение списка комплектаций по параметрам")
    @GetMapping(value = "/vehicle-set/vehiclesets", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<VehicleSetResDTO> getVehicleSets(
            @QuerydslPredicate(root = VehicleSetEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return vehicleSetService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<CheckVehicleSetPriceResDTO> checkVehicleSetPrice(Long modelId, String characteristicSysName, Long bodyId, Long engineSizeId, Long engineTypeId, Long powerId, Long transmissionId, Long yearId) {
        return ResponseEntity.ok(vehicleSetService.checkVehicleSetPrice(
                modelId,
                characteristicSysName,
                bodyId,
                engineSizeId,
                engineTypeId,
                powerId,
                transmissionId,
                yearId));
    }

    @Override
    public ResponseEntity<VehicleSetResDTO> getVehicleSet(Long vehicleSetId) {
        return ResponseEntity.ok(vehicleSetService.findById(vehicleSetId));
    }

    @Override
    public ResponseEntity<VehicleSetResDTO> createVehicleSet(VehicleSetReqDTO vehicleSetReqDTO) {
        return ResponseEntity.ok(vehicleSetService.create(vehicleSetReqDTO));
    }

    @Override
    public ResponseEntity<VehicleSetResDTO> modifyVehicleSet(Long vehicleSetId, VehicleSetReqDTO vehicleSetReqDTO) {
        return ResponseEntity.ok(vehicleSetService.modify(vehicleSetId, vehicleSetReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteVehicleSet(Long vehicleSetId) {
        vehicleSetService.delete(vehicleSetId);
        return ResponseEntity.ok(null);
    }

}
