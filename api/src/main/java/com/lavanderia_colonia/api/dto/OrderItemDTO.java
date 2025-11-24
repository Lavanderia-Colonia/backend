package com.lavanderia_colonia.api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDTO {

    private Long productId;
    private BigDecimal unitPrice;
    private String brand;
    private Long colorId;
    private Integer quantity;
    private String observation;

}
