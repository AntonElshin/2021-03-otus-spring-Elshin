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
import ru.otus.homework.api.ReferenceApi;
import ru.otus.homework.dto.ReferenceReqDTO;
import ru.otus.homework.dto.ReferenceResDTO;
import ru.otus.homework.entity.ReferenceEntity;
import ru.otus.homework.service.ReferenceService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReferenceController implements ReferenceApi {

    private final ReferenceService referenceService;

    @Override
    public ResponseEntity<List<ReferenceResDTO>> getAllReferencesByGroupId(Long groupId) {
        return ResponseEntity.ok(referenceService.findAllReferences(groupId));
    }

    @ApiOperation(value = "Получение списка справочников по параметам")
    @GetMapping(value = "/reference/references", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<ReferenceResDTO> getReferences (
            @QuerydslPredicate(root = ReferenceEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return referenceService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<ReferenceResDTO> getReferenceById(Long refId) {
        return ResponseEntity.ok(referenceService.findById(refId));
    }

    @Override
    public ResponseEntity<ReferenceResDTO> createReference(ReferenceReqDTO referenceReqDTO) {
        return ResponseEntity.ok(referenceService.create(referenceReqDTO));
    }

    @Override
    public ResponseEntity<ReferenceResDTO> modifyReference(Long refId, ReferenceReqDTO referenceReqDTO) {
        return ResponseEntity.ok(referenceService.modify(refId, referenceReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteReference(Long refId) {
        referenceService.delete(refId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
