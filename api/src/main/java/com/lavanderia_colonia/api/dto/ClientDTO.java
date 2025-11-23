package com.lavanderia_colonia.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTO {
    String name;
    String telephone;
    String street;
    String number;
    String district;
    String zipCode;
    String complement;
}
