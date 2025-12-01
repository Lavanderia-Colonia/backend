package com.lavanderia_colonia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.model.OrderItemColor;
import com.lavanderia_colonia.api.service.OrderItemColorService;

@RestController
@RequestMapping("/api/v1/order-item-colors")
public class OrderItemColorController {

    @Autowired
    private OrderItemColorService orderItemColorService;

    @GetMapping
    public ResponseEntity<List<OrderItemColor>> findAll(@RequestParam(required = false) String color) {
        if (color == null)
            return ResponseEntity.ok(orderItemColorService.findAll());

        return ResponseEntity.ok(orderItemColorService.findAll(color));
    }
}
