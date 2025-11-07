package com.lavanderia_colonia.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lavanderia_colonia.api.model.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

}
