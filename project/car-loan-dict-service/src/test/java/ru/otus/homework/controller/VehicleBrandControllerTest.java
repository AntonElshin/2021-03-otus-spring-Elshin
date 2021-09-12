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
import ru.otus.homework.dto.RefItemReqIdDTO;
import ru.otus.homework.dto.VehicleBrandReqDTO;
import ru.otus.homework.dto.VehicleBrandResDTO;
import ru.otus.homework.entity.QVehicleBrandEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.service.ReferenceItemService;
import ru.otus.homework.service.VehicleBrandService;

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
@DisplayName("Контроллер для марок должен")
public class VehicleBrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleBrandService vehicleBrandService;

    @Autowired
    private ReferenceItemService referenceItemService;

    private Long NOT_EXISTS_VEHICLE_BRAND_ID = 1000L;

    private final String URL_PATH = "/vehicle-brand/vehiclebrand";
    private ReferenceItemEntity productionKind_ForeignCar = null;
    private ReferenceItemEntity productionKind_NativeCar = null;
    private ReferenceItemEntity engineCapacity_1 = null;

    @BeforeEach
    public void setUp() {

        productionKind_ForeignCar = referenceItemService.findByReferenceSysNameAndItemCode("productionKind", "ForeignCar");
        productionKind_NativeCar = referenceItemService.findByReferenceSysNameAndItemCode("productionKind", "NativeCar");
        engineCapacity_1 = referenceItemService.findByReferenceSysNameAndItemCode("engineCapacity", "1");

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все марки автомобилей")
    @Test
    public void readAll() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
                .andExpect(content().string(containsString("brand name 11")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 15")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все марки автомобилей по названию")
    @Test
    public void readAllByName() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s/all?name=brand name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
                .andExpect(content().string(containsString("brand name 11")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 15")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все марки автомобилей по типу производства")
    @Test
    public void readAllByProductionKindId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s/all?name=brand name&productionKindId=" + productionKind_ForeignCar.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
                .andExpect(content().string(containsString("brand name 11")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 15")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все марки автомобилей по названию")
    @Test
    public void browseByName() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 10")))
                .andExpect(content().string(containsString("brand name 11")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name 10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=BRAND NAME 10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 10")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все марки автомобилей по типу производства")
    @Test
    public void browseByProductionKindId() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&productionKind.id=" + productionKind_ForeignCar.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все марки по параметрам")
    @Test
    public void browseByParams() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&productionKind.id=" + productionKind_ForeignCar.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все марки по параметрам постраничная разбивка и сортировка")
    @Test
    public void browseByParamsPagingSorting() throws Exception {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = createVehicleBrands();

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,asc&size=15&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
                .andExpect(content().string(containsString("brand name 11")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,asc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,asc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 6")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,asc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 11")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,desc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 15")))
                .andExpect(content().string(containsString("brand name 14")))
                .andExpect(content().string(containsString("brand name 13")))
                .andExpect(content().string(containsString("brand name 12")))
                .andExpect(content().string(containsString("brand name 11")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,desc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 10")))
                .andExpect(content().string(containsString("brand name 9")))
                .andExpect(content().string(containsString("brand name 8")))
                .andExpect(content().string(containsString("brand name 7")))
                .andExpect(content().string(containsString("brand name 6")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=brand name&sort=id,desc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 5")))
                .andExpect(content().string(containsString("brand name 4")))
                .andExpect(content().string(containsString("brand name 3")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("brand name 1")))
        ;

        deleteVehicleBrands(vehicleBrandsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить марку по идентификатору")
    @Test
    public void read() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        this.mockMvc.perform(get(URL_PATH + "/" + vehicleBrandResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать марку")
    @Test
    public void create() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));

        String json = new ObjectMapper().writeValueAsString(vehicleBrandReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("brand name 1")))
        ;

        List<VehicleBrandResDTO> vehicleBrandsResDTO = vehicleBrandService.findByParams(QVehicleBrandEntity.vehicleBrandEntity.name.equalsIgnoreCase(vehicleBrandReqDTO.getName()));
        assertThat(vehicleBrandsResDTO.size()).isEqualTo(1);
        assertThat(vehicleBrandsResDTO.get(0).getName()).isEqualTo("brand name 1");

        if(vehicleBrandsResDTO != null && vehicleBrandsResDTO.size() == 1) {
            Long id = vehicleBrandsResDTO.get(0).getId();
            vehicleBrandService.delete(id);
        }

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать марку из-за неуникального названия")
    @Test
    public void createNotUniqueNameError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        String json = new ObjectMapper().writeValueAsString(vehicleBrandReqDTO);

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
    @DisplayName("не создать марку из-за некорректного типа производства")
    @Test
    public void createNotValidProductionTypeError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(engineCapacity_1.getId()));

        String json = new ObjectMapper().writeValueAsString(vehicleBrandReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать марку без названия")
    @Test
    public void createWithoutNameError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO(null, createRefItemReqIdDTO(productionKind_ForeignCar.getId()));

        String json = new ObjectMapper().writeValueAsString(vehicleBrandReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующую марку")
    @Test
    public void update() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleBrandReqDTO updateVehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 2", createRefItemReqIdDTO(productionKind_NativeCar.getId()));

        String json = new ObjectMapper().writeValueAsString(updateVehicleBrandReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleBrandResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleBrandResDTO.getId()))
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("brand name 2")))
                .andExpect(content().string(containsString("" + productionKind_NativeCar.getId())))
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить несуществующую марку")
    @Test
    public void updateNotExistBrandId() throws Exception {

        VehicleBrandReqDTO updateVehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));

        String json = new ObjectMapper().writeValueAsString(updateVehicleBrandReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + NOT_EXISTS_VEHICLE_BRAND_ID).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую марку из-за неуникального названия")
    @Test
    public void updateNotUniqueName() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO_1 = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO_1 = vehicleBrandService.create(vehicleBrandReqDTO_1);

        VehicleBrandReqDTO vehicleBrandReqDTO_2 = createVehicleBrandReqDTO("brand name 2", createRefItemReqIdDTO(productionKind_NativeCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO_2 = vehicleBrandService.create(vehicleBrandReqDTO_2);

        VehicleBrandReqDTO updateVehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));

        String json = new ObjectMapper().writeValueAsString(updateVehicleBrandReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleBrandResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO_1.getId());
        vehicleBrandService.delete(vehicleBrandResDTO_2.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую марку без названия")
    @Test
    public void updateWithoutNameError() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        VehicleBrandReqDTO updateVehicleBrandReqDTO = createVehicleBrandReqDTO(null, createRefItemReqIdDTO(productionKind_ForeignCar.getId()));

        String json = new ObjectMapper().writeValueAsString(updateVehicleBrandReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + vehicleBrandResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        vehicleBrandService.delete(vehicleBrandResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующую марку")
    @Test
    public void delete() throws Exception {

        VehicleBrandReqDTO vehicleBrandReqDTO = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        VehicleBrandResDTO vehicleBrandResDTO = vehicleBrandService.create(vehicleBrandReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + vehicleBrandResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<VehicleBrandResDTO> vehicleBrandsResDTO = vehicleBrandService.findByParams(QVehicleBrandEntity.vehicleBrandEntity.name.equalsIgnoreCase(vehicleBrandReqDTO.getName()));

        assertThat(vehicleBrandsResDTO.size()).isEqualTo(0);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующую марку")
    @Test
    public void deleteNotExistBrandError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_VEHICLE_BRAND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

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

    private List<VehicleBrandResDTO> createVehicleBrands() {

        List<VehicleBrandResDTO> vehicleBrandsResDTO = new ArrayList<>();

        VehicleBrandReqDTO vehicleBrandReqDTO_1 = createVehicleBrandReqDTO("brand name 1", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_1));
        VehicleBrandReqDTO vehicleBrandReqDTO_2 = createVehicleBrandReqDTO("brand name 2", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_2));
        VehicleBrandReqDTO vehicleBrandReqDTO_3 = createVehicleBrandReqDTO("brand name 3", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_3));
        VehicleBrandReqDTO vehicleBrandReqDTO_4 = createVehicleBrandReqDTO("brand name 4", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_4));
        VehicleBrandReqDTO vehicleBrandReqDTO_5 = createVehicleBrandReqDTO("brand name 5", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_5));
        VehicleBrandReqDTO vehicleBrandReqDTO_6 = createVehicleBrandReqDTO("brand name 6", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_6));
        VehicleBrandReqDTO vehicleBrandReqDTO_7 = createVehicleBrandReqDTO("brand name 7", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_7));
        VehicleBrandReqDTO vehicleBrandReqDTO_8 = createVehicleBrandReqDTO("brand name 8", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_8));
        VehicleBrandReqDTO vehicleBrandReqDTO_9 = createVehicleBrandReqDTO("brand name 9", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_9));
        VehicleBrandReqDTO vehicleBrandReqDTO_10 = createVehicleBrandReqDTO("brand name 10", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_10));
        VehicleBrandReqDTO vehicleBrandReqDTO_11 = createVehicleBrandReqDTO("brand name 11", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_11));
        VehicleBrandReqDTO vehicleBrandReqDTO_12 = createVehicleBrandReqDTO("brand name 12", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_12));
        VehicleBrandReqDTO vehicleBrandReqDTO_13 = createVehicleBrandReqDTO("brand name 13", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_13));
        VehicleBrandReqDTO vehicleBrandReqDTO_14 = createVehicleBrandReqDTO("brand name 14", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_14));
        VehicleBrandReqDTO vehicleBrandReqDTO_15 = createVehicleBrandReqDTO("brand name 15", createRefItemReqIdDTO(productionKind_ForeignCar.getId()));
        vehicleBrandsResDTO.add(vehicleBrandService.create(vehicleBrandReqDTO_15));

        return vehicleBrandsResDTO;

    }

    private void deleteVehicleBrands(List<VehicleBrandResDTO> vehicleBransResDTO) {

        for(int i = vehicleBransResDTO.size() - 1; i >= 0; i--) {
            vehicleBrandService.delete(vehicleBransResDTO.get(i).getId());
        }

    }

}
