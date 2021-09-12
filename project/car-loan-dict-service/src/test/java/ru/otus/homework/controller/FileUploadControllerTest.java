package ru.otus.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.dto.VehicleBrandResDTO;
import ru.otus.homework.dto.VehicleModelResDTO;
import ru.otus.homework.entity.FileUploadHistoryEntity;
import ru.otus.homework.entity.QVehicleSetYearEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.repository.FileUploadHistoryRepository;
import ru.otus.homework.service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллер для истории загрузки файлов")
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUploadHistoryRepository fileUploadHistoryRepository;

    @Autowired
    private ReferenceItemService referenceItemService;

    private ReferenceItemEntity uploadFileType_PassengerUsed = null;
    private ReferenceItemEntity uploadFileType_PassengerNew = null;

    private static final String FILE_UPLOAD_STATUS_DONE = "DONE";

    @BeforeEach
    public void setUp() {

        uploadFileType_PassengerUsed = referenceItemService.findByReferenceSysNameAndItemCode("uploadFileType", "PassengerUsed");
        uploadFileType_PassengerNew = referenceItemService.findByReferenceSysNameAndItemCode("uploadFileType", "PassengerNew");

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить всю историю загрузки файлов по типу файла")
    @Test
    public void browseByFileType() throws Exception {

        createFileUploadHistories();

        this.mockMvc.perform(get("/file-upload/history?fileTypeId="+ uploadFileType_PassengerUsed.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(uploadFileType_PassengerUsed.getId().toString())))
                .andExpect(content().string(containsString("admin 1")))
                .andExpect(content().string(containsString("file name 1")))
        ;

        this.mockMvc.perform(get("/file-upload/history?fileTypeId="+ uploadFileType_PassengerNew.getId() + "&sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(uploadFileType_PassengerNew.getId().toString())))
                .andExpect(content().string(containsString("admin 2")))
                .andExpect(content().string(containsString("file name 2")))
        ;

        fileUploadHistoryRepository.deleteAll();

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("(browse) получить всю историю загрузки файлов")
    @Test
    public void browseByVehicleBrandId() throws Exception {

        createFileUploadHistories();

        this.mockMvc.perform(get("/file-upload/history?sort=id,asc&size=10&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(uploadFileType_PassengerUsed.getId().toString())))
                .andExpect(content().string(containsString(uploadFileType_PassengerNew.getId().toString())))
                .andExpect(content().string(containsString("admin 1")))
                .andExpect(content().string(containsString("file name 1")))
                .andExpect(content().string(containsString("admin 2")))
                .andExpect(content().string(containsString("file name 2")))
        ;

        fileUploadHistoryRepository.deleteAll();

    }

    private void createFileUploadHistories() {

        List<FileUploadHistoryEntity> fileUploadHistoryEntities = new ArrayList<>();

        FileUploadHistoryEntity fileUploadHistoryEntity_1 = new FileUploadHistoryEntity(
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                FILE_UPLOAD_STATUS_DONE,
                "admin 1",
                1,
                uploadFileType_PassengerUsed,
                "file name 1",
                "file content 1",
                null
        );
        fileUploadHistoryEntities.add(fileUploadHistoryEntity_1);

        FileUploadHistoryEntity fileUploadHistoryEntity_2 = new FileUploadHistoryEntity(
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                FILE_UPLOAD_STATUS_DONE,
                "admin 2",
                1,
                uploadFileType_PassengerNew,
                "file name 2",
                "file content 2",
                null
        );
        fileUploadHistoryEntities.add(fileUploadHistoryEntity_2);

        fileUploadHistoryRepository.saveAll(fileUploadHistoryEntities);

    }

}
