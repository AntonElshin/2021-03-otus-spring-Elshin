package ru.otus.homework.controller;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.api.ReferenceGroupApi;
import ru.otus.homework.dto.RefGroupReqDTO;
import ru.otus.homework.dto.RefGroupResDTO;
import ru.otus.homework.entity.ReferenceGroupEntity;
import ru.otus.homework.service.ReferenceGroupService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReferenceGroupController implements ReferenceGroupApi {

    private final ReferenceGroupService referenceGroupService;

    @Override
    public ResponseEntity<List<RefGroupResDTO>> getAllReferenceGroups() {
        return ResponseEntity.ok(referenceGroupService.findAllReferenceGroups());
    }

    @ApiOperation(value = "Получение списка групп справочников по параметам")
    @GetMapping(value = "/reference-group/refgroups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<RefGroupResDTO> getGroups(
            @QuerydslPredicate(root = ReferenceGroupEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return referenceGroupService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<RefGroupResDTO> getReferenceGroupById(Long refGroupId) {
        return ResponseEntity.ok(referenceGroupService.findById(refGroupId));
    }

    @Override
    public ResponseEntity<RefGroupResDTO> createReferenceGroup(
            RefGroupReqDTO referenceGroupDTO) {
        return ResponseEntity.ok(referenceGroupService.create(referenceGroupDTO));
    }

    @Override
    public ResponseEntity<RefGroupResDTO> modifyReferenceGroup(
            Long refGroupId,
            RefGroupReqDTO refGroupReqDTO) {
        return ResponseEntity.ok(referenceGroupService.modify(refGroupId, refGroupReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteReferenceGroup(Long refGroupId) {
        referenceGroupService.delete(refGroupId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
