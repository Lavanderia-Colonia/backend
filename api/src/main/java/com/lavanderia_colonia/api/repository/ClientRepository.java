package com.lavanderia_colonia.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lavanderia_colonia.api.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lavanderia_colonia.api.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Page<Client> findByName(String name, Pageable pageable);

    public List<Client> findByActiveAndName(boolean active, String name);

    @Query("SELECT o FROM Order o WHERE o.client.id = :clientId AND CAST(o.id AS string) LIKE %:code%")
    List<Order> findOrdersByClientAndCode(
        @Param("clientId") Long clientId,
        @Param("code") String code
    );
}
