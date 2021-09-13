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
import ru.otus.homework.entity.QReferenceEntity;
import ru.otus.homework.service.ReferenceGroupService;
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
@DisplayName("Контроллер для справочников должен")
public class ReferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReferenceGroupService referenceGroupService;

    @Autowired
    private ReferenceService referenceService;

    private final String URL_PATH = "/reference/reference";
    private final Long NOT_EXISTS_REFERENCE_GROUP_ID = 1000L;
    private final Long NOT_EXISTS_REFERENCE_ID = 1000L;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все справочники по идентификатору группы")
    @Test
    public void readAll() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);

        Long groupId_1 = refGroupsResDTO.get(0).getId();
        Long groupId_2 = refGroupsResDTO.get(1).getId();
        Long groupId_3 = refGroupsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s/all?groupId=" + groupId_1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refsysname2")))
                .andExpect(content().string(containsString("ref name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refsysname3")))
                .andExpect(content().string(containsString("ref name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refsysname4")))
                .andExpect(content().string(containsString("ref name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refsysname5")))
                .andExpect(content().string(containsString("ref name 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?groupId=" + groupId_2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname6")))
                .andExpect(content().string(containsString("ref name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refsysname7")))
                .andExpect(content().string(containsString("ref name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refsysname8")))
                .andExpect(content().string(containsString("ref name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refsysname9")))
                .andExpect(content().string(containsString("ref name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s/all?groupId=" + groupId_3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не получить все справочники без идентификатора группы")
    @Test
    public void readAllError() throws Exception {

        this.mockMvc.perform(get(URL_PATH + "s/all"))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все справочники по идентификатору группы")
    @Test
    public void browseByParentId() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);

        Long groupId_1 = refGroupsResDTO.get(0).getId();
        Long groupId_2 = refGroupsResDTO.get(1).getId();
        Long groupId_3 = refGroupsResDTO.get(2).getId();

        this.mockMvc.perform(get(URL_PATH + "s?group.id="+ groupId_1 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refsysname2")))
                .andExpect(content().string(containsString("ref name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refsysname3")))
                .andExpect(content().string(containsString("ref name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refsysname4")))
                .andExpect(content().string(containsString("ref name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refsysname5")))
                .andExpect(content().string(containsString("ref name 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?group.id="+ groupId_2 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname6")))
                .andExpect(content().string(containsString("ref name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refsysname7")))
                .andExpect(content().string(containsString("ref name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refsysname8")))
                .andExpect(content().string(containsString("ref name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refsysname9")))
                .andExpect(content().string(containsString("ref name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?group.id="+ groupId_3 + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все справочники по системному наименованию")
    @Test
    public void browseBySysName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=REFSYSNAME10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все справочники по наименованию")
    @Test
    public void browseByName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?name=ref name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=ref name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=REF NAME 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все справочники по параметрам")
    @Test
    public void browseByParams() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);

        Long groupId_1 = refGroupsResDTO.get(0).getId();

        this.mockMvc.perform(get(URL_PATH + "s?group.id=" + groupId_1 + " &sysname=refsysname1&name=ref name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все справочники по параметрам постраничная разбивка и сортировка")
    @Test
    public void browseByParamsPagingSorting() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();
        List<ReferenceResDTO> referencesResDTO = createReferences(refGroupsResDTO);

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,asc&size=15&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refsysname2")))
                .andExpect(content().string(containsString("ref name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refsysname3")))
                .andExpect(content().string(containsString("ref name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refsysname4")))
                .andExpect(content().string(containsString("ref name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refsysname5")))
                .andExpect(content().string(containsString("ref name 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("refsysname6")))
                .andExpect(content().string(containsString("ref name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refsysname7")))
                .andExpect(content().string(containsString("ref name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refsysname8")))
                .andExpect(content().string(containsString("ref name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refsysname9")))
                .andExpect(content().string(containsString("ref name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,asc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refsysname2")))
                .andExpect(content().string(containsString("ref name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refsysname3")))
                .andExpect(content().string(containsString("ref name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refsysname4")))
                .andExpect(content().string(containsString("ref name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refsysname5")))
                .andExpect(content().string(containsString("ref name 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,asc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname6")))
                .andExpect(content().string(containsString("ref name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refsysname7")))
                .andExpect(content().string(containsString("ref name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refsysname8")))
                .andExpect(content().string(containsString("ref name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refsysname9")))
                .andExpect(content().string(containsString("ref name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,asc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,desc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname15")))
                .andExpect(content().string(containsString("ref name 15")))
                .andExpect(content().string(containsString("description 15")))
                .andExpect(content().string(containsString("refsysname14")))
                .andExpect(content().string(containsString("ref name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refsysname13")))
                .andExpect(content().string(containsString("ref name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refsysname12")))
                .andExpect(content().string(containsString("ref name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refsysname11")))
                .andExpect(content().string(containsString("ref name 11")))
                .andExpect(content().string(containsString("description 11")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,desc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname10")))
                .andExpect(content().string(containsString("ref name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refsysname9")))
                .andExpect(content().string(containsString("ref name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refsysname8")))
                .andExpect(content().string(containsString("ref name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refsysname7")))
                .andExpect(content().string(containsString("ref name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refsysname6")))
                .andExpect(content().string(containsString("ref name 6")))
                .andExpect(content().string(containsString("description 6")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refsysname&sort=id,desc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refsysname5")))
                .andExpect(content().string(containsString("ref name 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("refsysname4")))
                .andExpect(content().string(containsString("ref name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refsysname3")))
                .andExpect(content().string(containsString("ref name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refsysname2")))
                .andExpect(content().string(containsString("ref name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        deleteReferences(referencesResDTO);
        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить группу справочника по идентификатору")
    @Test
    public void read() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        this.mockMvc.perform(get(URL_PATH + "/" + referenceResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать справочник")
    @Test
    public void create() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("refsysname1")))
                .andExpect(content().string(containsString("ref name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        List<ReferenceResDTO> referencesResDTO = referenceService.findByParams(QReferenceEntity.referenceEntity.sysname.equalsIgnoreCase(referenceReqDTO.getSysname()));
        assertThat(referencesResDTO.size()).isEqualTo(1);
        assertThat(referencesResDTO.get(0).getSysname()).isEqualTo("refsysname1");
        assertThat(referencesResDTO.get(0).getName()).isEqualTo("ref name 1");
        assertThat(referencesResDTO.get(0).getDescription()).isEqualTo("description 1");

        if(referencesResDTO != null && referencesResDTO.size() == 1) {
            Long id = referencesResDTO.get(0).getId();
            referenceService.delete(id);
        }

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать справочник из-за несуществующей группы")
    @Test
    public void createInvalidGroupIdError() throws Exception {

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(NOT_EXISTS_REFERENCE_GROUP_ID), "refgroupsysname1", "ref group name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать справочник из-за неуникального системного наименования")
    @Test
    public void createNotUniqueSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

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
    @DisplayName("не создать справочник без группы")
    @Test
    public void createWithoutGroupIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(null, "refsysname1", "ref name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать справочник без системного наименования")
    @Test
    public void createWithoutSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, null, "ref name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать справочник без наименования")
    @Test
    public void createWithoutNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqIdDTO refGroupReqIdDTO = createGroupReqIdDTO(refGroupResDTO.getId());

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(refGroupReqIdDTO, "refsysname1", null, "description 1");

        String json = new ObjectMapper().writeValueAsString(referenceReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующий справочник")
    @Test
    public void update() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname2", "ref name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + referenceResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(referenceResDTO.getId()))
                .andExpect(content().string(containsString("" + refGroupResDTO.getId())))
                .andExpect(content().string(containsString("refsysname2")))
                .andExpect(content().string(containsString("ref name 2")))
                .andExpect(content().string(containsString("description 2")))
        ;

        referenceService.delete(referenceResDTO.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить несуществующий справочник")
    @Test
    public void updateNotExistReferenceId() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname2", "ref name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + NOT_EXISTS_REFERENCE_GROUP_ID).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("не обновить существующий справочник из-за несуществующей группы")
    @Test
    public void updateNotExistGroupId() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(NOT_EXISTS_REFERENCE_GROUP_ID), "refsysname2", "ref name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + referenceResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("не обновить существующий справочник из-за неуникального системного наименования")
    @Test
    public void updateNotUniqueSysName() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO_1 = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO_1 = referenceService.create(referenceReqDTO_1);

        ReferenceReqDTO referenceReqDTO_2 = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname2", "ref name 2", "description 2");
        ReferenceResDTO referenceResDTO_2 = referenceService.create(referenceReqDTO_2);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + referenceResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceService.delete(referenceResDTO_1.getId());
        referenceService.delete(referenceResDTO_2.getId());
        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить справочник без группы")
    @Test
    public void updateWithoutGroupIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(null, "refsysname2", "ref name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + referenceResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("не обновить справочник без системного наименования")
    @Test
    public void updateWithoutSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), null, "ref name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + referenceResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("не обновить справочник без наименования")
    @Test
    public void updateWithoutNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        ReferenceReqDTO updateReferenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname2", null, "description 2");

        String json = new ObjectMapper().writeValueAsString(updateReferenceReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + referenceResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
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
    @DisplayName("удалить существующий справочник")
    @Test
    public void delete() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        ReferenceReqDTO referenceReqDTO = createReferenceReqDTO(createGroupReqIdDTO(refGroupResDTO.getId()), "refsysname1", "ref name 1", "description 1");
        ReferenceResDTO referenceResDTO = referenceService.create(referenceReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + referenceResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<ReferenceResDTO> referencesResDTO = referenceService.findByParams(QReferenceEntity.referenceEntity.sysname.equalsIgnoreCase(referenceResDTO.getSysname()));

        assertThat(referencesResDTO.size()).isEqualTo(0);

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующий справочник")
    @Test
    public void deleteNotExistReferenceError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_REFERENCE_GROUP_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    private RefGroupReqIdDTO createGroupReqIdDTO(Long id) {
        RefGroupReqIdDTO referenceReqIdDTO = new RefGroupReqIdDTO();
        referenceReqIdDTO.setId(id);
        return referenceReqIdDTO;
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

    private List<ReferenceResDTO> createReferences(List<RefGroupResDTO> refGroupsResDTO) {

        List<ReferenceResDTO> referencesResDTO = new ArrayList<>();

        Long groupId_1 = refGroupsResDTO.get(0).getId();
        Long groupId_2 = refGroupsResDTO.get(1).getId();
        Long groupId_3 = refGroupsResDTO.get(2).getId();

        ReferenceReqDTO referenceReqDTO_1 = createReferenceReqDTO(createGroupReqIdDTO(groupId_1), "refsysname1", "ref name 1", "description 1");
        referencesResDTO.add(referenceService.create(referenceReqDTO_1));
        ReferenceReqDTO referenceReqDTO_2 = createReferenceReqDTO(createGroupReqIdDTO(groupId_1), "refsysname2", "ref name 2", "description 2");
        referencesResDTO.add(referenceService.create(referenceReqDTO_2));
        ReferenceReqDTO referenceReqDTO_3 = createReferenceReqDTO(createGroupReqIdDTO(groupId_1), "refsysname3", "ref name 3", "description 3");
        referencesResDTO.add(referenceService.create(referenceReqDTO_3));
        ReferenceReqDTO referenceReqDTO_4 = createReferenceReqDTO(createGroupReqIdDTO(groupId_1), "refsysname4", "ref name 4", "description 4");
        referencesResDTO.add(referenceService.create(referenceReqDTO_4));
        ReferenceReqDTO referenceReqDTO_5 = createReferenceReqDTO(createGroupReqIdDTO(groupId_1), "refsysname5", "ref name 5", "description 5");
        referencesResDTO.add(referenceService.create(referenceReqDTO_5));

        ReferenceReqDTO referenceReqDTO_6 = createReferenceReqDTO(createGroupReqIdDTO(groupId_2), "refsysname6", "ref name 6", "description 6");
        referencesResDTO.add(referenceService.create(referenceReqDTO_6));
        ReferenceReqDTO referenceReqDTO_7 = createReferenceReqDTO(createGroupReqIdDTO(groupId_2), "refsysname7", "ref name 7", "description 7");
        referencesResDTO.add(referenceService.create(referenceReqDTO_7));
        ReferenceReqDTO referenceReqDTO_8 = createReferenceReqDTO(createGroupReqIdDTO(groupId_2), "refsysname8", "ref name 8", "description 8");
        referencesResDTO.add(referenceService.create(referenceReqDTO_8));
        ReferenceReqDTO referenceReqDTO_9 = createReferenceReqDTO(createGroupReqIdDTO(groupId_2), "refsysname9", "ref name 9", "description 9");
        referencesResDTO.add(referenceService.create(referenceReqDTO_9));
        ReferenceReqDTO referenceReqDTO_10 = createReferenceReqDTO(createGroupReqIdDTO(groupId_2), "refsysname10", "ref name 10", "description 10");
        referencesResDTO.add(referenceService.create(referenceReqDTO_10));

        ReferenceReqDTO referenceReqDTO_11 = createReferenceReqDTO(createGroupReqIdDTO(groupId_3), "refsysname11", "ref name 11", "description 11");
        referencesResDTO.add(referenceService.create(referenceReqDTO_11));
        ReferenceReqDTO referenceReqDTO_12 = createReferenceReqDTO(createGroupReqIdDTO(groupId_3), "refsysname12", "ref name 12", "description 12");
        referencesResDTO.add(referenceService.create(referenceReqDTO_12));
        ReferenceReqDTO referenceReqDTO_13 = createReferenceReqDTO(createGroupReqIdDTO(groupId_3), "refsysname13", "ref name 13", "description 13");
        referencesResDTO.add(referenceService.create(referenceReqDTO_13));
        ReferenceReqDTO referenceReqDTO_14 = createReferenceReqDTO(createGroupReqIdDTO(groupId_3), "refsysname14", "ref name 14", "description 14");
        referencesResDTO.add(referenceService.create(referenceReqDTO_14));
        ReferenceReqDTO referenceReqDTO_15 = createReferenceReqDTO(createGroupReqIdDTO(groupId_3), "refsysname15", "ref name 15", "description 15");
        referencesResDTO.add(referenceService.create(referenceReqDTO_15));

        return referencesResDTO;

    }

    private List<RefGroupResDTO> createRefGroups() {

        List<RefGroupResDTO> refGroupsResDTO = new ArrayList<>();

        RefGroupReqDTO refGroupReqDTO_1 = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO parentGroup_1 = referenceGroupService.create(refGroupReqDTO_1);
        refGroupsResDTO.add(parentGroup_1);

        RefGroupReqDTO refGroupReqDTO_2 = createRefGroupReqDTO(0L, "refgroupsysname2","ref group name 2","description 2");
        RefGroupResDTO parentGroup_2 = referenceGroupService.create(refGroupReqDTO_2);
        refGroupsResDTO.add(parentGroup_2);

        RefGroupReqDTO refGroupReqDTO_3 = createRefGroupReqDTO(0L, "refgroupsysname3", "ref group name 3", "description 3");
        RefGroupResDTO parentGroup_3 = referenceGroupService.create(refGroupReqDTO_3);
        refGroupsResDTO.add(parentGroup_3);

        return refGroupsResDTO;

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
