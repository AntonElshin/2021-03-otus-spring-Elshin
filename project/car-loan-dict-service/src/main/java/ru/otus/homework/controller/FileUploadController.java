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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.homework.api.FileUploadApi;
import ru.otus.homework.entity.FileUploadHistoryEntity;
import ru.otus.homework.service.FileUploadService;
import ru.otus.homework.dto.FileUploadHistoryResDTO;
import ru.otus.homework.dto.FileUploadHistoryResIdDTO;

@RequiredArgsConstructor
@RestController
public class FileUploadController implements FileUploadApi {

    private final FileUploadService fileUploadService;

    @ApiOperation(value = "Получение списка историй загрузки файлов")
    @GetMapping(value = "/file-upload/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<FileUploadHistoryResDTO> getFileUploadHistoriesByParams(
            @QuerydslPredicate(root = FileUploadHistoryEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return fileUploadService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<FileUploadHistoryResIdDTO> uploadPassengerUsedVehicleModelsAndSets(MultipartFile file) {
        return ResponseEntity.ok(fileUploadService.uploadPassengerUsedVehicleModelsAndSets(file));
    }
    

}
