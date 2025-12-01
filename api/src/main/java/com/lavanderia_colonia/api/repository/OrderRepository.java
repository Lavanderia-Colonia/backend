package com.lavanderia_colonia.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lavanderia_colonia.api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE CAST(o.id AS string) LIKE %:id%")
    Page<Order> searchById(@Param("id") String id, Pageable pageable);

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        LEFT JOIN FETCH o.orderItems items
        LEFT JOIN FETCH items.color
        WHERE o.id = :id
    """)
    Order findByIdWithItems(@Param("id") Long id);

}