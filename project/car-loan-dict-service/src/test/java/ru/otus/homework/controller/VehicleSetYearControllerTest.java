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
import ru.otus.homework.entity.QVehicleSetYearEntity;
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
@DisplayName("Контроллер для годов выпуска и цен комплектаций должен")
public class VehicleSetYearControllerTest {

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

    private Long NOT_EXISTS_VEHICLE_SET_ID = 1000L;
    private Long NOT_EXISTS_VEHICLE_SET_YEAR_ID = 1000L;

    private final String URL_PATH = "/vehicle-set-year/vehiclesetyear";
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
    @DisplayName("получить все года выпуска и цены комплектаций по комплектации")
    @Test
    public void readAllByVehicleSetId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long setId_1 = vehicleSetsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?setId=" + setId_1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
                .andExpect(content().string(containsString(yearOfManufacture_2017.getId().toString())))
                .andExpect(content().string(containsString("100001")))
                .andExpect(content().string(containsString(yearOfManufacture_2016.getId().toString())))
                .andExpect(content().string(containsString("100002")))
                .andExpect(content().string(containsString(yearOfManufacture_2015.getId().toString())))
                .andExpect(content().string(containsString("100003")))
                .andExpect(content().string(containsString(yearOfManufacture_2014.getId().toString())))
                .andExpect(content().string(containsString("100004")))
                .andExpect(content().string(containsString(yearOfManufacture_2013.getId().toString())))
                .andExpect(content().string(containsString("100005")))
                .andExpect(content().string(containsString(yearOfManufacture_2012.getId().toString())))
                .andExpect(content().string(containsString("100006")))
                .andExpect(content().string(containsString(yearOfManufacture_2011.getId().toString())))
                .andExpect(content().string(containsString("100007")))
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
    @DisplayName("не получить года выпуска и цены комплектаций без комплектации")
    @Test
    public void readAllWithoutSetIdError() throws Exception {

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
    @DisplayName("получить все года выпуска и цены комплектаций по году")
    @Test
    public void readAllByYear() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long setId_1 = vehicleSetsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?setId=" + setId_1 + "&yearId=" + yearOfManufacture_2018.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
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
    @DisplayName("(browse) получить все года выпуска и цены комплектаций автомобилей по комплектации")
    @Test
    public void browseByVehicleModelId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long setId_1 = vehicleSetsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?set.id="+ setId_1 +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
                .andExpect(content().string(containsString(yearOfManufacture_2017.getId().toString())))
                .andExpect(content().string(containsString("100001")))
                .andExpect(content().string(containsString(yearOfManufacture_2016.getId().toString())))
                .andExpect(content().string(containsString("100002")))
                .andExpect(content().string(containsString(yearOfManufacture_2015.getId().toString())))
                .andExpect(content().string(containsString("100003")))
                .andExpect(content().string(containsString(yearOfManufacture_2014.getId().toString())))
                .andExpect(content().string(containsString("100004")))
                .andExpect(content().string(containsString(yearOfManufacture_2013.getId().toString())))
                .andExpect(content().string(containsString("100005")))
                .andExpect(content().string(containsString(yearOfManufacture_2012.getId().toString())))
                .andExpect(content().string(containsString("100006")))
                .andExpect(content().string(containsString(yearOfManufacture_2011.getId().toString())))
                .andExpect(content().string(containsString("100007")))
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
    @DisplayName("(browse) получить все года выпуска и цены комплектаций автомобилей по году")
    @Test
    public void browseByYear() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();
        List<VehicleModelResDTO> vehicleModelsResDTO = createVehicleModels(vehicleBrandsResDTO);
        List<VehicleSetResDTO> vehicleSetsResDTO = createVehicleSets(vehicleModelsResDTO);
        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = createVehicleSetYears(vehicleSetsResDTO);

        Long setId_1 = vehicleSetsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?set.id="+ setId_1 +
                "&year.id=" + yearOfManufacture_2018.getId() +
                "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
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
    @DisplayName("получить года выпуска и цену по идентификатору пары год-цена")
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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );
        VehicleSetYearResDTO vehicleSetYearResDTO = vehicleSetYearService.create(vehicleSetYearReqDTO);

