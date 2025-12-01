package com.lavanderia_colonia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.dto.ClientDTO;
import com.lavanderia_colonia.api.model.Client;
import com.lavanderia_colonia.api.model.Order;
import com.lavanderia_colonia.api.service.ClientService;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<Client>> findAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @RequestParam(required = false) String name, @RequestParam(required = false) Boolean active) {
        Page<Client> clients = clientService.findAll(pageable, name);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<Order>> getHistory(@PathVariable Long id) {

        List<Order> orders = clientService.getHistory(id);

        orders.forEach(order -> order.getOrderItems());
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/active")
    public ResponseEntity<List<Client>> findAllActive(
            @RequestParam(required = false) String name) {

        List<Client> clients = clientService.findAllActive(true, name);
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody ClientDTO clientDTO) {
        Client client = clientService.create(clientDTO);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        Client client = clientService.update(id, clientDTO);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<Client> toggleActive(@PathVariable Long id) {
        Client client = clientService.toggleActive(id);
        return ResponseEntity.ok(client);
    }
}
