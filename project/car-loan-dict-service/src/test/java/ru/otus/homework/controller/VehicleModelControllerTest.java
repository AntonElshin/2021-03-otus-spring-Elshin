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
import ru.otus.homework.entity.QVehicleModelEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.service.ReferenceItemService;
import ru.otus.homework.service.VehicleBrandService;
import ru.otus.homework.service.VehicleModelService;

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
@DisplayName("Контроллер для моделей должен")
public class VehicleModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleModelService vehicleModelService;

    @Autowired
    private VehicleBrandService vehicleBrandService;

    @Autowired
    private ReferenceItemService referenceItemService;

    private Long NOT_EXISTS_VEHICLE_BRAND_ID = 1000L;
    private Long NOT_EXISTS_VEHICLE_MODEL_ID = 1000L;

    private final String URL_PATH = "/vehicle-model/vehiclemodel";
    private ReferenceItemEntity productionKind_ForeignCar = null;
    private ReferenceItemEntity vehicleKind_Passenger = null;
    private ReferenceItemEntity vehicleKind_Commercial = null;
    private ReferenceItemEntity propertyForm_FinancedCar = null;
    private ReferenceItemEntity propertyForm_AssetCar = null;

    @BeforeEach
    public void setUp() {

        productionKind_ForeignCar = referenceItemService.findByReferenceSysNameAndItemCode("productionKind", "ForeignCar");
        vehicleKind_Passenger = referenceItemService.findByReferenceSysNameAndItemCode("vehicleKind", "Passenger");
        vehicleKind_Commercial = referenceItemService.findByReferenceSysNameAndItemCode("vehicleKind", "Commercial");
        propertyForm_FinancedCar = referenceItemService.findByReferenceSysNameAndItemCode("propertyForm", "FinancedCar");
        propertyForm_AssetCar = referenceItemService.findByReferenceSysNameAndItemCode("propertyForm", "AssetCar");
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все модели автомобилей по марке")
    @Test
    public void readAllByVehicleBrandId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();
        Long brandId_2 = vehicleBrandsResDTO.get(1).getId();
        Long brandId_3 = vehicleBrandsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не получить все модели автомобилей без идентификатора марки")
    @Test
    public void readAllWithoutVehicleBrandIdError() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s/all"))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;


        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все модели автомобилей по названию")
    @Test
    public void readAllByName() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();
        Long brandId_2 = vehicleBrandsResDTO.get(1).getId();
        Long brandId_3 = vehicleBrandsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_1 + "&name=model name 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_2 + "&name=model name 6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_3 + "&name=model name 11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все модели автомобилей по виду")
    @Test
    public void readAllByVehicleKindId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();
        Long brandId_2 = vehicleBrandsResDTO.get(1).getId();
        Long brandId_3 = vehicleBrandsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_1 + "&kindId=" + vehicleKind_Passenger.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_2 + "&kindId=" + vehicleKind_Commercial.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_3 + "&kindId=" + vehicleKind_Passenger.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все модели автомобилей по форме собственности")
    @Test
    public void readAllByPropertyFormId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();
        Long brandId_2 = vehicleBrandsResDTO.get(1).getId();
        Long brandId_3 = vehicleBrandsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_1 + "&propertyFormId=" + propertyForm_FinancedCar.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_2 + "&propertyFormId=" + propertyForm_FinancedCar.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_3 + "&propertyFormId=" + propertyForm_AssetCar.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все модели автомобилей по параметрам")
    @Test
    public void readAllByParams() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?brandId=" + brandId_1 +
                "&name=model name" +
                "&kindId=" + vehicleKind_Passenger.getId() +
                "&propertyFormId=" + propertyForm_FinancedCar.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все модели автомобилей по марке")
    @Test
    public void browseByVehicleBrandId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();
        Long brandId_2 = vehicleBrandsResDTO.get(1).getId();
        Long brandId_3 = vehicleBrandsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s?brand.id="+ brandId_1 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?brand.id="+ brandId_2 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?brand.id="+ brandId_3 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все модели автомобилей по названию")
    @Test
    public void browseByName() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?name=model name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 10")))
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name 10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=MODEL NAME 10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 10")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все модели автомобилей по виду")
    @Test
    public void browseByVehicleKindId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&kind.id="+ vehicleKind_Passenger.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&kind.id="+ vehicleKind_Commercial.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все модели автомобилей по форме собственности")
    @Test
    public void browseByPropertyFormId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&ownForms.id="+ propertyForm_FinancedCar.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&ownForms.id="+ propertyForm_AssetCar.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все модели автомобилей по параметрам")
    @Test
    public void browseByParams() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?brand.id=" + brandId_1 +
                "&name=model name" +
                "&kind.id=" + vehicleKind_Passenger.getId() +
                "&ownForms.id=" + propertyForm_FinancedCar.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все модели по параметрам постраничная разбивка и сортировка")
    @Test
    public void browseByParamsPagingSorting() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,asc&size=15&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,asc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,asc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 6")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,asc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 11")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,desc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 15")))
                .andExpect(content().string(containsString("model name 14")))
                .andExpect(content().string(containsString("model name 13")))
                .andExpect(content().string(containsString("model name 12")))
                .andExpect(content().string(containsString("model name 11")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,desc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 10")))
                .andExpect(content().string(containsString("model name 9")))
                .andExpect(content().string(containsString("model name 8")))
                .andExpect(content().string(containsString("model name 7")))
                .andExpect(content().string(containsString("model name 6")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=model name&sort=id,desc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 5")))
                .andExpect(content().string(containsString("model name 4")))
                .andExpect(content().string(containsString("model name 3")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("model name 1")))
        ;

        deleteVehicleModels(vehicleModelsResDTO);
        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить модель по идентификатору")
    @Test
    public void read() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        this.mockMvc.perform(get(URL_PATH + "/" + vehicleModelResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать модель")
    @Test
    public void create() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("model name 1")))
        ;

        List<VehicleModelResDTO> vehicleModelsResDTO = vehicleModelService.findByParams(QVehicleModelEntity.vehicleModelEntity.name.equalsIgnoreCase(vehicleModelReqDTO.getName()));
        assertThat(vehicleModelsResDTO.size()).isEqualTo(1);
        assertThat(vehicleModelsResDTO.get(0).getName()).isEqualTo("model name 1");

        if(vehicleModelsResDTO != null && vehicleModelsResDTO.size() == 1) {
            Long id = vehicleModelsResDTO.get(0).getId();
            vehicleModelService.delete(id);
        }

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать модель из-за неуникального названия")
    @Test
    public void createNotUniqueNameError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

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
    @DisplayName("не создать модель из-за несуществующей марки")
    @Test
    public void createNotValidBrandError() throws Exception {

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(NOT_EXISTS_VEHICLE_BRAND_ID), "model name 1",
                createRefItemReqIdDTO(propertyForm_FinancedCar.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать модель из-за некорректного вида")
    @Test
    public void createNotValidProductionTypeError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(propertyForm_FinancedCar.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать модель из-за некорректной формы собственности")
    @Test
    public void createNotValidPropertyFormError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать модель без марки")
    @Test
    public void createWithoutBrandError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(null, "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать модель без названия")
    @Test
    public void createWithoutNameError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), null,
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать модель без вида")
    @Test
    public void createWithoutVehicleKindError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                null, List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(vehicleModelReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующую модель")
    @Test
    public void update() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO_1 = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO_1 = vehicleBrandService.create(vehicleBrandReqDTO_1);

        VehicleBrandReqDTO vehicleBrandReqDTO_2 = createVehicleBrandReqDTO("brand name 2", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO_2 = vehicleBrandService.create(vehicleBrandReqDTO_2);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO_1.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO_2.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Commercial.getId()), List.of(createRefItemReqIdDTO(propertyForm_AssetCar.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleModelResDTO.getId()))
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("model name 2")))
                .andExpect(content().string(containsString("" + vehicleKind_Commercial.getId())))
                .andExpect(content().string(containsString("" + propertyForm_AssetCar.getId())))
        ;

        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO_1.getId());
        vehicleBrandService.delete(vehicleBrandResDTO_2.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить несуществующую модель")
    @Test
    public void updateNotExistModelId() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + NOT_EXISTS_VEHICLE_MODEL_ID).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель из-за несуществующей марки")
    @Test
    public void updateNotValidBrand() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(NOT_EXISTS_VEHICLE_BRAND_ID), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель из-за некорректного вида")
    @Test
    public void updateNotValidVehicleKind() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(propertyForm_FinancedCar.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель из-за некорректной формы собственности")
    @Test
    public void updateNotValidPropertyForm() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель из-за неуникального названия")
    @Test
    public void updateNotUniqueName() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель без марки")
    @Test
    public void updateWithoutBrandError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(null, "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель без названия")
    @Test
    public void updateWithoutNameError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), null,
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую модель без вида")
    @Test
    public void updateWithoutVehicleKindError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_1 = vehicleModelService.create(vehicleModelReqDTO_1);

        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO_2 = vehicleModelService.create(vehicleModelReqDTO_2);

        VehicleModelReqDTO updateVehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 2",
                null, List.of(createRefItemReqIdDTO(vehicleKind_Passenger.getId())));

        String json = new ObjectMapper().writeValueAsString(updateVehicleModelReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleModelResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleModelService.delete(vehicleModelResDTO_1.getId());
        vehicleModelService.delete(vehicleModelResDTO_2.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующую модель")
    @Test
    public void delete() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleModelReqDTO vehicleModelReqDTO = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(vehicleBrandResDTO.getId()), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        VehicleModelResDTO vehicleModelResDTO = vehicleModelService.create(vehicleModelReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + vehicleModelResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<VehicleModelResDTO> vehicleModelsResDTO = vehicleModelService.findByParams(QVehicleModelEntity.vehicleModelEntity.name.equalsIgnoreCase(vehicleModelReqDTO.getName()));

        assertThat(vehicleModelsResDTO.size()).isEqualTo(0);

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующую модель")
    @Test
    public void deleteNotExistModelError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_VEHICLE_MODEL_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

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
        VehicleBrandReqDTO vehicleBrandReqDTO_2 = createVehicleBrandReqDTO("brand name 2", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_2));
        VehicleBrandReqDTO vehicleBrandReqDTO_3 = createVehicleBrandReqDTO("brand name 3", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_3));

        return vehicleBrandsResDTO;

    }

    private List<VehicleModelResDTO> createVehicleModels(List<VehicleBrandResDTO> vehicleBrandsResDTO) {

        List<VehicleModelResDTO> vehicleModelsResDTO = new ArrayList<>();

        Long brandId_1 = vehicleBrandsResDTO.get(0).getId();
        Long brandId_2 = vehicleBrandsResDTO.get(1).getId();
        Long brandId_3 = vehicleBrandsResDTO.get(2).getId();

        VehicleModelReqDTO vehicleModelReqDTO_1 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 1",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_1));
        VehicleModelReqDTO vehicleModelReqDTO_2 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 2",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_2));
        VehicleModelReqDTO vehicleModelReqDTO_3 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 3",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_3));
        VehicleModelReqDTO vehicleModelReqDTO_4 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 4",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_4));
        VehicleModelReqDTO vehicleModelReqDTO_5 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_1), "model name 5",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_5));

        VehicleModelReqDTO vehicleModelReqDTO_6 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_2), "model name 6",
                createRefItemReqIdDTO(vehicleKind_Commercial.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_6));
        VehicleModelReqDTO vehicleModelReqDTO_7 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_2), "model name 7",
                createRefItemReqIdDTO(vehicleKind_Commercial.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_7));
        VehicleModelReqDTO vehicleModelReqDTO_8 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_2), "model name 8",
                createRefItemReqIdDTO(vehicleKind_Commercial.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_8));
        VehicleModelReqDTO vehicleModelReqDTO_9 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_2), "model name 9",
                createRefItemReqIdDTO(vehicleKind_Commercial.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_9));
        VehicleModelReqDTO vehicleModelReqDTO_10 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_2), "model name 10",
                createRefItemReqIdDTO(vehicleKind_Commercial.getId()), List.of(createRefItemReqIdDTO(propertyForm_FinancedCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_10));

        VehicleModelReqDTO vehicleModelReqDTO_11 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_3), "model name 11",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_AssetCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_11));
        VehicleModelReqDTO vehicleModelReqDTO_12 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_3), "model name 12",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_AssetCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_12));
        VehicleModelReqDTO vehicleModelReqDTO_13 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_3), "model name 13",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_AssetCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_13));
        VehicleModelReqDTO vehicleModelReqDTO_14 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_3), "model name 14",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_AssetCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_14));
        VehicleModelReqDTO vehicleModelReqDTO_15 = createVehicleModelReqDTO(createVehicleBrandReqIdDTO(brandId_3), "model name 15",
                createRefItemReqIdDTO(vehicleKind_Passenger.getId()), List.of(createRefItemReqIdDTO(propertyForm_AssetCar.getId())));
        vehicleModelsResDTO.add(vehicleModelService.create(vehicleModelReqDTO_15));

        return vehicleModelsResDTO;
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

}
