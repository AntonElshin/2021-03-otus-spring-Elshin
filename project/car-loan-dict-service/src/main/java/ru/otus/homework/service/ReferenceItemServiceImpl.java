package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.RefItemReqDTO;
import ru.otus.homework.dto.RefItemResDTO;
import ru.otus.homework.entity.QReferenceItemEntity;
import ru.otus.homework.entity.ReferenceEntity;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mappers.ReferenceItemMapper;
import ru.otus.homework.repository.ReferenceItemRepository;
import ru.otus.homework.repository.ReferenceRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReferenceItemServiceImpl implements ReferenceItemService {

    private final ReferenceItemRepository referenceItemRepository;
    private final ReferenceRepository referenceRepository;
    private final ReferenceItemMapper referenceItemMapper;
    private final MessageService messageService;

    @Override
    @SneakyThrows
    public Iterable<RefItemResDTO> findByParams(Predicate predicate, Pageable pageable) {
        Iterable<RefItemResDTO> refItemsResDTO = referenceItemRepository.findAll(predicate, pageable).map(referenceItemMapper::toDto);
        return refItemsResDTO;
    }

    @Override
    public List<RefItemResDTO> findByParams(Predicate predicate) {
        List<ReferenceItemEntity> referenceItemEntities = referenceItemRepository.findAll(predicate);
        return referenceItemMapper.toListDto(referenceItemEntities);
    }

    @Override
    @SneakyThrows
    public RefItemResDTO findById(Long id) {

        ReferenceItemEntity referenceItemEntity = referenceItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_ITEM_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_item_not_found_by_id") + " " + id
                ));

        return referenceItemMapper.toDto(referenceItemEntity);
    }

    @Override
    @SneakyThrows
    public RefItemResDTO create(RefItemReqDTO refItemReqDTO) {

        ReferenceItemEntity entity = referenceItemRepository.save(checkAndPrepareReferenceItem(null, refItemReqDTO));
        return referenceItemMapper.toDto(entity);
    }

    @Override
    @SneakyThrows
    public RefItemResDTO modify(Long id, RefItemReqDTO refItemReqDTO) {

        refItemReqDTO.setId(id);

        // обновление элемента справочника
        ReferenceItemEntity entity = referenceItemRepository.save(checkAndPrepareReferenceItem(id, refItemReqDTO));
        return referenceItemMapper.toDto(entity);
    }

    @Override
    @SneakyThrows
    public void delete(Long id) {
        referenceItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_ITEM_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_item_not_found_by_id") + " " + id
                ));

        referenceItemRepository.deleteById(id);
    }

    @Override
    public void deleteAllByReferenceId(Long referenceId) {
        referenceItemRepository.deleteAllByReferenceIdEquals(referenceId);
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<RefItemResDTO> findAllReferenceItems(Long referenceId, String referenceSysName) {

        if(referenceId == null && referenceSysName == null) {
            throw new BusinessException(
                    Errors.REFERENCE_ID_AND_REFERENCE_SYSNAME_IS_NULL,
                    messageService.getLocalizedMessage("messages.one_of_required_param_referenceid_or_refsysname_shouldnt_be_null")
            );
        }

        List<ReferenceItemEntity> items;

        if(referenceSysName != null && !referenceSysName.isEmpty()) {
            items = referenceItemRepository.findAll(QReferenceItemEntity.referenceItemEntity.reference.sysname.equalsIgnoreCase(referenceSysName));
        }
        else {
            items = referenceItemRepository.findAll(QReferenceItemEntity.referenceItemEntity.reference.id.eq(referenceId));
        }

        return referenceItemMapper.toListDto(items);

    }

    @Override
    public ReferenceItemEntity checkReferenceItem(Long refItemId, String refSysName, Errors itemError, String itemErrorStr, Errors referenceError, String referenceErrorStr) {

        Optional<ReferenceEntity> reference = null;

        if(refItemId != null) {

            ReferenceItemEntity item = referenceItemRepository.findById(refItemId).orElseThrow(() -> new BusinessException(itemError, itemErrorStr + " " + refItemId));

            reference = referenceRepository.findById(item.getReference().getId());

            if(reference.isPresent() && !reference.get().getSysname().equalsIgnoreCase(refSysName)) {
                throw new BusinessException(
                        referenceError,
                        referenceErrorStr + " " + refSysName + ", " +
                                messageService.getLocalizedMessage("messages.actual") + " " + reference.get().getSysname()
                );
            }

            return item;

        }

        return null;
    }

    @Override
    public ReferenceItemEntity findByReferenceSysNameAndItemCode(String referenceName, String itemCode) {
        return referenceItemRepository.findByReferenceSysNameAndReferenceItemCodeEquals(referenceName, itemCode);
    }

    private ReferenceItemEntity checkAndPrepareReferenceItem(Long id, RefItemReqDTO refItemReqDTO) {

        ReferenceItemEntity referenceItemEntity = null;

        if(id != null) {
            referenceItemEntity = referenceItemRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            Errors.REFERENCE_ITEM_NOT_FOUND_BY_ID,
                            messageService.getLocalizedMessage("messages.reference_item_not_found_by_id") + " " + id)
                    );


        }

        ReferenceEntity reference = null;

        Long referenceId = null;

        if(refItemReqDTO.getReference() != null) {
            referenceId = refItemReqDTO.getReference().getId();
        }

        if(referenceId == null) {
            throw new BusinessException(
                    Errors.REFERENCE_GROUP_ID_IS_NULL,
                    messageService.getLocalizedMessage("messages.missing_required_param_reference_id"
            ));
        }

        // проверка на допустимость кода элемента справочника
        if(id == null || (id != null && !referenceItemEntity.getCode().equalsIgnoreCase(refItemReqDTO.getCode()))) {

            BooleanExpression fullPredicate = QReferenceItemEntity.referenceItemEntity.reference.id.eq(refItemReqDTO.getReference().getId()).and(
                    QReferenceItemEntity.referenceItemEntity.code.equalsIgnoreCase(refItemReqDTO.getCode())
            );

            Optional<ReferenceItemEntity> foundReferenceItem = referenceItemRepository.findOne(fullPredicate);
            if(foundReferenceItem.isPresent()) {
                throw new BusinessException(
                        Errors.NOT_UNIQUE_REFERENCE_ITEM_CODE,
                        messageService.getLocalizedMessage("messages.not_unique_reference_item_code") + " " + refItemReqDTO.getCode()
                );
            }
        }

        // поиск справочника по идентификатору
        if(referenceId != null) {
            reference = referenceRepository.findById(refItemReqDTO.getReference().getId())
                    .orElseThrow(() -> new BusinessException(
                        Errors.REFERENCE_NOT_FOUND_BY_ID,
                        messageService.getLocalizedMessage("messages.reference_not_found_by_id") + " " + refItemReqDTO.getReference().getId())
            );
        }

        if(id != null) {
            return new ReferenceItemEntity(id, reference, refItemReqDTO.getCode(), refItemReqDTO.getName(), refItemReqDTO.getBrief(), refItemReqDTO.getDescription());
        }
        else {
            return new ReferenceItemEntity(null, reference, refItemReqDTO.getCode(), refItemReqDTO.getName(), refItemReqDTO.getBrief(), refItemReqDTO.getDescription());
        }

    }

}