package ru.otus.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.otus.homework.entity.QReferenceItemEntity;
import ru.otus.homework.service.ReferenceGroupService;
import ru.otus.homework.service.ReferenceItemService;
import ru.otus.homework.service.ReferenceService;

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
@DisplayName("Контроллер для элементов справочника должен")
public class ReferenceItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReferenceGroupService referenceGroupService;

    @Autowired
    private ReferenceService referenceService;

    @Autowired
    private ReferenceItemService referenceItemService;

    private final String URL_PATH = "/reference-item/refitem";
    private final Long NOT_EXISTS_REFERENCE_GROUP_ID = 1000L;
    private final Long NOT_EXISTS_REFERENCE_ID = 1000L;
    private final Long NOT_EXISTS_REFERENCE_ITEM_ID = 1000L;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все элементы справочники по идентификатору справочника")
    @Test
    public void readAllByReferenceId() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        Long referenceId_1 = referencesResDTO.get(0).getId();
        Long referenceId_2 = referencesResDTO.get(1).getId();
        Long referenceId_3 = referencesResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?referenceId=" + referenceId_1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("code3")))
                .andExpect(content().string(containsString("name 3")))
                .andExpect(content().string(containsString("brief 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("code4")))
                .andExpect(content().string(containsString("name 4")))
                .andExpect(content().string(containsString("brief 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("code5")))
                .andExpect(content().string(containsString("name 5")))
                .andExpect(content().string(containsString("brief 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?referenceId=" + referenceId_2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code6")))
                .andExpect(content().string(containsString("name 6")))
                .andExpect(content().string(containsString("brief 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("code7")))
                .andExpect(content().string(containsString("name 7")))
                .andExpect(content().string(containsString("brief 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("code8")))
                .andExpect(content().string(containsString("name 8")))
                .andExpect(content().string(containsString("brief 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("code9")))
                .andExpect(content().string(containsString("name 9")))
                .andExpect(content().string(containsString("brief 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?referenceId=" + referenceId_3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все элементы справочники по cистемному наименованию")
    @Test
    public void readAllReferenceSysName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        String referenceSysName_1 = referencesResDTO.get(0).getSysname();
        String referenceSysName_2 = referencesResDTO.get(1).getSysname();
        String referenceSysName_3 = referencesResDTO.get(2).getSysname();

        this.mockMvc.perform(get(URL_PATH + "s/all?referenceSysName=" + referenceSysName_1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("code3")))
                .andExpect(content().string(containsString("name 3")))
                .andExpect(content().string(containsString("brief 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("code4")))
                .andExpect(content().string(containsString("name 4")))
                .andExpect(content().string(containsString("brief 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("code5")))
                .andExpect(content().string(containsString("name 5")))
                .andExpect(content().string(containsString("brief 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?referenceSysName=" + referenceSysName_2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code6")))
                .andExpect(content().string(containsString("name 6")))
                .andExpect(content().string(containsString("brief 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("code7")))
                .andExpect(content().string(containsString("name 7")))
                .andExpect(content().string(containsString("brief 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("code8")))
                .andExpect(content().string(containsString("name 8")))
                .andExpect(content().string(containsString("brief 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("code9")))
                .andExpect(content().string(containsString("name 9")))
                .andExpect(content().string(containsString("brief 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?referenceSysName=" + referenceSysName_3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не получить все элементы справочников без параметров")
    @Test
    public void readAllError() throws Exception {

        this.mockMvc.perform(get(URL_PATH + "s/all"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все элементы справочника по идентификатору справочника")
    @Test
    public void browseByParentId() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        Long referenceId_1 = referencesResDTO.get(0).getId();
        Long referenceId_2 = referencesResDTO.get(1).getId();
        Long referenceId_3 = referencesResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s?reference.id="+ referenceId_1 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("code3")))
                .andExpect(content().string(containsString("name 3")))
                .andExpect(content().string(containsString("brief 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("code4")))
                .andExpect(content().string(containsString("name 4")))
                .andExpect(content().string(containsString("brief 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("code5")))
                .andExpect(content().string(containsString("name 5")))
                .andExpect(content().string(containsString("brief 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?reference.id="+ referenceId_2 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code6")))
                .andExpect(content().string(containsString("name 6")))
                .andExpect(content().string(containsString("brief 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("code7")))
                .andExpect(content().string(containsString("name 7")))
                .andExpect(content().string(containsString("brief 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("code8")))
                .andExpect(content().string(containsString("name 8")))
                .andExpect(content().string(containsString("brief 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("code9")))
                .andExpect(content().string(containsString("name 9")))
                .andExpect(content().string(containsString("brief 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?reference.id="+ referenceId_3 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все элементы справочников по коду")
    @Test
    public void browseBySysName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?code=code1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=CODE10&&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все элементы справочников по названию")
    @Test
    public void browseByName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?name=name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=name 10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=NAME 10&&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все элементы справочников по сокращённому названию")
    @Test
    public void browseByBrief() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?brief=brief 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?brief=brief 10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?brief=BRIEF 10&&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все элементы справочников по параметрам")
    @Test
    public void browseByParams() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        Long referenceId_1 = referencesResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?reference.id=" + referenceId_1 +"&code=code1&name=name 1&brief=brief 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все элементы справочников по параметрам постраничная разбивка и сортировка")
    @Test
    public void browseByParamsPagingSorting() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);
        List<RefItemResDTO> refItemsResDTO = createRefItems(referencesResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?code=code&sort=id,asc&size=15&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("code3")))
                .andExpect(content().string(containsString("name 3")))
                .andExpect(content().string(containsString("brief 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("code4")))
                .andExpect(content().string(containsString("name 4")))
                .andExpect(content().string(containsString("brief 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("code5")))
                .andExpect(content().string(containsString("name 5")))
                .andExpect(content().string(containsString("brief 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("code6")))
                .andExpect(content().string(containsString("name 6")))
                .andExpect(content().string(containsString("brief 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("code7")))
                .andExpect(content().string(containsString("name 7")))
                .andExpect(content().string(containsString("brief 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("code8")))
                .andExpect(content().string(containsString("name 8")))
                .andExpect(content().string(containsString("brief 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("code9")))
                .andExpect(content().string(containsString("name 9")))
                .andExpect(content().string(containsString("brief 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code&sort=id,asc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("code3")))
                .andExpect(content().string(containsString("name 3")))
                .andExpect(content().string(containsString("brief 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("code4")))
                .andExpect(content().string(containsString("name 4")))
                .andExpect(content().string(containsString("brief 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("code5")))
                .andExpect(content().string(containsString("name 5")))
                .andExpect(content().string(containsString("brief 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code&sort=id,asc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code6")))
                .andExpect(content().string(containsString("name 6")))
                .andExpect(content().string(containsString("brief 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("code7")))
                .andExpect(content().string(containsString("name 7")))
                .andExpect(content().string(containsString("brief 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("code8")))
                .andExpect(content().string(containsString("name 8")))
                .andExpect(content().string(containsString("brief 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("code9")))
                .andExpect(content().string(containsString("name 9")))
                .andExpect(content().string(containsString("brief 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code&sort=id,asc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code&sort=id,desc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code15")))
                .andExpect(content().string(containsString("name 15")))
                .andExpect(content().string(containsString("brief 15")))
                .andExpect(content().string(containsString("description 15")))
                .andExpect(content().string(containsString("code14")))
                .andExpect(content().string(containsString("name 14")))
                .andExpect(content().string(containsString("brief 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("code13")))
                .andExpect(content().string(containsString("name 13")))
                .andExpect(content().string(containsString("brief 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("code12")))
                .andExpect(content().string(containsString("name 12")))
                .andExpect(content().string(containsString("brief 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("code11")))
                .andExpect(content().string(containsString("name 11")))
                .andExpect(content().string(containsString("brief 11")))
                .andExpect(content().string(containsString("description 11")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code&sort=id,desc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code10")))
                .andExpect(content().string(containsString("name 10")))
                .andExpect(content().string(containsString("brief 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("code9")))
                .andExpect(content().string(containsString("name 9")))
                .andExpect(content().string(containsString("brief 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("code8")))
                .andExpect(content().string(containsString("name 8")))
                .andExpect(content().string(containsString("brief 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("code7")))
                .andExpect(content().string(containsString("name 7")))
                .andExpect(content().string(containsString("brief 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("code6")))
                .andExpect(content().string(containsString("name 6")))
                .andExpect(content().string(containsString("brief 6")))
                .andExpect(content().string(containsString("description 6")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?code=code&&sort=id,desc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("code5")))
                .andExpect(content().string(containsString("name 5")))
                .andExpect(content().string(containsString("brief 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("code4")))
                .andExpect(content().string(containsString("name 4")))
                .andExpect(content().string(containsString("brief 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("code3")))
                .andExpect(content().string(containsString("name 3")))
                .andExpect(content().string(containsString("brief 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        deleteReferenceItems(refItemsResDTO);
        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить элемент справочника по идентификатору")
    @Test
    public void read() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        this.mockMvc.perform(get(URL_PATH + "/" + refItemResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("" + referenceResDTO.getId())))
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать элемент справочника")
    @Test
    public void create() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(refItemReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("" + referenceResDTO.getId())))
                .andExpect(content().string(containsString("code1")))
                .andExpect(content().string(containsString("name 1")))
                .andExpect(content().string(containsString("brief 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        List<RefItemResDTO> refItemsResDTO = referenceItemService.findByParams(
                QReferenceItemEntity.referenceItemEntity.reference.id.eq(refItemReqDTO.getReference().getId()).and(
                        QReferenceItemEntity.referenceItemEntity.code.equalsIgnoreCase(refItemReqDTO.getCode())
                )
        );
        assertThat(refItemsResDTO.size()).isEqualTo(1);
        assertThat(refItemsResDTO.get(0).getCode()).isEqualTo("code1");
        assertThat(refItemsResDTO.get(0).getName()).isEqualTo("name 1");
        assertThat(refItemsResDTO.get(0).getBrief()).isEqualTo("brief 1");
        assertThat(refItemsResDTO.get(0).getDescription()).isEqualTo("description 1");

        if(refItemsResDTO != null && refItemsResDTO.size() == 1) {
            Long id = refItemsResDTO.get(0).getId();
            referenceItemService.delete(id);
        }

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать элемент справочника из-за несуществующего справочника")
    @Test
    public void createInvalidReferenceIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(NOT_EXISTS_REFERENCE_ID);

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(refItemReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать элемент справочника из-за неуникального кода")
    @Test
    public void createNotUniqueSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        String json = new ObjectMapper().writeValueAsString(refItemReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать элемент справочника без справочника")
    @Test
    public void createWithoutReferenceIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(null, "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать элемент справочника без кода")
    @Test
    public void createWithoutCodeError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, null, "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать элемент справочника без названия")
    @Test
    public void createWithoutNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", null, "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать элемент справочника без названия")
    @Test
    public void createWithoutBriefError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", null, "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующий элемент справочника")
    @Test
    public void update() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO_1 = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO_1 = referenceService.create(referenceReqDTO_1);

        ReferenceReqDTO referenceReqDTO_2 = createReferenceReqDTO(refGroupReqIdDTO, "refsysname2", "ref name 2", "description 2");
        ReferenceResDTO referenceResDTO_2 = referenceService.create(referenceReqDTO_2);

        ReferenceReqIdDTO referenceReqIdDTO_1 = createReferenceReqIdDTO(referenceResDTO_1.getId());
        ReferenceReqIdDTO referenceReqIdDTO_2 = createReferenceReqIdDTO(referenceResDTO_2.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO_1, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(referenceReqIdDTO_2, "code2", "name 2", "brief 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(refItemResDTO.getId()))
                .andExpect(content().string(containsString("" + referenceResDTO_2.getId())))
                .andExpect(content().string(containsString("code2")))
                .andExpect(content().string(containsString("name 2")))
                .andExpect(content().string(containsString("brief 2")))
                .andExpect(content().string(containsString("description 2")))
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO_1.getId());
        referenceService.delete(referenceResDTO_2.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить несуществующий элемент справочника")
    @Test
    public void updateNotExistReferenceId() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + NOT_EXISTS_REFERENCE_ITEM_ID).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceService.delete(referenceReqIdDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующий элемент справочник из-за несуществующего справочника")
    @Test
    public void updateNotExistGroupId() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(createReferenceReqIdDTO(NOT_EXISTS_REFERENCE_ID), "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующий элемент справочника из-за неуникального кода")
    @Test
    public void updateNotUniqueSysName() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO_1 = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO_1 = referenceItemService.create(refItemReqDTO_1);

        RefItemReqDTO refItemReqDTO_2 = createRefItemReqDTO(referenceReqIdDTO, "code2", "name 2", "brief 2", "description 2");
        RefItemResDTO refItemResDTO_2 = referenceItemService.create(refItemReqDTO_2);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceItemService.delete(refItemResDTO_1.getId());
        referenceItemService.delete(refItemResDTO_2.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить элемент справочника без справочника")
    @Test
    public void updateWithoutGroupIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(null, "code1", "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить элемент справочника без кода")
    @Test
    public void updateWithoutCodeError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, null, "name 1", "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить элемент справочника без названия")
    @Test
    public void updateWithoutNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", null, "brief 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить элемент справочника без названия")
    @Test
    public void updateWithoutBriefError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        RefItemReqDTO updateRefItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", null, "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefItemReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refItemResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceItemService.delete(refItemResDTO.getId());
        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующий элемент справочников")
    @Test
    public void delete() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqIdDTO referenceReqIdDTO = createReferenceReqIdDTO(referenceResDTO.getId());

        RefItemReqDTO refItemReqDTO = createRefItemReqDTO(referenceReqIdDTO, "code1", "name 1", "brief 1", "description 1");
        RefItemResDTO refItemResDTO = referenceItemService.create(refItemReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + refItemResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<RefItemResDTO> refItemsResDTO = referenceItemService.findByParams(
                QReferenceItemEntity.referenceItemEntity.reference.id.eq(refItemReqDTO.getReference().getId()).and(
                        QReferenceItemEntity.referenceItemEntity.code.equalsIgnoreCase(refItemReqDTO.getCode())
                )
        );

        assertThat(refItemsResDTO.size()).isEqualTo(0);

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующий элемент справочник")
    @Test
    public void deleteNotExistItemError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_REFERENCE_ITEM_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    private ReferenceReqIdDTO createReferenceReqIdDTO(Long id) {
        ReferenceReqIdDTO referenceReqIdDTO = new ReferenceReqIdDTO();
        referenceReqIdDTO.setId(id);
        return referenceReqIdDTO;
    }

    private RefGroupReqIdDTO createGroupReqIdDTO(Long id) {
        RefGroupReqIdDTO referenceReqIdDTO = new RefGroupReqIdDTO();
        referenceReqIdDTO.setId(id);
        return referenceReqIdDTO;
    }

    private RefItemReqDTO createRefItemReqDTO(ReferenceReqIdDTO reference, String code, String name, String brief, String description) {
        RefItemReqDTO refItemReqDTO = new RefItemReqDTO();
        refItemReqDTO.setReference(reference);
        refItemReqDTO.setCode(code);
        refItemReqDTO.setName(name);
        refItemReqDTO.setBrief(brief);
        refItemReqDTO.setDescription(description);
        return refItemReqDTO;
    }

    private ReferenceReqDTO createReferenceReqDTO(RefGroupReqIdDTO group, String sysname, String name, String description) {
        ReferenceReqDTO referenceReqDTO = new ReferenceReqDTO();
        referenceReqDTO.setGroup(group);
        referenceReqDTO.setSysname(sysname);
        referenceReqDTO.setName(name);
        referenceReqDTO.setDescription(description);
        return referenceReqDTO;
    }

    private RefGroupReqDTO createRefGroupReqDTO(Long parentId, String sysname, String name, String description) {
        RefGroupReqDTO refGroupReqDTO = new RefGroupReqDTO();
        refGroupReqDTO.setParentId(parentId);
        refGroupReqDTO.setSysname(sysname);
        refGroupReqDTO.setName(name);
        refGroupReqDTO.setDescription(description);
        return refGroupReqDTO;
    }

    private List<RefItemResDTO> createRefItems(List<ReferenceResDTO> referencesResDTO) {

        List<RefItemResDTO> refItemsResDTO = new ArrayList<>();

        Long referenceId_1 = referencesResDTO.get(0).getId();
        Long referenceId_2 = referencesResDTO.get(1).getId();
        Long referenceId_3 = referencesResDTO.get(2).getId();

        RefItemReqDTO refItemReqDTO_1 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_1), "code1", "name 1", "brief 1", "description 1");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_1));
        RefItemReqDTO refItemReqDTO_2 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_1), "code2", "name 2", "brief 2", "description 2");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_2));
        RefItemReqDTO refItemReqDTO_3 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_1), "code3", "name 3", "brief 3", "description 3");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_3));
        RefItemReqDTO refItemReqDTO_4 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_1), "code4", "name 4", "brief 4", "description 4");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_4));
        RefItemReqDTO refItemReqDTO_5 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_1), "code5", "name 5", "brief 5", "description 5");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_5));

        RefItemReqDTO refItemReqDTO_6 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_2), "code6", "name 6", "brief 6", "description 6");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_6));
        RefItemReqDTO refItemReqDTO_7 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_2), "code7", "name 7", "brief 7", "description 7");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_7));
        RefItemReqDTO refItemReqDTO_8 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_2), "code8", "name 8", "brief 8", "description 8");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_8));
        RefItemReqDTO refItemReqDTO_9 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_2), "code9", "name 9", "brief 9", "description 9");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_9));
        RefItemReqDTO refItemReqDTO_10 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_2), "code10", "name 10", "brief 10", "description 10");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_10));

        RefItemReqDTO refItemReqDTO_11 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_3), "code11", "name 11", "brief 11", "description 11");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_11));
        RefItemReqDTO refItemReqDTO_12 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_3), "code12", "name 12", "brief 12", "description 12");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_12));
        RefItemReqDTO refItemReqDTO_13 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_3), "code13", "name 13", "brief 13", "description 13");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_13));
        RefItemReqDTO refItemReqDTO_14 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_3), "code14", "name 14", "brief 14", "description 14");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_14));
        RefItemReqDTO refItemReqDTO_15 = createRefItemReqDTO(createReferenceReqIdDTO(referenceId_3), "code15", "name 15", "brief 15", "description 15");
        refItemsResDTO.add(referenceItemService.create(refItemReqDTO_15));

        return refItemsResDTO;
    }

    private List<ReferenceResDTO> createReferences(List<RefGroupResDTO> refGroupsResDTO) {

        List<ReferenceResDTO> referencesResDTO = new ArrayList<>();

        Long groupId = refGroupsResDTO.get(0).getId();

        ReferenceReqDTO referenceReqDTO_1 = createReferenceReqDTO(createGroupReqIdDTO(groupId), "refsysname1", "ref name 1", "description 1");
        referencesResDTO.add(referenceService.create(referenceReqDTO_1));
        ReferenceReqDTO referenceReqDTO_2 = createReferenceReqDTO(createGroupReqIdDTO(groupId), "refsysname2", "ref name 2", "description 2");
        referencesResDTO.add(referenceService.create(referenceReqDTO_2));
        ReferenceReqDTO referenceReqDTO_3 = createReferenceReqDTO(createGroupReqIdDTO(groupId), "refsysname3", "ref name 3", "description 3");
        referencesResDTO.add(referenceService.create(referenceReqDTO_3));

        return referencesResDTO;

    }

    private List<RefGroupResDTO> createRefGroups() {

        List<RefGroupResDTO> refGroupsResDTO = new ArrayList<>();

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO));

        return refGroupsResDTO;

    }

    private void deleteReferenceItems(List<RefItemResDTO> refItemsResDTO) {

        for(int i = refItemsResDTO.size() - 1; i >= 0; i--) {
            referenceItemService.delete(refItemsResDTO.get(i).getId());
        }

    }

    private void deleteReferences(List<ReferenceResDTO> referencesResDTO) {

        for(int i = referencesResDTO.size() - 1; i >= 0; i--) {
            referenceService.delete(referencesResDTO.get(i).getId());
        }

    }

    private void deleteRefGroups(List<RefGroupResDTO> refGroupsResDTO) {

        for(int i = refGroupsResDTO.size() - 1; i >= 0; i--) {
            referenceGroupService.delete(refGroupsResDTO.get(i).getId());
        }

    }

}
