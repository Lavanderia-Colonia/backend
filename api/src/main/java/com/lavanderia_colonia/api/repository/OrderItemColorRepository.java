package com.lavanderia_colonia.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lavanderia_colonia.api.model.OrderItemColor;

@Repository
public interface OrderItemColorRepository extends JpaRepository<OrderItemColor, Long> {
    public List<OrderItemColor> findAllByColor(String color);
}
