package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.VehicleSetYearReqDTO;
import ru.otus.homework.dto.VehicleSetYearResDTO;
import ru.otus.homework.entity.QVehicleSetYearEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.entity.VehicleSetEntity;
import ru.otus.homework.entity.VehicleSetYearEntity;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mappers.VehicleSetYearMapper;
import ru.otus.homework.repository.VehicleSetRepository;
import ru.otus.homework.repository.VehicleSetYearRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehicleSetYearServiceImpl implements VehicleSetYearService {

    private final VehicleSetYearRepository vehicleSetYearRepository;

    private final VehicleSetRepository vehicleSetRepository;
    private final ReferenceItemService referenceItemService;
    private final VehicleSetYearMapper vehicleSetYearMapper;
    private final MessageService messageService;

    @Override
    public Iterable<VehicleSetYearResDTO> findByParams(Predicate predicate, Pageable pageable) {
        return vehicleSetYearRepository.findAll(predicate, pageable).map(vehicleSetYearMapper::toDto);
    }

    @Override
    public VehicleSetYearResDTO findById(Long id) {

        VehicleSetYearEntity vehicleSetYearEntity = vehicleSetYearRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_SET_YEAR_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_set_year_not_found_by_id") + " " + id
                ));

        return vehicleSetYearMapper.toDto(vehicleSetYearEntity);
    }

    @Override
    public VehicleSetYearResDTO create(VehicleSetYearReqDTO vehicleSetYearReqDTO) {

        VehicleSetYearEntity vehicleSetYearEntity = checkAndPrepareVehicleSetYear(null, vehicleSetYearReqDTO);
        vehicleSetYearEntity = vehicleSetYearRepository.save(vehicleSetYearEntity);

        return vehicleSetYearMapper.toDto(vehicleSetYearEntity);
    }

    @Override
    public VehicleSetYearResDTO modify(Long id, VehicleSetYearReqDTO vehicleSetYearReqDTO) {

        VehicleSetYearEntity vehicleSetYearEntity = checkAndPrepareVehicleSetYear(id, vehicleSetYearReqDTO);
        vehicleSetYearEntity = vehicleSetYearRepository.save(vehicleSetYearEntity);

        return vehicleSetYearMapper.toDto(vehicleSetYearEntity);
    }

    @Override
    public void delete(Long id) {

        VehicleSetYearEntity vehicleSetYearEntity = vehicleSetYearRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.VEHICLE_SET_YEAR_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_set_year_not_found_by_id") + " " + id
                ));

        //удаляем комплектацию
        vehicleSetYearRepository.delete(vehicleSetYearEntity);
    }

    @Override
    public List<VehicleSetYearResDTO> findAllVehicleSetYears(Long setId, Long yearId) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(setId != null) {
            predicates.add(QVehicleSetYearEntity.vehicleSetYearEntity.set.id.eq(setId));
        }
        if(yearId != null) {
            predicates.add(QVehicleSetYearEntity.vehicleSetYearEntity.year.id.eq(yearId));
        }

        List<VehicleSetYearEntity> setYears;

        if(predicates.size() == 0) {
            setYears = vehicleSetYearRepository.findAll();
        }
        else if(predicates.size() == 1) {
            setYears = vehicleSetYearRepository.findAll(predicates.get(0));
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
            setYears = vehicleSetYearRepository.findAll(fullPredicate);
        }

        return vehicleSetYearMapper.toListDto(setYears);

    }

    private VehicleSetYearEntity checkAndPrepareVehicleSetYear(Long id, VehicleSetYearReqDTO vehicleSetYearReqDTO) {

        if(id != null) {
            VehicleSetYearEntity vehicleSetYearEntity = vehicleSetYearRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            Errors.VEHICLE_SET_YEAR_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.vehicle_set_year_not_found_by_id") + " " + id
                    ));
        }

        VehicleSetEntity set = null;
        ReferenceItemEntity year = null;
        Double price = vehicleSetYearReqDTO.getPrice();

        Long setId = null;

        if(vehicleSetYearReqDTO.getSet() != null) {
            setId = vehicleSetYearReqDTO.getSet().getId();
        }

        if(setId == null) {
            throw new BusinessException(
                    Errors.VEHICLE_SET_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_vehicle_set_id")
            );
        }

        Long yearId = null;

        if(vehicleSetYearReqDTO.getYear() != null) {
            yearId = vehicleSetYearReqDTO.getYear().getId();
        }

        //проверка наличия комплектации автомобиля
        if(setId != null) {
            Optional<VehicleSetEntity> foundSet = vehicleSetRepository.findById(setId);
            if(foundSet.isPresent()) {
                set = foundSet.get();
            }
            else {
                throw new BusinessException(
                        Errors.VEHICLE_SET_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.vehicle_set_not_found_by_id") + " " + setId
                );
            }
        }

        // проверка наличия типа кузова
        if(yearId != null) {
            year = referenceItemService.checkReferenceItem(
                    yearId,
                    "yearOfManufacture",
                    Errors.VEHICLE_YEAR_OF_MANUFACTURE_NOT_FOUND_BY_ID,
                    messageService.getLocalizedMessage("messages.vehicle_year_of_manufacture_not_found_by_id"),
                    Errors.INVALID_VEHICLE_YEAR_OF_MANUFACTURE_REFERENCE,
                    messageService.getLocalizedMessage("messages.invalid_vehicle_year_of_manufacture_reference")
            );
        }

        if(id != null) {
            return new VehicleSetYearEntity(id, set, year, BigDecimal.valueOf(price));
        }
        return new VehicleSetYearEntity(null, set, year, BigDecimal.valueOf(price));
    }
}
