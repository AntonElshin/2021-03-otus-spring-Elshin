package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import ru.otus.homework.dto.RefItemReqDTO;
import ru.otus.homework.dto.RefItemResDTO;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.exceptions.Errors;

import java.util.List;

public interface ReferenceItemService {

    Iterable<RefItemResDTO> findByParams(Predicate predicate, Pageable pageable);

    List<RefItemResDTO> findByParams(Predicate predicate);

    RefItemResDTO findById(Long id);

    RefItemResDTO create(RefItemReqDTO refItemReqDTO);

    RefItemResDTO modify(Long id, RefItemReqDTO refItemReqDTO);

    void delete(Long id);

    void deleteAllByReferenceId(Long referenceId);

    List<RefItemResDTO> findAllReferenceItems(Long referenceId, String refSysName);

    ReferenceItemEntity checkReferenceItem(Long refItemId, String refSysName, Errors itemError, String itemErrorStr, Errors referenceError, String referenceErrorStr);

    ReferenceItemEntity findByReferenceSysNameAndItemCode(String referenceName, String itemCode);
}
