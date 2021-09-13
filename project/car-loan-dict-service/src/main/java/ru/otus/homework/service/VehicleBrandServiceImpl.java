package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.VehicleBrandReqDTO;
import ru.otus.homework.dto.VehicleBrandResDTO;
import ru.otus.homework.entity.QVehicleBrandEntity;
import ru.otus.homework.entity.ReferenceGroupEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.entity.VehicleBrandEntity;
import ru.otus.homework.entity.VehicleModelEntity;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mappers.VehicleBrandMapper;
import ru.otus.homework.repository.VehicleBrandRepository;
import ru.otus.homework.repository.VehicleModelRepository;
import ru.otus.homework.repository.VehicleSetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository vehicleBrandRepository;

    private final ReferenceItemService referenceItemService;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleSetRepository vehicleSetRepository;
    private final VehicleBrandMapper vehicleBrandMapper;
    private final MessageService messageService;

    private final static String REFERENCE_SYS_NAME_PRODUCTION_KIND = "productionKind";

    @Override
    public Iterable<VehicleBrandResDTO> findByParams(Predicate predicate, Pageable pageable) {
        return vehicleBrandRepository.findAll(predicate, pageable).map(vehicleBrandMapper::toDto);
    }

    @Override
    @Transactional
    public List<VehicleBrandResDTO> findByParams(Predicate predicate) {
        List<VehicleBrandEntity> vehicleBrandEntities = vehicleBrandRepository.findAll(predicate);
        return vehicleBrandMapper.toListDto(vehicleBrandEntities);
    }

    @Override
    public VehicleBrandResDTO findById(Long id) {

        VehicleBrandEntity vehicleBrandEntity = vehicleBrandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_BRAND_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_brand_not_found_by_id") + " " + id
                ));

        return vehicleBrandMapper.toDto(vehicleBrandEntity);
    }

    @Override
    @Transactional
    public VehicleBrandResDTO create(VehicleBrandReqDTO vehicleBrandReqDTO) {

        VehicleBrandEntity vehicleBrandEntity = checkAndPrepareVehicleBrand(null, vehicleBrandReqDTO);
        vehicleBrandEntity = vehicleBrandRepository.save(vehicleBrandEntity);

        return vehicleBrandMapper.toDto(vehicleBrandEntity);
    }

    @Override
    @Transactional
    public VehicleBrandResDTO modify(Long id, VehicleBrandReqDTO vehicleBrandReqDTO) {

        VehicleBrandEntity vehicleBrandEntity = checkAndPrepareVehicleBrand(id, vehicleBrandReqDTO);
        vehicleBrandEntity = vehicleBrandRepository.save(vehicleBrandEntity);

        return vehicleBrandMapper.toDto(vehicleBrandEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        VehicleBrandEntity vehicleBrandEntity = vehicleBrandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_BRAND_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_brand_not_found_by_id") + " " + id
                ));

        List<VehicleModelEntity> models = vehicleModelRepository.findAllByBrand_Id(id);

        //удаляем комплектации
        for(VehicleModelEntity model : models) {
            vehicleSetRepository.deleteByModel_Id(model.getId());
        }

        //удаляем модели
        vehicleModelRepository.deleteByBrand_Id(vehicleBrandEntity.getId());

        //удаляем марку
        vehicleBrandRepository.delete(vehicleBrandEntity);

    }

    @Override
    public List<VehicleBrandResDTO> findAllVehicleBrands(String name, Long productionKindId) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(name != null && !name.isEmpty()) {
            predicates.add(QVehicleBrandEntity.vehicleBrandEntity.name.containsIgnoreCase(name));
        }
        if(productionKindId != null) {
            predicates.add(QVehicleBrandEntity.vehicleBrandEntity.productionKind.id.eq(productionKindId));
        }

        List<VehicleBrandEntity> brands;

        if(predicates.size() == 0) {
            brands = vehicleBrandRepository.findAll();
        }
        else if(predicates.size() == 1) {
            brands = vehicleBrandRepository.findAll(predicates.get(0));
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
            brands = vehicleBrandRepository.findAll(fullPredicate);
        }

        return vehicleBrandMapper.toListDto(brands);
    }

    private VehicleBrandEntity checkAndPrepareVehicleBrand(Long id, VehicleBrandReqDTO vehicleBrandReqDTO) {

        VehicleBrandEntity vehicleBrandEntity = null;

        if(id != null) {
            vehicleBrandEntity = vehicleBrandRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            Errors.VEHICLE_BRAND_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.vehicle_brand_not_found_by_id") + " " + id
                    ));
        }

        //проверка на уникальность названия марки
        if(id == null || (id != null && !vehicleBrandEntity.getName().equalsIgnoreCase(vehicleBrandReqDTO.getName()))) {
            Optional<VehicleBrandEntity> foundBrand = vehicleBrandRepository.findByNameEqualsIgnoreCase(vehicleBrandReqDTO.getName());
            if(foundBrand.isPresent()) {
                throw new BusinessException(
                        Errors.NOT_UNIQUE_BRAND_NAME,
                        messageService.getLocalizedMessage("messages.not_unique_brand_name") + " " + vehicleBrandReqDTO.getName()
                );
            }
        }

        ReferenceItemEntity productionKind = null;
        Long productionKindId = null;

        if(vehicleBrandReqDTO.getProductionKind() != null) {
            productionKindId = vehicleBrandReqDTO.getProductionKind().getId();
        }

        // проверка наличия типа производства
        if(productionKindId != null) {
            productionKind = referenceItemService.checkReferenceItem(
                    productionKindId,
                    REFERENCE_SYS_NAME_PRODUCTION_KIND,
                    Errors.PRODUCTION_TYPE_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.production_type_not_found_by_id"),
                    Errors.INVALID_PRODUCTION_TYPE_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_production_type_reference")
            );
        }

        if(id != null) {
            return new VehicleBrandEntity(id, vehicleBrandReqDTO.getName(), productionKind);
        }
        else {
            return new VehicleBrandEntity(null, vehicleBrandReqDTO.getName(), productionKind);
        }
    }

}
