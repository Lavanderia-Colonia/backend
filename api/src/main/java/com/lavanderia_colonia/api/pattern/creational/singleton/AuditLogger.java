package com.lavanderia_colonia.api.pattern.creational.singleton;

import java.time.LocalDate;

import com.lavanderia_colonia.api.enums.AuditAction;
import com.lavanderia_colonia.api.model.Audit;
import com.lavanderia_colonia.api.repository.AuditRepository;

public class AuditLogger {

    private static volatile AuditLogger instance;
    private AuditRepository auditRepository;

    private AuditLogger() {
    }

    public static AuditLogger getInstance() {
        if (instance == null) {
            synchronized (AuditLogger.class) {
                if (instance == null) {
                    instance = new AuditLogger();
                }
            }
        }
        return instance;
    }

    public void setRepository(AuditRepository repository) {
        this.auditRepository = repository;
    }

    public void log(String description) {
        saveToDatabase(description, LocalDate.now());
    }

    public void log(String description, LocalDate changeDate) {
        saveToDatabase(description, changeDate);
    }

    public void log(AuditAction action, String details) {
        String description = String.format("%s: %s", action.getDescription(), details);
        saveToDatabase(description, LocalDate.now());
    }

    public void logOrder(AuditAction action, Long orderId, String additionalInfo) {
        String description = String.format("%s - Pedido #%d%s",
                action.getDescription(),
                orderId,
                additionalInfo != null ? " - " + additionalInfo : "");
        saveToDatabase(description, LocalDate.now());
    }

    public void logClient(AuditAction action, Integer clientId, String clientName) {
        String description = String.format("%s - Cliente #%d (%s)",
                action.getDescription(),
                clientId,
                clientName);
        saveToDatabase(description, LocalDate.now());
    }

    private void saveToDatabase(String description, LocalDate changeDate) {
        if (auditRepository != null) {
            Audit audit = new Audit();
            audit.setDescription(description);
            audit.setChangeDate(changeDate);
            auditRepository.save(audit);
        }
    }
}