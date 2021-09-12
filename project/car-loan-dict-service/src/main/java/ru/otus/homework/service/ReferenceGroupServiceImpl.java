package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.RefGroupReqDTO;
import ru.otus.homework.dto.RefGroupResDTO;
import ru.otus.homework.entity.QReferenceEntity;
import ru.otus.homework.entity.QReferenceGroupEntity;
import ru.otus.homework.entity.ReferenceEntity;
import ru.otus.homework.entity.ReferenceGroupEntity;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mappers.ReferenceGroupMapper;
import ru.otus.homework.repository.ReferenceGroupRepository;
import ru.otus.homework.repository.ReferenceRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReferenceGroupServiceImpl implements ReferenceGroupService {

    private final ReferenceGroupRepository referenceGroupRepository;
    private final ReferenceRepository referenceRepository;
    private final ReferenceGroupMapper referenceGroupMapper;
    private final MessageService messageService;

    @Override
    @SneakyThrows
    public Iterable<RefGroupResDTO> findByParams(Predicate predicate, Pageable pageable) {
        Iterable<RefGroupResDTO> refGroupsResDTO = referenceGroupRepository.findAll(predicate, pageable).map(referenceGroupMapper::toDto);
        return refGroupsResDTO;
    }

    @Override
    public List<RefGroupResDTO> findByParams(Predicate predicate) {
        List<ReferenceGroupEntity> referenceGroupEntities = referenceGroupRepository.findAll(predicate);
        return referenceGroupMapper.toListDto(referenceGroupEntities);
    }

    @Override
    @SneakyThrows
    public RefGroupResDTO findById(Long id) {

        ReferenceGroupEntity referenceGroupEntity = referenceGroupRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_GROUP_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_group_not_found_by_id") + " " + id
                ));

        return referenceGroupMapper.toDto(referenceGroupEntity);
    }

    @Override
    @SneakyThrows
    public RefGroupResDTO create(RefGroupReqDTO refGroupReqDTO) {

        if(refGroupReqDTO.getParentId() == null) {
            refGroupReqDTO.setParentId(0L);
        }

        ReferenceGroupEntity referenceGroupEntity = referenceGroupRepository.save(checkAndPrepareReferenceGroup(null, refGroupReqDTO));

        return referenceGroupMapper.toDto(referenceGroupEntity);
    }

    @Override
    @SneakyThrows
    public RefGroupResDTO modify(Long id, RefGroupReqDTO refGroupReqDTO) {

        if(refGroupReqDTO.getParentId() == null) {
            refGroupReqDTO.setParentId(0L);
        }

        refGroupReqDTO.setId(id);

        ReferenceGroupEntity referenceGroupEntity = referenceGroupRepository.save(checkAndPrepareReferenceGroup(id, refGroupReqDTO));
        return referenceGroupMapper.toDto(referenceGroupEntity);
    }

    @Override
    @Transactional
    @SneakyThrows
    public void delete(Long id) {

        ReferenceGroupEntity entity = checkForDelete(id);

        //удаляем группу справочника
        referenceGroupRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefGroupResDTO> findAllReferenceGroups() {
        List<ReferenceGroupEntity> groups = referenceGroupRepository.findAll();
        return referenceGroupMapper.toListDto(groups);
    }

    private ReferenceGroupEntity checkAndPrepareReferenceGroup(Long id, RefGroupReqDTO refGroupReqDTO) {

        ReferenceGroupEntity referenceGroupEntity = null;

        if(id != null) {
            Optional<ReferenceGroupEntity> foundReferenceGroup = referenceGroupRepository.findById(id);

            if(foundReferenceGroup.isPresent()) {
                referenceGroupEntity = foundReferenceGroup.get();
            }
            else {
                throw new BusinessException(
                        Errors.REFERENCE_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_group_not_found_by_id") + " " + id);
            }
        }

        //проверка наличия родительской записи
        if(refGroupReqDTO.getParentId() != 0) {
            referenceGroupRepository.findById(refGroupReqDTO.getParentId())
                    .orElseThrow(() -> new BusinessException(
                            Errors.PARENT_REFERENCE_GROUP_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.parent_reference_group_not_found_by_id") + " " + refGroupReqDTO.getParentId()
                    ));
        }

        //проверка на уникальность сиснейма
        if(id == null || (id != null && !refGroupReqDTO.getSysname().equalsIgnoreCase(referenceGroupEntity.getSysname()))) {

            BooleanExpression fullPredicate = QReferenceGroupEntity.referenceGroupEntity.parentId.eq(refGroupReqDTO.getParentId()).and(
                    QReferenceGroupEntity.referenceGroupEntity.sysname.equalsIgnoreCase(refGroupReqDTO.getSysname())
            );

            Optional<ReferenceGroupEntity> foundGroup = referenceGroupRepository.findOne(fullPredicate);

            if(foundGroup.isPresent()) {
                throw new BusinessException(
                        Errors.NOT_UNIQUE_REFERENCE_GROUP_SYSNAME,
                        messageService.getLocalizedMessage("messages.not_unique_reference_group_sysname") + " " +
                                refGroupReqDTO.getSysname() + " " +
                                messageService.getLocalizedMessage("messages.for_parent_group_id") + " " +
                                refGroupReqDTO.getParentId()
                );
            }
        }

        if(id != null) {
            return new ReferenceGroupEntity(id, refGroupReqDTO.getParentId(), refGroupReqDTO.getName(), refGroupReqDTO.getSysname(), refGroupReqDTO.getDescription());
        }
        else {
            return new ReferenceGroupEntity(null, refGroupReqDTO.getParentId(), refGroupReqDTO.getName(), refGroupReqDTO.getSysname(), refGroupReqDTO.getDescription());
        }

    }

    private ReferenceGroupEntity checkForDelete(Long id) {

        // поиск элемента группы справочника по идентификатору
        ReferenceGroupEntity referenceGroupEntity = referenceGroupRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_GROUP_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_group_not_found_by_id") + " " + id
                ));

        //поиск вложенных в группу элементов
        if(referenceGroupEntity != null) {
            List<ReferenceGroupEntity> children = referenceGroupRepository.findAll(QReferenceGroupEntity.referenceGroupEntity.parentId.eq(referenceGroupEntity.getId()));
            if (children != null && children.size() > 0) {
                throw new BusinessException(
                        Errors.REFERENCE_GROUP_HAS_CHILDREN_GROUPS,
                        messageService.getLocalizedMessage("messages.reference_group_with_id") + " " +
                        referenceGroupEntity.getId() + " " +
                        messageService.getLocalizedMessage("messages.has_children_groups")
                );
            }
        }

        //поиск связанных справочников
        List<ReferenceEntity> referenceEntities = referenceRepository.findAll(QReferenceEntity.referenceEntity.group.id.eq(id));
        if(referenceEntities != null && referenceEntities.size() > 0) {
            throw new BusinessException(
                    Errors.REFERENCE_GROUP_HAS_LINKED_REFERENCES,
                    messageService.getLocalizedMessage("messages.reference_group_with_id") + " " +
                    id + " " +
                    messageService.getLocalizedMessage("messages.has_linked_references")
            );
        }

        return referenceGroupEntity;
    }

}
