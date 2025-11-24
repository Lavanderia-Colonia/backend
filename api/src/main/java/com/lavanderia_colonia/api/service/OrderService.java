package com.lavanderia_colonia.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.dto.OrderDTO;
import com.lavanderia_colonia.api.model.Order;
import com.lavanderia_colonia.api.model.OrderItem;
import com.lavanderia_colonia.api.repository.ClientRepository;
import com.lavanderia_colonia.api.repository.OrderItemRepository;
import com.lavanderia_colonia.api.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public Order create(OrderDTO orderDTO) {

        Order order = new Order();

        if (orderDTO.getClientId() == null) {
            throw new RuntimeException("Client id é obrigatório");
        }

        order.setClient(clientRepository.findById(orderDTO.getClientId()).orElse(null));
        order.setFinishDeadline(orderDTO.getFinishDeadline());

        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    BeanUtils.copyProperties(dto, item);
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList());

        List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems);

        order.setOrderItems(savedItems);

        return orderRepository.save(order);
    }

    public Order update(Long id, OrderDTO orderDTO) {

        Order order = findById(id);

        if (order == null) {
            throw new RuntimeException("Order nao encontrado com ID: " + id);
        }

        order.setClient(clientRepository.findById(orderDTO.getClientId()).orElse(null));
        order.setFinishDeadline(orderDTO.getFinishDeadline());

        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    BeanUtils.copyProperties(dto, item);
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

}
