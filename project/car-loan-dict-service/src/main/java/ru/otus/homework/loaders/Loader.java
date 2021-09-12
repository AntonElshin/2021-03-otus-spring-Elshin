package ru.otus.homework.loaders;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Loader {

    List<String> loadVehiclePassangerUsedSets(MultipartFile file);

}
