package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.VehicleSetReqDTO;
import ru.otus.homework.dto.VehicleSetResDTO;
import ru.otus.homework.entity.VehicleSetEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleSetMapper {

    VehicleSetMapper INSTANCE = Mappers.getMapper(VehicleSetMapper.class);

    VehicleSetEntity fromDto(VehicleSetReqDTO vehicleSetReqDTO);

    VehicleSetResDTO toDto(VehicleSetEntity vehicleSetEntity);
    List<VehicleSetResDTO> toListDto(List<VehicleSetEntity> vehicleSetEntities);

}
