package ru.otus.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.dto.VehicleSetYearReqDTO;
import ru.otus.homework.dto.VehicleSetYearResDTO;
import ru.otus.homework.entity.VehicleSetYearEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleSetYearMapper {

    VehicleSetYearMapper INSTANCE = Mappers.getMapper(VehicleSetYearMapper.class);

    VehicleSetYearEntity fromDto(VehicleSetYearReqDTO vehicleSetYearReqDTO);

    VehicleSetYearResDTO toDto(VehicleSetYearEntity vehicleSetYearEntity);
    List<VehicleSetYearResDTO> toListDto(List<VehicleSetYearEntity> vehicleSetYearEntities);
}
