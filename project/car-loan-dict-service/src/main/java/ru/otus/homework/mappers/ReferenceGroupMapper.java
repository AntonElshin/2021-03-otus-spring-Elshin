package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.RefGroupReqDTO;
import ru.otus.homework.dto.RefGroupResDTO;
import ru.otus.homework.entity.ReferenceGroupEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReferenceGroupMapper {

    ReferenceGroupMapper INSTANCE = Mappers.getMapper(ReferenceGroupMapper.class);

    ReferenceGroupEntity fromDto(RefGroupReqDTO refGroupReqDTO);

    RefGroupResDTO toDto(ReferenceGroupEntity referenceGroupEntity);
    List<RefGroupResDTO> toListDto(List<ReferenceGroupEntity> referenceGroupEntities);

}
