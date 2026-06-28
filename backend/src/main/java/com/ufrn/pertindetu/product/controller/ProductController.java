package com.ufrn.pertindetu.product.controller;

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
import com.ufrn.pertindetu.product.dto.ProductDTO;
import com.ufrn.pertindetu.product.model.Product;
import com.ufrn.pertindetu.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController
        extends GenericController<Product, ProductDTO, ProductService> {

    public ProductController(ProductService service) {
        super(service);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<PageImpl<EntityDTO>>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String neighborhood,
            @ParameterObject Pageable pageable) {

        Page<ProductDTO> page =
                service.search(category, neighborhood, pageable);

        PageImpl<EntityDTO> body = new PageImpl<>(
                page.getContent().stream().map(ProductDTO::toResponse).toList(),
                pageable, page.getTotalElements());

        return ResponseEntity.ok(new ApiResponseDTO<>(true, body, null));
    }
}