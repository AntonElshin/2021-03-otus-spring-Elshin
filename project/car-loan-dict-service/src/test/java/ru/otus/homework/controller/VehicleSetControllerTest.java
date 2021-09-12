package ru.otus.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.homework.dto.*;
import ru.otus.homework.entity.QVehicleSetEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.service.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллер для комплектаций должен")
public class VehicleSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleSetYearService vehicleSetYearService;

    @Autowired
    private VehicleSetService vehicleSetService;

    @Autowired
    private VehicleModelService vehicleModelService;

    @Autowired
    private VehicleBrandService vehicleBrandService;

    @Autowired
    private ReferenceItemService referenceItemService;

    private Long NOT_EXISTS_VEHICLE_MODEL_ID = 1000L;
    private Long NOT_EXISTS_VEHICLE_SET_ID = 1000L;

    private final String URL_PATH = "/vehicle-set/vehicleset";
    private ReferenceItemEntity productionKind_ForeignCar = null;
    private ReferenceItemEntity propertyForm_FinancedCar = null;
    private ReferenceItemEntity vehicleKind_Passenger = null;
    private ReferenceItemEntity bodyType_12 = null;
    private ReferenceItemEntity engineCapacity_1 = null;
    private ReferenceItemEntity engineType_B = null;
    private ReferenceItemEntity enginePower_6 = null;
    private ReferenceItemEntity transmission_M = null;
    private ReferenceItemEntity transmission_A = null;
    private ReferenceItemEntity yearOfManufacture_2018 = null;
    private ReferenceItemEntity yearOfManufacture_2017 = null;
    private ReferenceItemEntity yearOfManufacture_2016 = null;
    private ReferenceItemEntity yearOfManufacture_2015 = null;
    private ReferenceItemEntity yearOfManufacture_2014 = null;
    private ReferenceItemEntity yearOfManufacture_2013 = null;
    private ReferenceItemEntity yearOfManufacture_2012 = null;
    private ReferenceItemEntity yearOfManufacture_2011 = null;

    @BeforeEach
    public void setUp() {

        productionKind_ForeignCar = referenceItemService.findByReferenceSysNameAndItemCode("productionKind", "ForeignCar");
        vehicleKind_Passenger = referenceItemService.findByReferenceSysNameAndItemCode("vehicleKind", "Passenger");
        propertyForm_FinancedCar = referenceItemService.findByReferenceSysNameAndItemCode("propertyForm", "FinancedCar");

        bodyType_12 = referenceItemService.findByReferenceSysNameAndItemCode("bodyType", "12");
        engineCapacity_1 = referenceItemService.findByReferenceSysNameAndItemCode("engineCapacity", "1");
        engineType_B = referenceItemService.findByReferenceSysNameAndItemCode("engineType", "Б");
        enginePower_6 = referenceItemService.findByReferenceSysNameAndItemCode("enginePower", "6");
        transmission_M = referenceItemService.findByReferenceSysNameAndItemCode("transmission", "М");
        transmission_A = referenceItemService.findByReferenceSysNameAndItemCode("transmission", "А");

        yearOfManufacture_2018 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2018");
        yearOfManufacture_2017 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2017");
        yearOfManufacture_2016 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2016");
        yearOfManufacture_2015 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2015");
        yearOfManufacture_2014 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2014");
        yearOfManufacture_2013 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2013");
        yearOfManufacture_2012 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2012");
        yearOfManufacture_2011 = referenceItemService.findByReferenceSysNameAndItemCode("yearOfManufacture", "2011");

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по модели")
    @Test
    public void readAllByVehicleModelId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не получить комплектации без модели")
    @Test
    public void readAllWithoutModelIdError() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s/all?"))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по кузову")
    @Test
    public void readAllByBodyId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 + "&bodyId=" + bodyType_12.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по объёму двигателя")
    @Test
    public void readAllByEngineSizeId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 + "&engineSizeId=" + engineCapacity_1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по типу двигателя")
    @Test
    public void readAllByEngineTypeId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 + "&engineTypeId=" + engineType_B.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по типу двигателя")
    @Test
    public void readAllByPowerId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 + "&powerId=" + enginePower_6.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по трансмиссии")
    @Test
    public void readAllByTransmissionId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 + "&transmissionId=" + transmission_A.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 + "&transmissionId=" + transmission_M.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по году выпуска")
    @Test
    public void readAllByYearId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 +
                "&yearId=" + yearOfManufacture_2018.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 +
                "&yearId=" + yearOfManufacture_2015.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2015.getId().toString())))
                .andExpect(content().string(containsString("100003")))
                .andExpect(content().string(containsString("100008")))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комплектации автомобилей по параметрам")
    @Test
    public void readAllByParams() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 +
                "&bodyId=" + bodyType_12.getId() +
                "&engineSizeId=" + engineCapacity_1.getId() +
                "&engineTypeId=" + engineType_B.getId() +
                "&powerId=" + enginePower_6.getId() +
                "&transmissionId=" + transmission_A.getId() +
                "&yearId=" + yearOfManufacture_2018.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?modelId=" + modelId_1 +
                "&bodyId=" + bodyType_12.getId() +
                "&engineSizeId=" + engineCapacity_1.getId() +
                "&engineTypeId=" + engineType_B.getId() +
                "&powerId=" + enginePower_6.getId() +
                "&transmissionId=" + transmission_M.getId() +
                "&yearId=" + yearOfManufacture_2015.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2015.getId().toString())))
                .andExpect(content().string(containsString("100008")))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по модели")
    @Test
    public void browseByVehicleModelId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по кузову")
    @Test
    public void browseByBodyId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&body.id=" + bodyType_12.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по объёму двигателя")
    @Test
    public void browseByEngineSizeId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&engineSize.id=" + engineCapacity_1.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по типу двигателя")
    @Test
    public void browseByEngineTypeId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&engineType.id=" + engineType_B.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по мощности")
    @Test
    public void browseByPowerId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&power.id=" + enginePower_6.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по трансмиссии")
    @Test
    public void browseByTransmissionId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&transmission.id=" + transmission_A.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&transmission.id=" + transmission_M.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по году выпуска")
    @Test
    public void browseByYearId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&years.year.id=" + yearOfManufacture_2018.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&years.year.id=" + yearOfManufacture_2015.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2015.getId().toString())))
                .andExpect(content().string(containsString("100003")))
                .andExpect(content().string(containsString("100008")))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все комплектации автомобилей по параметрам")
    @Test
    public void browseByParams() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&transmission.id=" + transmission_A.getId() +
                "&body.id=" + bodyType_12.getId() +
                "&engineSize.id=" + engineCapacity_1.getId() +
                "&engineType.id=" + engineType_B.getId() +
                "&power.id=" + enginePower_6.getId() +
                "&years.year.id=" + yearOfManufacture_2018.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?model.id="+ modelId_1 +
                "&body.id=" + bodyType_12.getId() +
                "&engineSize.id=" + engineCapacity_1.getId() +
                "&engineType.id=" + engineType_B.getId() +
                "&power.id=" + enginePower_6.getId() +
                "&transmission.id=" + transmission_M.getId() +
                "&years.year.id=" + yearOfManufacture_2015.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
                .andExpect(content().string(containsString(yearOfManufacture_2015.getId().toString())))
                .andExpect(content().string(containsString("100008")))
        ;

        deleteVehicleSetYears(vehicleSetYearsResDTO);
        deleteVehicleSets(vehicleSetsResDTO);
        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить комлектацию по идентификатору")
    @Test
    public void read() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        this.mockMvc.perform(get(URL_PATH + "/" + vehicleSetResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bodyType_12.getId().toString())))
                .andExpect(content().string(containsString(engineCapacity_1.getId().toString())))
                .andExpect(content().string(containsString(engineType_B.getId().toString())))
                .andExpect(content().string(containsString(enginePower_6.getId().toString())))
                .andExpect(content().string(containsString(transmission_A.getId().toString())))
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать комплектацию")
    @Test
    public void create() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
        ;

        List<VehicleSetResDTO> vehicleSetsResDTO = vehicleSetService.findByParams(QVehicleSetEntity.vehicleSetEntity.model.id.eq(vehicleModelResDTO.getId()));
        assertThat(vehicleSetsResDTO.size()).isEqualTo(1);

        if(vehicleSetsResDTO != null && vehicleSetsResDTO.size() == 1) {
            Long id = vehicleSetsResDTO.get(0).getId();
            vehicleSetService.delete(id);
        }

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать комплектацию из-за несуществующей модели")
    @Test
    public void createNotValidModelError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(NOT_EXISTS_VEHICLE_MODEL_ID),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать комплектацию из-за некорректного кузова")
    @Test
    public void createNotValidBodyError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать комплектацию из-за некорректного объёма двигателя")
    @Test
    public void createNotValidEngineSizeError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать комплектацию из-за некорректного типа двигателя")
    @Test
    public void createNotValidEngineTypeError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать комплектацию из-за некорректной мощности")
    @Test
    public void createNotValidPowerError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать комплектацию из-за некорректной трансмиссии")
    @Test
    public void createNotValidTransmissionError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(enginePower_6.getId())
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующую комплектацию")
    @Test
    public void update() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO_1.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO_2.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleSetResDTO.getId()))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString(transmission_M.getId().toString())))
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить несуществующую комплектацию")
    @Test
    public void updateNotExistSetError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + NOT_EXISTS_VEHICLE_SET_ID).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить комплектацию из-за некорректного кузова")
    @Test
    public void updateNotValidBodyError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить комплектацию из-за некорректного объёма двигателя")
    @Test
    public void updateNotValidEngineSizeError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить комплектацию из-за некорректного типа двигателя")
    @Test
    public void updateNotValidEngineTypeError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить комплектацию из-за некорректной мощности")
    @Test
    public void updateNotValidPowerError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить комплектацию из-за некорректной трансмиссии")
    @Test
    public void updateNotValidTransmissionError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetReqDTO updateVehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(enginePower_6.getId())
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующую комплектацию")
    @Test
    public void delete() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleSetReqDTO vehicleSetReqDTO = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(vehicleModelResDTO.getId()),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + vehicleSetResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<VehicleSetResDTO> vehicleSetsResDTO = vehicleSetService.findByParams(QVehicleSetEntity.vehicleSetEntity.model.id.eq(vehicleModelResDTO.getId()));

        assertThat(vehicleSetsResDTO.size()).isEqualTo(0);

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующую комплектацию")
    @Test
    public void deleteNotExistSetError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_VEHICLE_SET_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    private VehicleSetYearReqDTO createVehicleSetYearReqDTO(VehicleSetReqIdDTO set, RefItemReqIdDTO year, Double price) {
        VehicleSetYearReqDTO vehicleSetYearReqDTO = new VehicleSetYearReqDTO();
        vehicleSetYearReqDTO.setSet(set);
        vehicleSetYearReqDTO.setYear(year);
        vehicleSetYearReqDTO.setPrice(price);
        return vehicleSetYearReqDTO;
    }

    private VehicleSetReqIdDTO createVehicleSetReqIdDTO(Long id) {
        VehicleSetReqIdDTO vehicleSetReqIdDTO = new VehicleSetReqIdDTO();
        vehicleSetReqIdDTO.setId(id);
        return vehicleSetReqIdDTO;
    }

    private VehicleSetReqDTO createVehicleSetReqDTO(
            VehicleModelReqIdDTO model,
            RefItemReqIdDTO body,
            RefItemReqIdDTO engineSize,
            RefItemReqIdDTO engineType,
            RefItemReqIdDTO power,
            RefItemReqIdDTO transmission
            )
    {
        VehicleSetReqDTO vehicleSetReqDTO = new VehicleSetReqDTO();
        vehicleSetReqDTO.setModel(model);
        vehicleSetReqDTO.setBody(body);
        vehicleSetReqDTO.setEngineSize(engineSize);
        vehicleSetReqDTO.setEngineType(engineType);
        vehicleSetReqDTO.setPower(power);
        vehicleSetReqDTO.setTransmission(transmission);
        return vehicleSetReqDTO;
    }

    private VehicleModelReqIdDTO createVehicleModelReqIdDTO(Long id) {
        VehicleModelReqIdDTO vehicleModelReqIdDTO = new VehicleModelReqIdDTO();
        vehicleModelReqIdDTO.setId(id);
        return vehicleModelReqIdDTO;
    }

    private VehicleModelReqDTO createVehicleModelReqDTO(VehicleBrandReqIdDTO brand, String name, RefItemReqIdDTO kind, List<RefItemReqIdDTO> ownForms) {
        VehicleModelReqDTO vehicleModelReqDTO = new VehicleModelReqDTO();
        vehicleModelReqDTO.setBrand(brand);
        vehicleModelReqDTO.setName(name);
        vehicleModelReqDTO.setKind(kind);
        vehicleModelReqDTO.setOwnForms(ownForms);
        return vehicleModelReqDTO;
    }

    private VehicleBrandReqDTO createVehicleBrandReqDTO(String name, RefItemReqIdDTO productionKind) {
        VehicleBrandReqDTO vehicleBrandReqDTO = new VehicleBrandReqDTO();
        vehicleBrandReqDTO.setName(name);
        vehicleBrandReqDTO.setProductionKind(productionKind);
        return vehicleBrandReqDTO;
    }

    private RefItemReqIdDTO createRefItemReqIdDTO(Long id) {
        RefItemReqIdDTO refItemReqIdDTO = new RefItemReqIdDTO();
        refItemReqIdDTO.setId(id);
        return refItemReqIdDTO;
    }

    private VehicleBrandReqIdDTO createVehicleBrandReqIdDTO(Long id) {
        VehicleBrandReqIdDTO vehicleBrandReqIdDTO = new VehicleBrandReqIdDTO();
        vehicleBrandReqIdDTO.setId(id);
        return vehicleBrandReqIdDTO;
    }

    private List<VehicleBrandResDTO> createVehicleBrands() {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = new ArrayList<>();

        VehicleBrandReqDTO vehicleBrandReqDTO_1 = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_1));

        return vehicleBrandsResDTO;

    }

    private List<VehicleModelResDTO> createVehicleModels(List<VehicleBrandResDTO> vehicleBrandsResDTO) {

        List<VehicleModelResDTO> vehicleModelsResDTO = new ArrayList<>();

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_1));
        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        return vehicleModelsResDTO;
    }

    private List<VehicleSetResDTO> createVehicleSets(List<VehicleModelResDTO> vehicleModelsResDTO) {

        List<VehicleSetResDTO> vehicleSetsResDTO = new ArrayList<>();

        Long modelId_1 = vehicleModelsResDTO.get(0).getId();

        VehicleSetReqDTO vehicleSetReqDTO_1 = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(modelId_1),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_A.getId())
        );
        VehicleSetResDTO vehicleSetResDTO_1 = vehicleSetService.create(vehicleSetReqDTO_1);
        vehicleSetsResDTO.add(vehicleSetResDTO_1);

        VehicleSetReqDTO vehicleSetReqDTO_2 = createVehicleSetReqDTO(
                createVehicleModelReqIdDTO(modelId_1),
                createRefItemReqIdDTO(bodyType_12.getId()),
                createRefItemReqIdDTO(engineCapacity_1.getId()),
                createRefItemReqIdDTO(engineType_B.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                createRefItemReqIdDTO(transmission_M.getId())
        );
        VehicleSetResDTO vehicleSetResDTO_2 = vehicleSetService.create(vehicleSetReqDTO_2);
        vehicleSetsResDTO.add(vehicleSetResDTO_2);

        return vehicleSetsResDTO;
    }

    private List<VehicleSetYearResDTO> createVehicleSetYears(List<VehicleSetResDTO> vehicleSetsResDTO) {

        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = new ArrayList<>();

        Long setId_1 = vehicleSetsResDTO.get(0).getId();
        Long setId_2 = vehicleSetsResDTO.get(1).getId();

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_1 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_1));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_2 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2017.getId()),
                Double.valueOf(100001)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_2));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_3 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2016.getId()),
                Double.valueOf(100002)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_3));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_4 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2015.getId()),
                Double.valueOf(100003)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_4));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_5 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2014.getId()),
                Double.valueOf(100004)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_5));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_6 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2013.getId()),
                Double.valueOf(100005)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_6));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_7 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2012.getId()),
                Double.valueOf(100006)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_7));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_1_8 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_1),
                createRefItemReqIdDTO(yearOfManufacture_2011.getId()),
                Double.valueOf(100007)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_1_8));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_2_1 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_2),
                createRefItemReqIdDTO(yearOfManufacture_2015.getId()),
                Double.valueOf(100008)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_2_1));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_2_2 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_2),
                createRefItemReqIdDTO(yearOfManufacture_2013.getId()),
                Double.valueOf(100009)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_2_2));

        VehicleSetYearReqDTO vehicleSetYearReqDTO_2_3 = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(setId_2),
                createRefItemReqIdDTO(yearOfManufacture_2012.getId()),
                Double.valueOf(100010)
        );
        vehicleSetYearsResDTO.add(vehicleSetYearService.create(vehicleSetYearReqDTO_2_3));

        return vehicleSetYearsResDTO;

    }

    private void deleteVehicleBrands(List<VehicleBrandResDTO> vehicleBransResDTO) {

        for(int i = vehicleBransResDTO.size() - 1; i >= 0; i--) {
            vehicleBrandService.delete(vehicleBransResDTO.get(i).getId());
        }

    }

    private void deleteVehicleModels(List<VehicleModelResDTO> vehicleModelsResDTO) {

        for(int i = vehicleModelsResDTO.size() - 1; i >= 0; i--) {
            vehicleModelService.delete(vehicleModelsResDTO.get(i).getId());
        }

    }

    private void deleteVehicleSets(List<VehicleSetResDTO> vehicleSetsResDTO) {

        for(int i = vehicleSetsResDTO.size() - 1; i >= 0; i--) {
            vehicleSetService.delete(vehicleSetsResDTO.get(i).getId());
        }

    }

    private void deleteVehicleSetYears(List<VehicleSetYearResDTO> vehicleSetYearsResDTO) {

        for(int i = vehicleSetYearsResDTO.size() - 1; i >= 0; i--) {
            vehicleSetYearService.delete(vehicleSetYearsResDTO.get(i).getId());
        }

    }

}
