package ru.otus.homework.parsers;

import ru.otus.homework.entity.FileUploadHistoryEntity;
import ru.otus.homework.entity.VehicleModelEntity;
import ru.otus.homework.entity.VehicleSetEntity;

import java.util.List;

public interface Parser {

    List<VehicleSetEntity> getPassengerUsedVehicleSets(List<String> fileStrings, FileUploadHistoryEntity fileUploadHistoryEntity);

}

