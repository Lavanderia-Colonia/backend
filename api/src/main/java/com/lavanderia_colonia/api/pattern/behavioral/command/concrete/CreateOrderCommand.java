package com.lavanderia_colonia.api.pattern.behavioral.command.concrete;

import com.lavanderia_colonia.api.dto.OrderDTO;
import com.lavanderia_colonia.api.pattern.behavioral.command.core.Command;
import com.lavanderia_colonia.api.service.OrderService;

abstract class CreateOrderCommand implements Command {
    private final OrderDTO orderDTO;
    private OrderService orderService;

    public CreateOrderCommand(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    @Override
    public void execute() {
        orderService.create(orderDTO);
    }

    @Override
    public void undo() {
        orderService.delete(orderDTO.getId());
    }

    @Override
    public String getDescription() {
        return "Criar pedido ";
    }
}
