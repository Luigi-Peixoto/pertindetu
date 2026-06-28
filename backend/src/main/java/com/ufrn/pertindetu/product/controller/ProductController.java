package com.ufrn.pertindetu.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.pertindetu.base.controller.GenericController;
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
}