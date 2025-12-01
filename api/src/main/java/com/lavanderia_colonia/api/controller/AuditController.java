package com.lavanderia_colonia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.model.Audit;
import com.lavanderia_colonia.api.service.AuditService;

@RestController
@RequestMapping("/api/v1/audits")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping
    public List<Audit> getRecentAudits() {
        return auditService.getRecentAudits();
    }

}
