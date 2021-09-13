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
import ru.otus.homework.api.ReferenceItemApi;
import ru.otus.homework.dto.RefItemReqDTO;
import ru.otus.homework.dto.RefItemResDTO;
import ru.otus.homework.entity.ReferenceItemEntity;
import ru.otus.homework.service.ReferenceItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReferenceItemController implements ReferenceItemApi {

    private final ReferenceItemService referenceItemService;

    @Override
    public ResponseEntity<List<RefItemResDTO>> getAllReferenceItems(
            Long referenceId,
            String referenceSysName
    ) {
        return ResponseEntity.ok(referenceItemService.findAllReferenceItems(referenceId, referenceSysName));
    }

    @ApiOperation(value = "Получение списка элементов справочников по параметам")
    @GetMapping(value = "/reference-item/refitems", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<RefItemResDTO> getItems (
            @QuerydslPredicate(root = ReferenceItemEntity.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {
        return referenceItemService.findByParams(predicate, pageable);
    }

    @Override
    public ResponseEntity<RefItemResDTO> getReferenceItemById(Long refItemId) {
        return ResponseEntity.ok(referenceItemService.findById(refItemId));
    }

    @Override
    public ResponseEntity<RefItemResDTO> createReferenceItem(
            RefItemReqDTO refItemReqDTO) {
        return ResponseEntity.ok(referenceItemService.create(refItemReqDTO));
    }

    @Override
    public ResponseEntity<RefItemResDTO> modifyReferenceItem(
            Long refItemId,
            RefItemReqDTO refItemReqDTO
    ) {
        return ResponseEntity.ok(referenceItemService.modify(refItemId, refItemReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteReferenceItem(Long refItemId) {
        referenceItemService.delete(refItemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
