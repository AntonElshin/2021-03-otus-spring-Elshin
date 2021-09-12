package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.VehicleBrandReqDTO;
import ru.otus.homework.dto.VehicleBrandResDTO;
import ru.otus.homework.entity.VehicleBrandEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleBrandMapper {

    VehicleBrandMapper INSTANCE = Mappers.getMapper(VehicleBrandMapper.class);

    VehicleBrandEntity fromDto(VehicleBrandReqDTO vehicleBrandReqDTO);

    VehicleBrandResDTO toDto(VehicleBrandEntity vehicleBrandEntity);
    List<VehicleBrandResDTO> toListDto(List<VehicleBrandEntity> vehicleBrandEntities);

}
