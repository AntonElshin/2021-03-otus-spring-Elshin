package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import ru.otus.homework.dto.ReferenceReqDTO;
import ru.otus.homework.dto.ReferenceResDTO;

import java.util.List;

public interface ReferenceService {

    Iterable<ReferenceResDTO> findByParams(Predicate predicate, Pageable pageable);

    List<ReferenceResDTO> findByParams(Predicate predicate);

    ReferenceResDTO findById(Long id);

    ReferenceResDTO create(ReferenceReqDTO referenceReqDTO);

    ReferenceResDTO modify(Long id, ReferenceReqDTO referenceReqDTO);

    void delete(Long id);

    List<ReferenceResDTO> findAllReferences(Long groupId);
}
