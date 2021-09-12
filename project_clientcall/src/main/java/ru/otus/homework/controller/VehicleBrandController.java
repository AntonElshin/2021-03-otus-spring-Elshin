package ru.otus.homework.controller;

//import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.service.VehicleBrandService;
import ru.otus.homework.dto.*;

import java.util.List;

@RestController
public class VehicleBrandController {

    private VehicleBrandService vehicleBrandService;

    public VehicleBrandController(VehicleBrandService vehicleBrandService) {
        this.vehicleBrandService = vehicleBrandService;
    }

    @GetMapping(value = "/api/vehiclebrands/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleBrandResDTO>> getAllVehicleBrands() {
        return ResponseEntity.ok(vehicleBrandService.findAll());
    }

}
