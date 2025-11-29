package com.lavanderia_colonia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.model.OrderStatus;
import com.lavanderia_colonia.api.service.OrderStatusService;

@RestController
@RequestMapping("api/v1/order-statuses")
public class OrderStatusController {

    @Autowired
    OrderStatusService orderStatusService;

    @GetMapping
    public ResponseEntity<List<OrderStatus>> getOrderStatuses() {
        return ResponseEntity.ok(orderStatusService.findAll());
    }

}
