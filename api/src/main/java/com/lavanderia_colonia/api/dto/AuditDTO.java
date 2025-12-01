package com.lavanderia_colonia.api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AuditDTO {
    String description;
    LocalDate changeDate;
}
