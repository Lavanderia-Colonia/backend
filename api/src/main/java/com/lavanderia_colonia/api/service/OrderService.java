package com.lavanderia_colonia.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.dto.OrderDTO;
import com.lavanderia_colonia.api.dto.OrderItemDTO;
import com.lavanderia_colonia.api.model.Order;
import com.lavanderia_colonia.api.model.OrderItem;
import com.lavanderia_colonia.api.model.OrderStatus;
import com.lavanderia_colonia.api.pattern.creational.singleton.AuditLogger;
import com.lavanderia_colonia.api.repository.ClientRepository;
import com.lavanderia_colonia.api.repository.OrderItemColorRepository;
import com.lavanderia_colonia.api.repository.OrderItemRepository;
import com.lavanderia_colonia.api.repository.OrderRepository;
import com.lavanderia_colonia.api.repository.OrderStatusRepository;
import com.lavanderia_colonia.api.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderItemColorRepository orderItemColorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private final AuditLogger auditLogger = AuditLogger.getInstance();

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Page<Order> findAll(String code, Pageable pageable) {
        if (code != null && !code.isBlank()) {
            return orderRepository.findByIdContainingIgnoreCase(code, pageable);
        }

        return orderRepository.findAll(pageable);
    }

    @Transactional
    public Order create(OrderDTO orderDTO) {

        Order order = new Order();

        var client = clientRepository.findById(orderDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client não encontrado"));

        order.setClient(client);
        order.setFinishDeadline(orderDTO.getFinishDeadline());
        order.setFinishType(orderDTO.getFinishType());
        OrderStatus initialStatus = orderStatusRepository.findByName("Em Aberto");
        order.setStatus(initialStatus);

        List<OrderItem> items = new ArrayList<>();

        for (var dto : orderDTO.getItems()) {

            OrderItem item = new OrderItem();

            item.setBrand(dto.getBrand());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getUnitPrice());
            item.setObservation(dto.getObservation());

            var color = orderItemColorRepository.findById(dto.getColorId())
                    .orElseThrow(() -> new RuntimeException("Color não encontrada: " + dto.getColorId()));

            var product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product não encontrado: " + dto.getProductId()));

            item.setColor(color);
            item.setProduct(product);
            item.setOrder(order);

            items.add(item);
        }

        order.setOrderItems(items);

        Order savedOrder = orderRepository.save(order);

        auditLogger.log("Criou o pedido: " + savedOrder.getId());

        return savedOrder;
    }

    @Transactional
    public Order finishOrder(Long id) {

        Order order = findById(id);

        if (order == null) {
            throw new RuntimeException("Order nao encontrado com ID: " + id);
        }

        order.setStatus(orderStatusRepository.findByName("Pago"));

        order.setDeliveryDate(LocalDateTime.now());

        auditLogger.log("Finalizou o pedido: " + order.getId());

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long id) {

        Order order = findById(id);

        if (order == null) {
            throw new RuntimeException("Order nao encontrado com ID: " + id);
        }

        order.setStatus(orderStatusRepository.findByName("Cancelado"));

        order.setDeliveryDate(LocalDateTime.now());

        auditLogger.log("Cancelou o pedido: " + order.getId());

        return orderRepository.save(order);

    }

    public Order update(Long id, OrderDTO orderDTO) {

        Order order = findById(id);

        if (order == null) {
            throw new RuntimeException("Order não encontrado com ID: " + id);
        }

        order.setClient(clientRepository.findById(orderDTO.getClientId()).orElse(null));
        order.setFinishDeadline(orderDTO.getFinishDeadline());

        List<Long> updatedItemIds = orderDTO.getItems().stream()
                .map(OrderItemDTO::getId)
                .filter(itemId -> itemId != null)
                .collect(Collectors.toList());

        List<OrderItem> updatedOrderItems = new ArrayList<>();

        for (OrderItemDTO dto : orderDTO.getItems()) {
            if (dto.getId() != null) {
                OrderItem existingItem = orderItemRepository.findById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("OrderItem não encontrado com ID: " + dto.getId()));

                existingItem.setBrand(dto.getBrand());
                existingItem.setQuantity(dto.getQuantity());
                existingItem.setPrice(dto.getUnitPrice());
                existingItem.setObservation(dto.getObservation());

                var color = orderItemColorRepository.findById(dto.getColorId())
                        .orElseThrow(() -> new RuntimeException("Color não encontrada: " + dto.getColorId()));

                var product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product não encontrado: " + dto.getProductId()));

                existingItem.setColor(color);
                existingItem.setProduct(product);

                updatedOrderItems.add(existingItem);

            } else {
                OrderItem newItem = new OrderItem();
                newItem.setBrand(dto.getBrand());
                newItem.setQuantity(dto.getQuantity());
                newItem.setPrice(dto.getUnitPrice());
                newItem.setObservation(dto.getObservation());

                var color = orderItemColorRepository.findById(dto.getColorId())
                        .orElseThrow(() -> new RuntimeException("Color não encontrada: " + dto.getColorId()));

                var product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product não encontrado: " + dto.getProductId()));

                newItem.setColor(color);
                newItem.setProduct(product);
                newItem.setOrder(order);

                updatedOrderItems.add(newItem);
            }
        }

        orderItemRepository.saveAll(updatedOrderItems);

        order.getOrderItems().stream()
                .filter(item -> !updatedItemIds.contains(item.getId()))
                .forEach(orderItemRepository::delete);

        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

}
