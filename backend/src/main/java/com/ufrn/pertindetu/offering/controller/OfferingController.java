package com.ufrn.pertindetu.offering.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.pertindetu.base.controller.GenericController;
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
}