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
import ru.otus.homework.dto.RefGroupReqDTO;
import ru.otus.homework.dto.RefGroupResDTO;
import ru.otus.homework.entity.QReferenceGroupEntity;
import ru.otus.homework.service.ReferenceGroupService;

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
@DisplayName("Контроллер для групп справочников должен")
public class ReferenceGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReferenceGroupService referenceGroupService;

    private final String URL_PATH = "/reference-group/refgroup";
    private final Long NOT_EXISTS_REFERENCE_GROUP_ID = 1000L;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все группы справочников")
    @Test
    public void readAll() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();

        this.mockMvc.perform(get(URL_PATH + "s/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refgroupsysname2")))
                .andExpect(content().string(containsString("ref group name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refgroupsysname3")))
                .andExpect(content().string(containsString("ref group name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refgroupsysname4")))
                .andExpect(content().string(containsString("ref group name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refgroupsysname5")))
                .andExpect(content().string(containsString("ref group name 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("refgroupsysname6")))
                .andExpect(content().string(containsString("ref group name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refgroupsysname7")))
                .andExpect(content().string(containsString("ref group name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refgroupsysname8")))
                .andExpect(content().string(containsString("ref group name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refgroupsysname9")))
                .andExpect(content().string(containsString("ref group name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все группы по идентификатору родительской группы")
    @Test
    public void browseByParentId() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();

        Long parentGroupId_1 = refGroupsResDTO.get(0).getId();
        Long parentGroupId_2 = refGroupsResDTO.get(5).getId();
        Long parentGroupId_3 = refGroupsResDTO.get(10).getId();

        this.mockMvc.perform(get(URL_PATH + "s?parentId=0&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refgroupsysname6")))
                .andExpect(content().string(containsString("ref group name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?parentId="+ parentGroupId_1 +"&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname2")))
                .andExpect(content().string(containsString("ref group name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refgroupsysname3")))
                .andExpect(content().string(containsString("ref group name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refgroupsysname4")))
                .andExpect(content().string(containsString("ref group name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refgroupsysname5")))
                .andExpect(content().string(containsString("ref group name 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?parentId="+ parentGroupId_2 +"&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname7")))
                .andExpect(content().string(containsString("ref group name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refgroupsysname8")))
                .andExpect(content().string(containsString("ref group name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refgroupsysname9")))
                .andExpect(content().string(containsString("ref group name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?parentId="+ parentGroupId_3 +"&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        deleteRefGroups(refGroupsResDTO);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все группы по системному наименованию")
    @Test
    public void browseBySysName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=REFGROUPSYSNAME10&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все группы по наименованию")
    @Test
    public void browseByName() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();

        this.mockMvc.perform(get(URL_PATH + "s?name=ref group name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=ref group name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?name=REF GROUP NAME 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все группы по параметрам")
    @Test
    public void browseByParams() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();

        this.mockMvc.perform(get(URL_PATH + "s?parentId=0&sysname=refgroupsysname1&name=ref group name 1&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        deleteRefGroups(refGroupsResDTO);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить все группы по параметрам постраничная разбивка и сортировка")
    @Test
    public void browseByParamsPagingSorting() throws Exception {

        List<RefGroupResDTO> refGroupsResDTO = createRefGroups();

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,asc&size=15&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refgroupsysname2")))
                .andExpect(content().string(containsString("ref group name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refgroupsysname3")))
                .andExpect(content().string(containsString("ref group name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refgroupsysname4")))
                .andExpect(content().string(containsString("ref group name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refgroupsysname5")))
                .andExpect(content().string(containsString("ref group name 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("refgroupsysname6")))
                .andExpect(content().string(containsString("ref group name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refgroupsysname7")))
                .andExpect(content().string(containsString("ref group name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refgroupsysname8")))
                .andExpect(content().string(containsString("ref group name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refgroupsysname9")))
                .andExpect(content().string(containsString("ref group name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,asc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
                .andExpect(content().string(containsString("refgroupsysname2")))
                .andExpect(content().string(containsString("ref group name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refgroupsysname3")))
                .andExpect(content().string(containsString("ref group name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refgroupsysname4")))
                .andExpect(content().string(containsString("ref group name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refgroupsysname5")))
                .andExpect(content().string(containsString("ref group name 5")))
                .andExpect(content().string(containsString("description 5")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,asc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname6")))
                .andExpect(content().string(containsString("ref group name 6")))
                .andExpect(content().string(containsString("description 6")))
                .andExpect(content().string(containsString("refgroupsysname7")))
                .andExpect(content().string(containsString("ref group name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refgroupsysname8")))
                .andExpect(content().string(containsString("ref group name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refgroupsysname9")))
                .andExpect(content().string(containsString("ref group name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,asc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,desc&size=5&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname15")))
                .andExpect(content().string(containsString("ref group name 15")))
                .andExpect(content().string(containsString("description 15")))
                .andExpect(content().string(containsString("refgroupsysname14")))
                .andExpect(content().string(containsString("ref group name 14")))
                .andExpect(content().string(containsString("description 14")))
                .andExpect(content().string(containsString("refgroupsysname13")))
                .andExpect(content().string(containsString("ref group name 13")))
                .andExpect(content().string(containsString("description 13")))
                .andExpect(content().string(containsString("refgroupsysname12")))
                .andExpect(content().string(containsString("ref group name 12")))
                .andExpect(content().string(containsString("description 12")))
                .andExpect(content().string(containsString("refgroupsysname11")))
                .andExpect(content().string(containsString("ref group name 11")))
                .andExpect(content().string(containsString("description 11")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,desc&size=5&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname10")))
                .andExpect(content().string(containsString("ref group name 10")))
                .andExpect(content().string(containsString("description 10")))
                .andExpect(content().string(containsString("refgroupsysname9")))
                .andExpect(content().string(containsString("ref group name 9")))
                .andExpect(content().string(containsString("description 9")))
                .andExpect(content().string(containsString("refgroupsysname8")))
                .andExpect(content().string(containsString("ref group name 8")))
                .andExpect(content().string(containsString("description 8")))
                .andExpect(content().string(containsString("refgroupsysname7")))
                .andExpect(content().string(containsString("ref group name 7")))
                .andExpect(content().string(containsString("description 7")))
                .andExpect(content().string(containsString("refgroupsysname6")))
                .andExpect(content().string(containsString("ref group name 6")))
                .andExpect(content().string(containsString("description 6")))
        ;

        this.mockMvc.perform(get(URL_PATH + "s?sysname=refgroupsysname&sort=id,desc&size=5&page=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("refgroupsysname5")))
                .andExpect(content().string(containsString("ref group name 5")))
                .andExpect(content().string(containsString("description 5")))
                .andExpect(content().string(containsString("refgroupsysname4")))
                .andExpect(content().string(containsString("ref group name 4")))
                .andExpect(content().string(containsString("description 4")))
                .andExpect(content().string(containsString("refgroupsysname3")))
                .andExpect(content().string(containsString("ref group name 3")))
                .andExpect(content().string(containsString("description 3")))
                .andExpect(content().string(containsString("refgroupsysname2")))
                .andExpect(content().string(containsString("ref group name 2")))
                .andExpect(content().string(containsString("description 2")))
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

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

        this.mockMvc.perform(get(URL_PATH + "/" + + refGroupResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать группу справочников")
    @Test
    public void create() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(refGroupReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("refgroupsysname1")))
                .andExpect(content().string(containsString("ref group name 1")))
                .andExpect(content().string(containsString("description 1")))
        ;

        List<RefGroupResDTO> refGroupsResDTO = referenceGroupService.findByParams(QReferenceGroupEntity.referenceGroupEntity.sysname.equalsIgnoreCase(refGroupReqDTO.getSysname()));
        assertThat(refGroupsResDTO.size()).isEqualTo(1);
        assertThat(refGroupsResDTO.get(0).getSysname()).isEqualTo("refgroupsysname1");
        assertThat(refGroupsResDTO.get(0).getName()).isEqualTo("ref group name 1");
        assertThat(refGroupsResDTO.get(0).getDescription()).isEqualTo("description 1");

        if(refGroupsResDTO != null && refGroupsResDTO.size() == 1) {
            Long id = refGroupsResDTO.get(0).getId();
            referenceGroupService.delete(id);
        }

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать группу справочников из-за несуществующей родительской группы")
    @Test
    public void createInvalidParentIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(NOT_EXISTS_REFERENCE_GROUP_ID, "refgroupsysname1", "ref group name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(refGroupReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать группу справочников из-за неуникального системного наименования")
    @Test
    public void createNotUniqueSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        String json = new ObjectMapper().writeValueAsString(refGroupReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать группу справочников без родительской группы")
    @Test
    public void createWithoutParentIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(null, "refgroupsysname1", "ref group name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(refGroupReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать группу справочников без системного наименования")
    @Test
    public void createWithoutSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, null, "ref group name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(refGroupReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не создать группу справочников без наименования")
    @Test
    public void createWithoutNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", null, "description 1");

        String json = new ObjectMapper().writeValueAsString(refGroupReqDTO);

        mockMvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующую группу")
    @Test
    public void update() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname2", "ref group name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refGroupResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(refGroupResDTO.getId()))
                .andExpect(content().string(containsString("0")))
                .andExpect(content().string(containsString("refgroupsysname2")))
                .andExpect(content().string(containsString("ref group name 2")))
                .andExpect(content().string(containsString("description 2")))
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить несуществующую группу")
    @Test
    public void updateNotExistGroupId() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname2", "ref group name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + NOT_EXISTS_REFERENCE_GROUP_ID).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую группу из-за несуществующей родительской группы")
    @Test
    public void updateNotExistParentGroupId() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(NOT_EXISTS_REFERENCE_GROUP_ID, "refgroupsysname2", "ref group name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refGroupResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую группу из-за неуникального системного наименования")
    @Test
    public void updateNotUniqueSysName() throws Exception {

        RefGroupReqDTO refGroupReqDTO_1 = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO_1 = referenceGroupService.create(refGroupReqDTO_1);

        RefGroupReqDTO refGroupReqDTO_2 = createRefGroupReqDTO(0L, "refgroupsysname2", "ref group name 2", "description 2");
        RefGroupResDTO refGroupResDTO_2 = referenceGroupService.create(refGroupReqDTO_2);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refGroupResDTO_2.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

        referenceGroupService.delete(refGroupResDTO_1.getId());
        referenceGroupService.delete(refGroupResDTO_2.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую группу без родительской группы")
    @Test
    public void updateWithoutParentIdError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(null, "refgroupsysname2", "ref group name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refGroupResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую группу без системного наименования")
    @Test
    public void updateWithoutSysNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(0L, null, "ref group name 2", "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refGroupResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не обновить существующую группу без наименования")
    @Test
    public void updateWithoutNameError() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        RefGroupReqDTO updateRefGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", null, "description 2");

        String json = new ObjectMapper().writeValueAsString(updateRefGroupReqDTO);

        this.mockMvc.perform(put(URL_PATH + "/" + refGroupResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        referenceGroupService.delete(refGroupResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующую группу справочников")
    @Test
    public void delete() throws Exception {

        RefGroupReqDTO refGroupReqDTO = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO refGroupResDTO = referenceGroupService.create(refGroupReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + refGroupResDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        List<RefGroupResDTO> refGroupsResDTO = referenceGroupService.findByParams(QReferenceGroupEntity.referenceGroupEntity.sysname.equalsIgnoreCase(refGroupReqDTO.getSysname()));

        assertThat(refGroupsResDTO.size()).isEqualTo(0);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("не удалить несуществующую группу справочников")
    @Test
    public void deleteNotExistGroupError() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/" + NOT_EXISTS_REFERENCE_GROUP_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    private RefGroupReqDTO createRefGroupReqDTO(Long parentId, String sysname, String name, String description) {
        RefGroupReqDTO refGroupReqDTO = new RefGroupReqDTO();
        refGroupReqDTO.setParentId(parentId);
        refGroupReqDTO.setSysname(sysname);
        refGroupReqDTO.setName(name);
        refGroupReqDTO.setDescription(description);
        return refGroupReqDTO;
    }

    private List<RefGroupResDTO> createRefGroups() {

        List<RefGroupResDTO> refGroupsResDTO = new ArrayList<>();

        RefGroupReqDTO refGroupReqDTO_1 = createRefGroupReqDTO(0L, "refgroupsysname1", "ref group name 1", "description 1");
        RefGroupResDTO parentGroup_1 = referenceGroupService.create(refGroupReqDTO_1);
        refGroupsResDTO.add(parentGroup_1);

        RefGroupReqDTO refGroupReqDTO_2 = createRefGroupReqDTO(parentGroup_1.getId(), "refgroupsysname2","ref group name 2", "description 2");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_2));
        RefGroupReqDTO refGroupReqDTO_3 = createRefGroupReqDTO(parentGroup_1.getId(), "refgroupsysname3","ref group name 3", "description 3");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_3));
        RefGroupReqDTO refGroupReqDTO_4 = createRefGroupReqDTO(parentGroup_1.getId(), "refgroupsysname4","ref group name 4",  "description 4");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_4));
        RefGroupReqDTO refGroupReqDTO_5 = createRefGroupReqDTO(parentGroup_1.getId(), "refgroupsysname5","ref group name 5",  "description 5");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_5));

        RefGroupReqDTO refGroupReqDTO_6 = createRefGroupReqDTO(0L, "refgroupsysname6","ref group name 6","description 6");
        RefGroupResDTO parentGroup_2 = referenceGroupService.create(refGroupReqDTO_6);
        refGroupsResDTO.add(parentGroup_2);

        RefGroupReqDTO refGroupReqDTO_7 = createRefGroupReqDTO(parentGroup_2.getId(), "refgroupsysname7", "ref group name 7", "description 7");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_7));
        RefGroupReqDTO refGroupReqDTO_8 = createRefGroupReqDTO(parentGroup_2.getId(), "refgroupsysname8", "ref group name 8", "description 8");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_8));
        RefGroupReqDTO refGroupReqDTO_9 = createRefGroupReqDTO(parentGroup_2.getId(), "refgroupsysname9", "ref group name 9", "description 9");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_9));
        RefGroupReqDTO refGroupReqDTO_10 = createRefGroupReqDTO(parentGroup_2.getId(), "refgroupsysname10", "ref group name 10", "description 10");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_10));

        RefGroupReqDTO refGroupReqDTO_11 = createRefGroupReqDTO(0L, "refgroupsysname11", "ref group name 11", "description 11");
        RefGroupResDTO parentGroup_3 = referenceGroupService.create(refGroupReqDTO_11);
        refGroupsResDTO.add(parentGroup_3);

        RefGroupReqDTO refGroupReqDTO_12 = createRefGroupReqDTO(parentGroup_3.getId(), "refgroupsysname12", "ref group name 12", "description 12");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_12));
        RefGroupReqDTO refGroupReqDTO_13 = createRefGroupReqDTO(parentGroup_3.getId(), "refgroupsysname13", "ref group name 13", "description 13");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_13));
        RefGroupReqDTO refGroupReqDTO_14 = createRefGroupReqDTO(parentGroup_3.getId(), "refgroupsysname14", "ref group name 14", "description 14");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_14));
        RefGroupReqDTO refGroupReqDTO_15 = createRefGroupReqDTO(parentGroup_3.getId(), "refgroupsysname15", "ref group name 15", "description 15");
        refGroupsResDTO.add(referenceGroupService.create(refGroupReqDTO_15));

        return refGroupsResDTO;

    }

    private void deleteRefGroups(List<RefGroupResDTO> refGroupsResDTO) {

        for(int i = refGroupsResDTO.size() - 1; i >= 0; i--) {
            referenceGroupService.delete(refGroupsResDTO.get(i).getId());
        }

    }

}
