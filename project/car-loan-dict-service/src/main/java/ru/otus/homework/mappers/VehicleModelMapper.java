package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.VehicleModelReqDTO;
import ru.otus.homework.dto.VehicleModelResDTO;
import ru.otus.homework.entity.VehicleModelEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleModelMapper {

    VehicleModelMapper INSTANCE = Mappers.getMapper(VehicleModelMapper.class);

    VehicleModelEntity fromDto(VehicleModelReqDTO vehicleModelReqDTO);

    VehicleModelResDTO toDto(VehicleModelEntity vehicleModelEntity);
    List<VehicleModelResDTO> toListDto(List<VehicleModelEntity> vehicleModelEntities);

}
