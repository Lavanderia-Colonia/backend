package com.lavanderia_colonia.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.dto.OrderItemDTO;
import com.lavanderia_colonia.api.model.OrderItem;
import com.lavanderia_colonia.api.repository.OrderItemColorRepository;
import com.lavanderia_colonia.api.repository.OrderItemRepository;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemColorRepository orderItemColorRepository;

    public OrderItem create(OrderItemDTO orderItemDTO) {

        OrderItem orderItem = new OrderItem();

        orderItem.setBrand(orderItemDTO.getBrand());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setColor(orderItemColorRepository.findById(orderItemDTO.getColorId()).orElse(null));
        orderItem.setPrice(orderItemDTO.getUnitPrice());
        orderItem.setObservation(orderItemDTO.getObservation());

        return orderItemRepository.save(orderItem);

    }
}
