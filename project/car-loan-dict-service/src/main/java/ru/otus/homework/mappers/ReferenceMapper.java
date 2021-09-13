package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.ReferenceReqDTO;
import ru.otus.homework.dto.ReferenceResDTO;
import ru.otus.homework.entity.ReferenceEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReferenceMapper {

    ReferenceMapper INSTANCE = Mappers.getMapper(ReferenceMapper.class);

    ReferenceEntity fromDto(ReferenceReqDTO referenceReqDTO);

    ReferenceResDTO toDto(ReferenceEntity referenceEntity);
    List<ReferenceResDTO> toListDto(List<ReferenceEntity> referenceEntities);

}
