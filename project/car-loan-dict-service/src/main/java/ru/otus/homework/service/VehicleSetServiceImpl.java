package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.homework.dto.CheckVehicleSetPriceResDTO;
import ru.otus.homework.dto.VehicleSetReqDTO;
import ru.otus.homework.dto.VehicleSetResDTO;
import ru.otus.homework.entity.*;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.mappers.ReferenceItemMapper;
import ru.otus.homework.mappers.VehicleSetMapper;
import ru.otus.homework.parsers.Parser;
import ru.otus.homework.repository.ReferenceRepository;
import ru.otus.homework.repository.VehicleModelRepository;
import ru.otus.homework.repository.VehicleSetRepository;
import ru.otus.homework.repository.VehicleSetYearRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehicleSetServiceImpl implements VehicleSetService {

    private final VehicleSetRepository vehicleSetRepository;
    private final VehicleSetYearRepository vehicleSetYearRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final ReferenceRepository referenceRepository;
    private final ReferenceItemService referenceItemService;
    private final VehicleSetMapper vehicleSetMapper;
    private final ReferenceItemMapper referenceItemMapper;
    private final MessageService messageService;
    private final Loader loader;
    private final Parser parser;

    @Override
    public Iterable<VehicleSetResDTO> findByParams(Predicate predicate, Pageable pageable) {
        return vehicleSetRepository.findAll(predicate, pageable).map(vehicleSetMapper::toDto);
    }

    @Override
    @Transactional
    public List<VehicleSetResDTO> findByParams(Predicate predicate) {
        List<VehicleSetEntity> vehicleSetEntities = vehicleSetRepository.findAll(predicate);
        return vehicleSetMapper.toListDto(vehicleSetEntities);
    }

    @Override
    public VehicleSetResDTO findById(Long id) {

        VehicleSetEntity vehicleSetEntity = vehicleSetRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_SET_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_set_not_found_by_id") + " " + id
                ));

        return vehicleSetMapper.toDto(vehicleSetEntity);
    }

    @Override
    @Transactional
    public VehicleSetResDTO create(VehicleSetReqDTO vehicleSetReqDTO) {
        VehicleSetEntity vehicleSetEntity = checkAndPrepareVehicleSet(null, vehicleSetReqDTO);
        vehicleSetEntity = vehicleSetRepository.save(vehicleSetEntity);

        return vehicleSetMapper.toDto(vehicleSetEntity);
    }

    @Override
    @Transactional
    public VehicleSetResDTO modify(Long id, VehicleSetReqDTO vehicleSetReqDTO) {

        VehicleSetEntity vehicleSetEntity = checkAndPrepareVehicleSet(id, vehicleSetReqDTO);
        vehicleSetEntity = vehicleSetRepository.save(vehicleSetEntity);

        return vehicleSetMapper.toDto(vehicleSetEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        VehicleSetEntity vehicleSetEntity = vehicleSetRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_SET_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_set_not_found_by_id") + " " + id
                ));

        //удаляем комплектацию
        vehicleSetRepository.delete(vehicleSetEntity);
    }

    @Override
    public List<VehicleSetResDTO> findAllVehicleSets(Long modelId, Long bodyId, Long engineSizeId, Long engineTypeId, Long powerId, Long transmissionId, Long yearId) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(modelId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.model.id.eq(modelId));
        }
        if(bodyId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.body.id.eq(bodyId));
        }
        if(engineSizeId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.engineSize.id.eq(engineSizeId));
        }
        if(engineTypeId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.engineType.id.eq(engineTypeId));
        }
        if(powerId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.power.id.eq(powerId));
        }
        if(transmissionId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.transmission.id.eq(transmissionId));
        }
        if(yearId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.years.any().year.id.eq(yearId));
        }

        List<VehicleSetEntity> sets;

        if(predicates.size() == 0) {
            sets = vehicleSetRepository.findAll();
        }
        else if(predicates.size() == 1) {
            sets = vehicleSetRepository.findAll(predicates.get(0));
        }
        else {
            BooleanExpression fullPredicate = predicates.get(0);
            int index = 0;
            for(BooleanExpression predicate : predicates) {
                if(index < predicates.size() - 1) {
                    index++;
                    fullPredicate = fullPredicate.and(predicates.get(index));
                }
            }
            sets = vehicleSetRepository.findAll(fullPredicate);
        }

        return vehicleSetMapper.toListDto(sets);
    }

    @Override
    public CheckVehicleSetPriceResDTO checkVehicleSetPrice(Long modelId, String characteristicSysName, Long bodyId, Long engineSizeId, Long engineTypeId, Long powerId, Long transmissionId, Long yearId) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(modelId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.model.id.eq(modelId));
        }
        if(bodyId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.body.id.eq(bodyId));
        }
        if(engineSizeId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.engineSize.id.eq(engineSizeId));
        }
        if(engineTypeId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.engineType.id.eq(engineTypeId));
        }
        if(powerId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.power.id.eq(powerId));
        }
        if(transmissionId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.transmission.id.eq(transmissionId));
        }
        if(yearId != null) {
            predicates.add(QVehicleSetEntity.vehicleSetEntity.years.any().year.id.eq(yearId));
        }

        List<VehicleSetEntity> sets;

        if(predicates.size() == 0) {
            sets = vehicleSetRepository.findAll();
        }
        else if(predicates.size() == 1) {
            sets = vehicleSetRepository.findAll(predicates.get(0));
        }
        else {
            BooleanExpression fullPredicate = predicates.get(0);
            int index = 0;
            for(BooleanExpression predicate : predicates) {
                if(index < predicates.size() - 1) {
                    index++;
                    fullPredicate = fullPredicate.and(predicates.get(index));
                }
            }
            sets = vehicleSetRepository.findAll(fullPredicate);
        }

        if(characteristicSysName != null && !characteristicSysName.isEmpty()) {

            ReferenceEntity referenceEntity = referenceRepository.findOne(QReferenceEntity.referenceEntity.sysname.equalsIgnoreCase(characteristicSysName)).orElseThrow(() -> new BusinessException(Errors.VEHICLE_SET_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.reference_not_found_by_sysname") + " " + characteristicSysName
            ));

            List<ReferenceItemEntity> uniqueCharacteristicItems = new ArrayList<>();

            for(VehicleSetEntity set : sets) {

                if(!characteristicSysName.equals("yearOfManufacture")) {

                    ReferenceItemEntity referenceItemEntity = null;

                    if(set.getBody().getReference().getId().equals(referenceEntity.getId())) {
                        referenceItemEntity = set.getBody();
                    }
                    else if(set.getEngineSize().getReference().getId().equals(referenceEntity.getId())) {
                        referenceItemEntity = set.getEngineSize();
                    }
                    else if(set.getEngineType().getReference().getId().equals(referenceEntity.getId())) {
                        referenceItemEntity = set.getEngineType();
                    }
                    else if(set.getPower().getReference().getId().equals(referenceEntity.getId())) {
                        referenceItemEntity = set.getPower();
                    }
                    else if(set.getTransmission().getReference().getId().equals(referenceEntity.getId())) {
                        referenceItemEntity = set.getTransmission();
                    }

                    Boolean foundFlag = false;

                    for(ReferenceItemEntity item : uniqueCharacteristicItems) {
                        if(item.getId().equals(referenceItemEntity.getId())) {
                            foundFlag = true;
                            break;
                        }
                    }

                    if(!foundFlag) {
                        uniqueCharacteristicItems.add(referenceItemEntity);
                    }
                }

                else if(characteristicSysName.equals("yearOfManufacture")) {

                    for(VehicleSetYearEntity year : set.getYears()) {

                        Boolean foundFlag = false;

                        for(ReferenceItemEntity item : uniqueCharacteristicItems) {
                            if(item.getId().equals(year.getYear().getId())) {
                                foundFlag = true;
                                break;
                            }
                        }

                        if(!foundFlag) {
                            uniqueCharacteristicItems.add(year.getYear());
                        }

                    }
                }
            }

            CheckVehicleSetPriceResDTO checkVehicleSetPriceResDTO = new CheckVehicleSetPriceResDTO();
            checkVehicleSetPriceResDTO.setCharacteristicUniqueValues(referenceItemMapper.toListDto(uniqueCharacteristicItems));
            checkVehicleSetPriceResDTO.setPrice(null);
            return checkVehicleSetPriceResDTO;
        }
        else {

            if(sets != null && sets.size() == 1) {
                VehicleSetEntity vehicleSetEntity = sets.get(0);

                for(VehicleSetYearEntity year : vehicleSetEntity.getYears()) {
                   if(year.getYear().getId().equals(yearId)) {
                       CheckVehicleSetPriceResDTO checkVehicleSetPriceResDTO = new CheckVehicleSetPriceResDTO();
                       checkVehicleSetPriceResDTO.setCharacteristicUniqueValues(null);
                       checkVehicleSetPriceResDTO.setPrice(year.getPrice().doubleValue());
                       return checkVehicleSetPriceResDTO;
                   }
                }
            }

        }

        return new CheckVehicleSetPriceResDTO();
    }

    @Override
    @Transactional
    public void saveVehicleSetsFromFile(List<VehicleSetEntity> vehicleSetEntities) {

        List<VehicleSetYearEntity> vehicleSetYearEntities = new ArrayList<>();

        vehicleSetYearRepository.deleteAllVehicleSetYears();
        vehicleSetRepository.deleteAllVehicleSets();

        vehicleSetRepository.saveAll(vehicleSetEntities);

        for(VehicleSetEntity vehicleSetEntity : vehicleSetEntities) {
            for(VehicleSetYearEntity vehicleSetYearEntity : vehicleSetEntity.getYears()) {
                vehicleSetYearEntity.setSet(vehicleSetEntity);
                vehicleSetYearEntities.add(vehicleSetYearEntity);
            }
        }

        vehicleSetYearRepository.saveAll(vehicleSetYearEntities);

    }

    private VehicleSetEntity checkAndPrepareVehicleSet(Long id, VehicleSetReqDTO vehicleSetReqDTO) {

        List<VehicleSetYearEntity> years = new ArrayList<>();

        if(id != null) {
            VehicleSetEntity vehicleSetEntity = vehicleSetRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            Errors.VEHICLE_SET_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.vehicle_set_not_found_by_id") + " " + id
                    ));
            if(vehicleSetEntity.getYears() != null && vehicleSetEntity.getYears().size() > 0) {
                years = vehicleSetEntity.getYears();
            }
        }

        VehicleModelEntity model = null;
        ReferenceItemEntity body = null;
        ReferenceItemEntity engineSize = null;
        ReferenceItemEntity engineType = null;
        ReferenceItemEntity power = null;
        ReferenceItemEntity transmission = null;

        Long modelId = null;

        if(vehicleSetReqDTO.getModel() != null) {
            modelId = vehicleSetReqDTO.getModel().getId();
        }

        if(modelId == null) {
            throw new BusinessException(
                    Errors.VEHICLE_MODEL_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_vehicle_model_id")
            );
        }

        Long bodyId = null;
        Long engineSizeId = null;
        Long engineTypeId = null;
        Long powerId = null;
        Long transmissionId = null;

        if(vehicleSetReqDTO.getBody() != null) {
            bodyId = vehicleSetReqDTO.getBody().getId();
        }
        if(vehicleSetReqDTO.getEngineSize() != null) {
            engineSizeId = vehicleSetReqDTO.getEngineSize().getId();
        }
        if(vehicleSetReqDTO.getEngineType() != null) {
            engineTypeId = vehicleSetReqDTO.getEngineType().getId();
        }
        if(vehicleSetReqDTO.getPower() != null) {
            powerId = vehicleSetReqDTO.getPower().getId();
        }
        if(vehicleSetReqDTO.getTransmission() != null) {
            transmissionId = vehicleSetReqDTO.getTransmission().getId();
        }

        //проверка наличия модели автомобиля
        if(modelId != null) {
            Long finalModelId = modelId;
            model = vehicleModelRepository.findById(modelId)
                    .orElseThrow(() -> new BusinessException(
                            Errors.VEHICLE_MODEL_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.vehicle_model_not_found_by_id") + " " + finalModelId
                    ));
        }

        // проверка наличия типа кузова
        if(bodyId != null) {
            body = referenceItemService.checkReferenceItem(
                    bodyId,
                    "bodyType",
                    Errors.VEHICLE_BODY_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_body_not_found_by_id"),
                    Errors.INVALID_VEHICLE_BODY_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_body_reference")
            );
        }

        // проверка наличия объёма двигателя
        if(engineSizeId != null) {
            engineSize = referenceItemService.checkReferenceItem(
                    engineSizeId,
                    "engineCapacity",
                    Errors.VEHICLE_ENGINE_SIZE_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_engine_size_not_found_by_id"),
                    Errors.INVALID_VEHICLE_ENGINE_SIZE_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_engine_size_reference")
            );
        }

        // проверка наличия типа двигателя
        if(engineTypeId != null) {
            engineType = referenceItemService.checkReferenceItem(
                    engineTypeId,
                    "engineType",
                    Errors.VEHICLE_ENGINE_TYPE_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_engine_type_not_found_by_id"),
                    Errors.INVALID_VEHICLE_ENGINE_TYPE_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_engine_type_reference")
            );
        }

        // проверка наличия мощности двигателя
        if(powerId != null) {
            power = referenceItemService.checkReferenceItem(
                    powerId,
                    "enginePower",
                    Errors.VEHICLE_ENGINE_POWER_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_engine_power_not_found_by_id"),
                    Errors.INVALID_VEHICLE_ENGINE_POWER_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_engine_power_reference")
            );
        }

        // проверка наличия мощности двигателя
        if(transmissionId != null) {
            transmission = referenceItemService.checkReferenceItem(
                    transmissionId,
                    "transmission",
                    Errors.VEHICLE_TRANSMISSION_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_transmission_not_found_by_id"),
                    Errors.INVALID_VEHICLE_TRANSMISSION_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_transmission_reference")
            );
        }

        if(id != null) {
            return new VehicleSetEntity(id, model, body, engineSize, engineType, power, transmission, years);
        }
        return new VehicleSetEntity(null, model, body, engineSize, engineType, power, transmission);
    }

}
