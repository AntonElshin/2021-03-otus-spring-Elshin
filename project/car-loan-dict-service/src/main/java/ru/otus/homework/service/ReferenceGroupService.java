package ru.otus.homework.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import ru.otus.homework.dto.RefGroupReqDTO;
import ru.otus.homework.dto.RefGroupResDTO;

import java.util.List;

public interface ReferenceGroupService {

    Iterable<RefGroupResDTO> findByParams(Predicate predicate, Pageable pageable);

    List<RefGroupResDTO> findByParams(Predicate predicate);

    RefGroupResDTO findById(Long id);

    RefGroupResDTO create(RefGroupReqDTO refGroupReqDTO);

    RefGroupResDTO modify(Long id, RefGroupReqDTO refGroupReqDTO);

    void delete(Long id);

    List<RefGroupResDTO> findAllReferenceGroups();

}
