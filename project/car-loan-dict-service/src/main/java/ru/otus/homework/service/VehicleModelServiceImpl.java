package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.RefItemReqIdDTO;
import ru.otus.homework.dto.VehicleModelReqDTO;
import ru.otus.homework.dto.VehicleModelResDTO;
import ru.otus.homework.entity.QVehicleModelEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.entity.VehicleBrandEntity;
import ru.otus.homework.entity.VehicleModelEntity;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mappers.VehicleModelMapper;
import ru.otus.homework.repository.VehicleBrandRepository;
import ru.otus.homework.repository.VehicleModelRepository;
import ru.otus.homework.repository.VehicleSetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;

    private final VehicleBrandRepository vehicleBrandRepository;
    private final VehicleSetRepository vehicleSetRepository;
    private final ReferenceItemService referenceItemService;
    private final VehicleModelMapper vehicleModelMapper;
    private final MessageService messageService;

    @Override
    public Iterable<VehicleModelResDTO> findByParams(Predicate predicate, Pageable pageable) {
        return vehicleModelRepository.findAll(predicate, pageable).map(vehicleModelMapper::toDto);
    }

    @Override
    @Transactional
    public List<VehicleModelResDTO> findByParams(Predicate predicate) {
        List<VehicleModelEntity> vehicleModelEntities = vehicleModelRepository.findAll(predicate);
        return vehicleModelMapper.toListDto(vehicleModelEntities);
    }

    @Override
    public VehicleModelResDTO findById(Long id) {

        VehicleModelEntity vehicleModelEntity = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_MODEL_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_model_not_found_by_id") + " " + id
                ));

        return vehicleModelMapper.toDto(vehicleModelEntity);
    }

    @Override
    @Transactional
    public VehicleModelResDTO create(VehicleModelReqDTO vehicleModelReqDTO) {

        VehicleModelEntity vehicleModelEntity = checkAndPrepareVehicleModel(null, vehicleModelReqDTO);
        vehicleModelEntity = vehicleModelRepository.save(vehicleModelEntity);

        return vehicleModelMapper.toDto(vehicleModelEntity);
    }

    @Override
    @Transactional
    public VehicleModelResDTO modify(Long id, VehicleModelReqDTO vehicleModelReqDTO) {

        VehicleModelEntity vehicleModelEntity = checkAndPrepareVehicleModel(id, vehicleModelReqDTO);
        vehicleModelEntity = vehicleModelRepository.save(vehicleModelEntity);

        return vehicleModelMapper.toDto(vehicleModelEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        VehicleModelEntity vehicleModelEntity = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_MODEL_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_model_not_found_by_id") + " " + id
                ));

        //удаляем комплектации
        vehicleSetRepository.deleteByModel_Id(vehicleModelEntity.getId());

        //удаляем модель
        vehicleModelRepository.delete(vehicleModelEntity);

    }

    @Override
    public List<VehicleModelResDTO> findAllVehicleModels(Long vehicleBrandId, String name, Long kindId, Long propertyFormId, Long productionKindId) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(vehicleBrandId != null) {
            predicates.add(QVehicleModelEntity.vehicleModelEntity.brand.id.eq(vehicleBrandId));
        }
        if(name != null && !name.isEmpty()) {
            predicates.add(QVehicleModelEntity.vehicleModelEntity.name.containsIgnoreCase(name));
        }
        if(kindId != null) {
            predicates.add(QVehicleModelEntity.vehicleModelEntity.kind.id.eq(kindId));
        }
        if(propertyFormId != null) {
            predicates.add(QVehicleModelEntity.vehicleModelEntity.ownForms.any().id.eq(propertyFormId));
        }
        if(productionKindId != null) {
            predicates.add(QVehicleModelEntity.vehicleModelEntity.brand.productionKind.id.eq(productionKindId));
        }

        List<VehicleModelEntity> models;

        if(predicates.size() == 0) {
            models = vehicleModelRepository.findAll();
        }
        else if(predicates.size() == 1) {
            models = vehicleModelRepository.findAll(predicates.get(0));
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
            models = vehicleModelRepository.findAll(fullPredicate);
        }

        return vehicleModelMapper.toListDto(models);
    }

    private VehicleModelEntity checkAndPrepareVehicleModel(Long id, VehicleModelReqDTO vehicleModelReqDTO) {

        VehicleModelEntity vehicleModelEntity = null;

        if(id != null) {
            vehicleModelEntity = vehicleModelRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            Errors.VEHICLE_MODEL_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.vehicle_model_not_found_by_id") + " " + id
                    ));
        }

        VehicleBrandEntity brand = null;
        ReferenceItemEntity kind = null;
        List<ReferenceItemEntity> ownForms = new ArrayList<>();

        Long brandId = null;

        if(vehicleModelReqDTO.getBrand() != null) {
            brandId = vehicleModelReqDTO.getBrand().getId();
        }

        if(brandId == null) {
            throw new BusinessException(
                    Errors.VEHICLE_BRAND_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_vehicle_brand_id")
            );
        }

        Long kindId = null;

        if(vehicleModelReqDTO.getKind() != null) {
            kindId = vehicleModelReqDTO.getKind().getId();
        }

        if(kindId == null) {
            throw new BusinessException(
                    Errors.VEHICLE_MODEL_KIND_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_vehicle_model_kind_id")
            );
        }

        //проверка на уникальность названия марки
        if(id == null || (id != null && !vehicleModelEntity.getName().equalsIgnoreCase(vehicleModelReqDTO.getName()))) {
            Optional<VehicleModelEntity> foundModel = vehicleModelRepository.findOne(
                    QVehicleModelEntity.vehicleModelEntity.brand.id.eq(vehicleModelReqDTO.getBrand().getId())
                            .and(QVehicleModelEntity.vehicleModelEntity.name.equalsIgnoreCase(vehicleModelReqDTO.getName()))
                            .and(QVehicleModelEntity.vehicleModelEntity.kind.id.eq(vehicleModelReqDTO.getKind().getId()))
            );
            if(foundModel.isPresent()) {
                throw new BusinessException(
                        Errors.NOT_UNIQUE_MODEL_NAME,
                        messageService.getLocalizedMessage("messages.not_unique_model_name") + " " +
                                vehicleModelReqDTO.getName() + " " +
                                messageService.getLocalizedMessage("messages.for_brand_id") + " " +
                                vehicleModelReqDTO.getBrand().getId() + " " +
                                messageService.getLocalizedMessage("messages.and_for_vehicle_kind") + " " +
                                vehicleModelReqDTO.getKind().getId()
                );
            }
        }

        //проверка наличия марки автомобиля
        if(brandId != null) {
            Long finalBrandId = brandId;
            brand = vehicleBrandRepository.findById(brandId)
                    .orElseThrow(() -> new BusinessException(
                            Errors.VEHICLE_BRAND_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.vehicle_brand_not_found_by_id") + " " + finalBrandId
                    ));
        }

        // проверка наличия вида транспортного средства
        if(kindId != null) {
            kind = referenceItemService.checkReferenceItem(
                    kindId,
                    "vehicleKind",
                    Errors.VEHICLE_KIND_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_kind_not_found_by_id"),
                    Errors.INVALID_VEHICLE_KIND_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_kind_reference")
            );
        }

        //проверка наличия форм собственности
        if(vehicleModelReqDTO.getOwnForms() != null && vehicleModelReqDTO.getOwnForms().size() != 0) {

            List<RefItemReqIdDTO> items = vehicleModelReqDTO.getOwnForms();
            for(RefItemReqIdDTO item : items) {
                if(item.getId() != null) {
                    ReferenceItemEntity ownForm = referenceItemService.checkReferenceItem(
                            item.getId(),
                            "propertyForm",
                            Errors.PROPERTY_FORM_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.property_form_not_found_by_id"),
                            Errors.INVALID_PROPERTY_FORM_REFERENCE,
                            messageService.getLocalizedMessage("messages.invalid_property_form_reference")
                    );
                    ownForms.add(ownForm);
                }
            }

        }

        if(id != null) {
            return new VehicleModelEntity(id, brand, vehicleModelReqDTO.getName(), kind, ownForms);
        }
        else {
            return new VehicleModelEntity(null, brand, vehicleModelReqDTO.getName(), kind, ownForms);
        }
    }

}
