package com.lavanderia_colonia.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.model.OrderItemColor;
import com.lavanderia_colonia.api.repository.OrderItemColorRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderItemColorService {

    @Autowired
    private OrderItemColorRepository orderItemColorRepository;

    @Transactional
    public List<OrderItemColor> findAll(String color) {
        return orderItemColorRepository.findAllByColor(color);
    }

    @Transactional
    public List<OrderItemColor> findAll() {
        return orderItemColorRepository.findAll();
    }
}
