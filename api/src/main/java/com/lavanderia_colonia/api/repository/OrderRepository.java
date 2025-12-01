package com.lavanderia_colonia.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lavanderia_colonia.api.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCodeContainingIgnoreCase(String code, Pageable pageable);

}
