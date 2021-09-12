package ru.otus.homework.parsers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.entity.*;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.repository.FileUploadHistoryRepository;
import ru.otus.homework.repository.ReferenceItemRepository;
import ru.otus.homework.repository.VehicleBrandRepository;
import ru.otus.homework.repository.VehicleModelRepository;
import ru.otus.homework.service.MessageService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParserCSV implements Parser {

    @Value("${upload-vehicle-set-file.load-with-errors}")
    private String loadWithErrors;

    private final VehicleBrandRepository vehicleBrandRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final ReferenceItemRepository referenceItemRepository;
    private final FileUploadHistoryRepository fileUploadHistoryRepository;
    private final MessageService messageService;

    private static final int VEHICLE_KIND_ROW_NUMBER = 2;
    private static final int ORDER_NUMBER_ROW_NUMBER = 3;
    private static final int BRAND_NAME_ROW_NUMBER = 4;
    private static final int MODEL_NAME_ROW_NUMBER = 5;
    private static final int BODY_TYPE_ROW_NUMBER = 6;
    private static final int ENGINE_CAPACITY_ROW_NUMBER = 7;
    private static final int ENGINE_POWER_ROW_NUMBER = 8;
    private static final int ENGINE_TYPE_ROW_NUMBER = 9;
    private static final int TRANSMISSION_ROW_NUMBER = 10;
    private static final String FILE_UPLOAD_STATUS_ERROR = "ERROR";

    @Override
    public List<VehicleSetEntity> getPassengerUsedVehicleSets(List<String> fileStrings, FileUploadHistoryEntity fileUploadHistoryEntity) {

        List<VehicleBrandEntity> brands = vehicleBrandRepository.findAll();
        List<VehicleModelEntity> models = vehicleModelRepository.findAll();

        List<ReferenceItemEntity> vehicleKinds = referenceItemRepository.findByReferenceSysNameEquals("vehicleKind");
        List<ReferenceItemEntity> bodyTypes = referenceItemRepository.findByReferenceSysNameEquals("bodyType");
        List<ReferenceItemEntity> engineCapacities = referenceItemRepository.findByReferenceSysNameEquals("engineCapacity");
        List<ReferenceItemEntity> engineTypes = referenceItemRepository.findByReferenceSysNameEquals("engineType");
        List<ReferenceItemEntity> enginePowers = referenceItemRepository.findByReferenceSysNameEquals("enginePower");
        List<ReferenceItemEntity> transmissions = referenceItemRepository.findByReferenceSysNameEquals("transmission");
        List<ReferenceItemEntity> manufactureYears = referenceItemRepository.findByReferenceSysNameEquals("yearOfManufacture");

        List<String> errors = new ArrayList<>();
        List<VehicleSetEntity> vehicleSetEntities = new ArrayList<>();

        for(String fileString : fileStrings) {

            if(fileString != null) {

                String vehicleKindCode = "";
                ReferenceItemEntity vehicleKind = null;
                String orderNumber = null;
                String brandName = "";
                Long brandId = null;
                String modelName = "";
                VehicleModelEntity model = null;
                String bodyTypeCode = "";
                ReferenceItemEntity bodyType = null;
                String engineCapacityCode = "";
                ReferenceItemEntity engineCapacity = null;
                String enginePowerCode = "";
                ReferenceItemEntity enginePower = null;
                String engineTypeCode = "";
                ReferenceItemEntity engineType = null;
                String transmissionCode = "";
                ReferenceItemEntity transmission = null;
                List<VehicleSetYearEntity> years = new ArrayList<>();
                String yearCode = "";
                ReferenceItemEntity year = null;
                BigDecimal price = null;
                List<String> yearErrors = new ArrayList<>();

                String[] vehicleSetStrings = fileString.split(";");

                int count = 1;

                for(String vehicleSetString : vehicleSetStrings) {

                    if(count == VEHICLE_KIND_ROW_NUMBER) {
                        vehicleKindCode = vehicleSetString;
                        vehicleKind = findReferenceItemByCode(vehicleKinds, vehicleKindCode);
                    }
                    if(count == ORDER_NUMBER_ROW_NUMBER) {
                        orderNumber = vehicleSetString;
                    }
                    if(count == BRAND_NAME_ROW_NUMBER) {
                        brandName = vehicleSetString;
                        brandId = findVehicleBrandByName(brands, brandName);
                    }
                    if(count == MODEL_NAME_ROW_NUMBER) {
                        modelName = vehicleSetString;
                        model = findVehicleModelByName(brandId, vehicleKind.getId(), models, vehicleSetString);
                    }
                    if(count == BODY_TYPE_ROW_NUMBER) {
                        bodyTypeCode = vehicleSetString;
                        bodyType = findReferenceItemByCode(bodyTypes, bodyTypeCode);
                    }
                    if(count == ENGINE_CAPACITY_ROW_NUMBER) {
                        engineCapacityCode = vehicleSetString;
                        engineCapacity = findReferenceItemByCode(engineCapacities, engineCapacityCode);
                    }
                    if(count == ENGINE_POWER_ROW_NUMBER) {
                        enginePowerCode = vehicleSetString;
                        enginePower = findReferenceItemByCode(enginePowers, enginePowerCode);
                    }
                    if(count == ENGINE_TYPE_ROW_NUMBER) {
                        engineTypeCode = vehicleSetString;
                        engineType = findReferenceItemByCode(engineTypes, engineTypeCode);
                    }
                    if(count == TRANSMISSION_ROW_NUMBER) {
                        transmissionCode = vehicleSetString;
                        transmission = findReferenceItemByCode(transmissions, transmissionCode);
                    }

                    if(count > TRANSMISSION_ROW_NUMBER && count % 2 != 0 && !vehicleSetString.trim().isEmpty()) {
                        yearCode = vehicleSetString;
                        year = findReferenceItemByCode(manufactureYears, yearCode);
                    }
                    if(count > TRANSMISSION_ROW_NUMBER && count % 2 == 0 && !vehicleSetString.trim().isEmpty()) {

                        try {
                            price = BigDecimal.valueOf(Double.parseDouble(vehicleSetString));
                        }
                        catch (Exception exception) {
                            price = null;
                        }

                    }

                    if(count > TRANSMISSION_ROW_NUMBER && count % 2 == 0) {

                        if(year == null && price == null) {
                            count++;
                            continue;
                        }
                        else if(year == null || price == null) {
                            if(year == null) {
                                yearErrors.add(messageService.getLocalizedMessage("messages.year_of_manufacture_not_found_by_code") + " " + yearCode);
                            }
                            if(price == null) {
                                yearErrors.add(messageService.getLocalizedMessage("messages.price_is_empty_or_incorrect") + " " + price + (yearCode != null ? " " + messageService.getLocalizedMessage("messages.for_year") + " " + yearCode : ""));
                            }
                        }
                        else {
                            VehicleSetYearEntity vehicleSetYearEntity = new VehicleSetYearEntity(null, null, year, price);
                            years.add(vehicleSetYearEntity);
                        }

                        yearCode = null;
                        year = null;
                        price = null;
                    }

                    count++;
                }

                if(vehicleKind == null
                        || brandId == null
                        || model == null
                        || bodyType == null
                        || engineCapacity == null
                        || enginePower == null
                        || engineType == null
                        || transmission == null
                        || yearErrors.size() != 0
                ) {

                    StringBuilder error = new StringBuilder("");
                    error.append(messageService.getLocalizedMessage("messages.row_with_number") + " " + orderNumber);
                    error.append(" " + messageService.getLocalizedMessage("messages.contains_errors"));
                    Boolean isFirstRowError = false;

                    if(vehicleKind == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.vehicle_kind_not_found_by_code") + " " + vehicleKindCode);
                        isFirstRowError = true;
                    }
                    if(brandId == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.brand_not_found_by_name") + " " + brandName);
                        isFirstRowError = true;
                    }
                    if(model == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.model_not_found_by_name") + " " + modelName);
                        error.append(" " + messageService.getLocalizedMessage("messages.for_brand") + " " + brandName);
                        error.append(" " + messageService.getLocalizedMessage("messages.for_vehicle_kind") + " " + vehicleKindCode);
                        isFirstRowError = true;
                    }
                    if(bodyType == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.body_type_not_found_by_code") + " " + bodyTypeCode);
                        isFirstRowError = true;
                    }
                    if(engineCapacity == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.engine_capacity_not_found_by_code") + " " + engineCapacityCode);
                        isFirstRowError = true;
                    }
                    if(enginePower == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.engine_power_not_found_by_code") + " " + enginePowerCode);
                        isFirstRowError = true;
                    }
                    if(engineType == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.engine_type_not_found_by_code") + " " + engineTypeCode);
                        isFirstRowError = true;
                    }
                    if(transmission == null) {
                        error.append((!isFirstRowError ? " " : " , ") + messageService.getLocalizedMessage("messages.transmission_not_found_by_code") + " " + transmissionCode);
                        isFirstRowError = true;
                    }
                    if(yearErrors.size() != 0) {

                        for(String yearError : yearErrors) {
                            error.append((!isFirstRowError ? " " : " , ") + yearError);
                        }

                    }

                    errors.add(error.toString());
                }
                else {

                    VehicleSetEntity vehicleSetEntity = new VehicleSetEntity(null, model, bodyType, engineCapacity, engineType, enginePower, transmission);

                    for(VehicleSetYearEntity curYear : years) {
                        curYear.setSet(vehicleSetEntity);
                    }
                    vehicleSetEntity.setYears(years);
                    vehicleSetEntities.add(vehicleSetEntity);
                }

            }

        }

        if(!loadWithErrors.equalsIgnoreCase("true") && errors.size() > 0) {
            fileUploadHistoryEntity.setErrorMessage(errors.stream().collect(Collectors.joining("\n")));
            fileUploadHistoryEntity.setStatus(FILE_UPLOAD_STATUS_ERROR);
            fileUploadHistoryEntity.setUploadDateEnd(LocalDateTime.now());
            fileUploadHistoryRepository.save(fileUploadHistoryEntity);
            throw new BusinessException(
                    Errors.UPLOAD_VEHICLE_SET_FILE,
                    messageService.getLocalizedMessage("messages.upload_file_contains_errors") + "\n" + errors.stream().collect(Collectors.joining("\n"))
            );
        }

        return vehicleSetEntities;
    }

    private Long findVehicleBrandByName(List<VehicleBrandEntity> brands, String name) {

        for(VehicleBrandEntity brand : brands) {
            if(brand.getName().equalsIgnoreCase(name.trim())) {
                return brand.getId();
            }
        }

        return null;
    }

    private VehicleModelEntity findVehicleModelByName(Long brandId, Long vehicleKindId, List<VehicleModelEntity> models, String name) {

        for(VehicleModelEntity model : models) {
            if(model.getBrand().getId().equals(brandId) && model.getKind().getId().equals(vehicleKindId) && model.getName().equalsIgnoreCase(name.trim())) {
                return model;
            }
        }

        return null;
    }

    private ReferenceItemEntity findReferenceItemByCode(List<ReferenceItemEntity> items, String code) {

        for(ReferenceItemEntity item : items) {
            if(item.getCode().equalsIgnoreCase(code) || item.getBrief().equalsIgnoreCase(code)) {
                return item;
            }
        }

        return null;
    }
}
