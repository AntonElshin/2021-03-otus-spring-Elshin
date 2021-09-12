package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.RefGroupResDTO;
import ru.otus.homework.dto.ReferenceReqDTO;
import ru.otus.homework.dto.ReferenceResDTO;
import ru.otus.homework.entity.QReferenceEntity;
import ru.otus.homework.entity.ReferenceEntity;
import ru.otus.homework.entity.ReferenceGroupEntity;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mappers.ReferenceMapper;
import ru.otus.homework.repository.ReferenceGroupRepository;
import ru.otus.homework.repository.ReferenceItemRepository;
import ru.otus.homework.repository.ReferenceRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReferenceServiceImpl implements ReferenceService {

    private final ReferenceRepository referenceRepository;

    private final ReferenceGroupRepository referenceGroupRepository;
    private final ReferenceItemRepository referenceItemRepository;
    private final ReferenceMapper referenceMapper;
    private final MessageService messageService;

    @Override
    @SneakyThrows
    public Iterable<ReferenceResDTO> findByParams(Predicate predicate, Pageable pageable) {
        Iterable<ReferenceResDTO> referencesDTO = referenceRepository.findAll(predicate, pageable).map(referenceMapper::toDto);
        return referencesDTO;
    }

    @Override
    public List<ReferenceResDTO> findByParams(Predicate predicate) {
        List<ReferenceEntity> referenceEntities = referenceRepository.findAll(predicate);
        return referenceMapper.toListDto(referenceEntities);
    }

    @Override
    @SneakyThrows
    public ReferenceResDTO findById(Long id) {
        ReferenceEntity referenceEntity = referenceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_not_found_by_id") + " " + id
                ));

        return referenceMapper.toDto(referenceEntity);
    }

    @Override
    @SneakyThrows
    public ReferenceResDTO create(ReferenceReqDTO referenceReqDTO) {

        ReferenceEntity entity = referenceRepository.save(checkAndPrepareReference(null, referenceReqDTO));
        return referenceMapper.toDto(entity);
    }

    @Override
    @SneakyThrows
    public ReferenceResDTO modify(Long id, ReferenceReqDTO referenceReqDTO) {

        referenceReqDTO.setId(id);

        ReferenceEntity referenceEntity = referenceRepository.save(checkAndPrepareReference(id, referenceReqDTO));
        return referenceMapper.toDto(referenceEntity);
    }

    @Override
    @Transactional
    @SneakyThrows
    public void delete(Long id) {
        ReferenceEntity referenceEntity = referenceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_not_found_by_id") + " " + id
                ));

        deleteLinkedEntities(referenceEntity);

        //удаляем справочник
        referenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<ReferenceResDTO> findAllReferences(Long groupId) {

        if(groupId == null) {
            throw new BusinessException(
                    Errors.REFERENCE_GROUP_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_reference_group_id")
            );
        }

        List<ReferenceEntity> references = referenceRepository.findAll(QReferenceEntity.referenceEntity.group.id.eq(groupId));
        return referenceMapper.toListDto(references);

    }

    private void deleteLinkedEntities(ReferenceEntity referenceEntity) {

        referenceItemRepository.deleteAllByReferenceIdEquals(referenceEntity.getId());

    }

    private ReferenceEntity checkAndPrepareReference(Long id, ReferenceReqDTO referenceReqDTO) {

        ReferenceEntity referenceEntity = null;

        if(id != null) {
            referenceEntity = referenceRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            Errors.REFERENCE_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.reference_not_found_by_id") + " " + id
                    ));
        }

        ReferenceGroupEntity group = null;

        Long groupId = null;

        if(referenceReqDTO.getGroup() != null) {
            groupId = referenceReqDTO.getGroup().getId();
        }

        if(groupId == null) {
            throw new BusinessException(
                    Errors.REFERENCE_GROUP_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_reference_group_id")
            );
        }

        //проверка на уникальность сиснейма
        if(id == null || (id != null && !referenceReqDTO.getSysname().equalsIgnoreCase(referenceEntity.getSysname()))) {
            Optional<ReferenceEntity> foundReference = referenceRepository.findOne(QReferenceEntity.referenceEntity.sysname.equalsIgnoreCase(referenceReqDTO.getSysname()));
            if (foundReference.isPresent()) {
                throw new BusinessException(
                        Errors.NOT_UNIQUE_REFERENCE_SYSNAME,
                        messageService.getLocalizedMessage("messages.not_unique_reference_sysname") + " " + referenceReqDTO.getSysname()
                );
            }

        }

        //проверка наличия группы
        if(groupId != null) {
            group = referenceGroupRepository.findById(referenceReqDTO.getGroup().getId())
                    .orElseThrow(() -> new BusinessException(
                            Errors.REFERENCE_GROUP_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.reference_group_not_found_by_id") + " " + referenceReqDTO.getGroup().getId()
                    ));
        }

        if(id != null) {
            return new ReferenceEntity(id, group, referenceReqDTO.getName(), referenceReqDTO.getSysname(), referenceReqDTO.getDescription());
        }
        else {
            return new ReferenceEntity(null, group, referenceReqDTO.getName(), referenceReqDTO.getSysname(), referenceReqDTO.getDescription());
        }

    }

}
