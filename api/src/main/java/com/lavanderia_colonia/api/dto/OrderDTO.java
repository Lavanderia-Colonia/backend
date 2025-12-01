package com.lavanderia_colonia.api.dto;

import java.util.List;

import com.lavanderia_colonia.api.enums.OrderType;

import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OrderDTO {

    private Long id;
    private Long clientId;
    private Long statusId;

    @Enumerated
    private OrderType finishType;

    private String finishDeadline;
    private List<OrderItemDTO> items;
    private String observation;

}