        this.mockMvc.perform(get(URL_PATH + "/" + vehicleSetYearResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
        ;

        vehicleSetYearService.delete(vehicleSetYearResDTO.getId());
        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать пару год-цена")
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
        VehicleSetResDTO vehicleSetResDTO = vehicleSetService.create(vehicleSetReqDTO);

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetYearReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(yearOfManufacture_2018.getId().toString())))
                .andExpect(content().string(containsString("100000")))
        ;

        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = vehicleSetYearService.findByParams(QVehicleSetYearEntity.vehicleSetYearEntity.set.id.eq(vehicleSetResDTO.getId()));
        assertThat(vehicleSetYearsResDTO.size()).isEqualTo(1);

        if(vehicleSetYearsResDTO != null && vehicleSetYearsResDTO.size() == 1) {
            Long id = vehicleSetYearsResDTO.get(0).getId();
            vehicleSetYearService.delete(id);
        }

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать пару год-цена из-за несуществующей комплектации")
    @Test
    public void createNotExistSetError() throws Exception {

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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(NOT_EXISTS_VEHICLE_SET_ID),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetYearReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("не создать пару год-цена из-за несуществующего года")
    @Test
    public void createNotValidYearError() throws Exception {

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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(enginePower_6.getId()),
                Double.valueOf(100000)
        );

        String json = new ObjectMapper().writeValueAsString(vehicleSetYearReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("обновить пару год-цена")
    @Test
    public void update() throws Exception {

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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );
        VehicleSetYearResDTO vehicleSetYearResDTO = vehicleSetYearService.create(vehicleSetYearReqDTO);

        VehicleSetYearReqDTO updateVehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2017.getId()),
                Double.valueOf(100001)
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetYearReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetYearResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleSetYearResDTO.getId()))
                .andExpect(content().string(containsString(yearOfManufacture_2017.getId().toString())))
                .andExpect(content().string(containsString("100001")))
        ;

        vehicleSetYearService.delete(vehicleSetYearResDTO.getId());
        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить пару год-цена из-за несуществующей комплектации")
    @Test
    public void updateNotExistSet() throws Exception {

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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );
        VehicleSetYearResDTO vehicleSetYearResDTO = vehicleSetYearService.create(vehicleSetYearReqDTO);

        VehicleSetYearReqDTO updateVehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(NOT_EXISTS_VEHICLE_SET_ID),
                createRefItemReqIdDTO(yearOfManufacture_2017.getId()),
                Double.valueOf(100001)
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetYearReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetYearResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetYearService.delete(vehicleSetYearResDTO.getId());
        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить пару год-цена из-за некорректного года")
    @Test
    public void updateNotValidYear() throws Exception {

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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );
        VehicleSetYearResDTO vehicleSetYearResDTO = vehicleSetYearService.create(vehicleSetYearReqDTO);

        VehicleSetYearReqDTO updateVehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetYearResDTO.getId()),
                createRefItemReqIdDTO(transmission_A.getId()),
                Double.valueOf(100001)
        );

        String json = new ObjectMapper().writeValueAsString(updateVehicleSetYearReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleSetYearResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleSetYearService.delete(vehicleSetYearResDTO.getId());
        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующую пару год-цена")
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

        VehicleSetYearReqDTO vehicleSetYearReqDTO = createVehicleSetYearReqDTO(
                createVehicleSetReqIdDTO(vehicleSetResDTO.getId()),
                createRefItemReqIdDTO(yearOfManufacture_2018.getId()),
                Double.valueOf(100000)
        );
        VehicleSetYearResDTO vehicleSetYearResDTO = vehicleSetYearService.create(vehicleSetYearReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + vehicleSetYearResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<VehicleSetYearResDTO> vehicleSetYearsResDTO = vehicleSetYearService.findByParams(QVehicleSetYearEntity.vehicleSetYearEntity.set.id.eq(vehicleSetResDTO.getId()));

        assertThat(vehicleSetYearsResDTO.size()).isEqualTo(0);

        vehicleSetService.delete(vehicleSetResDTO.getId());
        vehicleModelService.delete(vehicleModelResDTO.getId());
        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующую пару год-цена")
    @Test
    public void deleteNotExistSetError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_VEHICLE_SET_YEAR_ID))
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
