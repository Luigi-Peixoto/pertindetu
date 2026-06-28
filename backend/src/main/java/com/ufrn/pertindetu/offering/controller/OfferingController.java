package com.ufrn.pertindetu.offering.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.pertindetu.base.controller.GenericController;
import com.ufrn.pertindetu.base.dto.ApiResponseDTO;
import com.ufrn.pertindetu.base.dto.EntityDTO;
import com.ufrn.pertindetu.offering.dto.OfferingDTO;
import com.ufrn.pertindetu.offering.model.Offering;
import com.ufrn.pertindetu.offering.service.OfferingService;

@RestController
@RequestMapping("/api/offerings")
public class OfferingController
        extends GenericController<Offering, OfferingDTO, OfferingService> {

    public OfferingController(OfferingService service) {
        super(service);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<PageImpl<EntityDTO>>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String neighborhood,
            @ParameterObject Pageable pageable) {

        Page<OfferingDTO> page =
                service.search(category, neighborhood, pageable);

        PageImpl<EntityDTO> body = new PageImpl<>(
                page.getContent().stream().map(OfferingDTO::toResponse).toList(),
                pageable, page.getTotalElements());

        return ResponseEntity.ok(new ApiResponseDTO<>(true, body, null));
    }
}