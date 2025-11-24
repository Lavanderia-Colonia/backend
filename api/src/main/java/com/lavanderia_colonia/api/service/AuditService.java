package com.lavanderia_colonia.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.model.Audit;
import com.lavanderia_colonia.api.repository.AuditRepository;

import jakarta.transaction.Transactional;

@Service
public class AuditService {
    @Autowired
    private AuditRepository auditRepository;

    public Page<Audit> get(Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }

        return auditRepository.findAll(pageable);
    }

    @Transactional
    public void create(Audit audit) {
        auditRepository.save(audit);
    }
}
