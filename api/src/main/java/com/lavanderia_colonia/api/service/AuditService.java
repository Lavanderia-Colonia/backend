package com.lavanderia_colonia.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.dto.AuditDTO;
import com.lavanderia_colonia.api.model.Audit;
import com.lavanderia_colonia.api.pattern.creational.singleton.AuditLogger;
import com.lavanderia_colonia.api.repository.AuditRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuditService {

    private final AuditRepository auditRepository;
    private final AuditLogger auditLogger;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
        this.auditLogger = AuditLogger.getInstance();
    }

    @PostConstruct
    public void init() {
        auditLogger.setRepository(auditRepository);
    }

    public Audit create(AuditDTO auditDTO) {
        Audit audit = new Audit();
        audit.setDescription(auditDTO.getDescription());
        audit.setChangeDate(auditDTO.getChangeDate());
        return auditRepository.save(audit);
    }

    public List<Audit> getRecentAudits() {
        return auditRepository.findTop10ByOrderByChangeDateDesc();
    }

}