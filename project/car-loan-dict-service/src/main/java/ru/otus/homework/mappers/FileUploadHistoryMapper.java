package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.FileUploadHistoryResDTO;
import ru.otus.homework.entity.FileUploadHistoryEntity;

@Mapper(componentModel = "spring")
public interface FileUploadHistoryMapper {

    FileUploadHistoryMapper INSTANCE = Mappers.getMapper(FileUploadHistoryMapper.class);

    FileUploadHistoryResDTO toDto(FileUploadHistoryEntity fileUploadHistoryEntity);
    FileUploadHistoryEntity fromDto(FileUploadHistoryResDTO fileUploadHistoryResDTO);

}
