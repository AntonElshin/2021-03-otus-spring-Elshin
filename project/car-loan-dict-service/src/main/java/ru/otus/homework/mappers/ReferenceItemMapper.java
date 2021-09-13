package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.RefItemReqDTO;
import ru.otus.homework.dto.RefItemResDTO;
import ru.otus.homework.entity.ReferenceItemEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReferenceItemMapper {

    ReferenceItemMapper INSTANCE = Mappers.getMapper(ReferenceItemMapper.class);

    ReferenceItemEntity fromDto(RefItemReqDTO refItemReqDTO);

    RefItemResDTO toDto(ReferenceItemEntity referenceItemEntity);
    List<RefItemResDTO> toListDto(List<ReferenceItemEntity> items);

}
