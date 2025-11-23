package com.lavanderia_colonia.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.dto.ClientDTO;
import com.lavanderia_colonia.api.exception.ResourceNotFoundException;
import com.lavanderia_colonia.api.model.Client;
import com.lavanderia_colonia.api.repository.ClientRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientService() {

    }

    public Page<Client> findAll(@org.springframework.lang.NonNull Pageable pageable, String name) {

        if (name != null && !name.trim().isEmpty()) {
            return clientRepository.findByName(name.trim(), pageable);
        }
        return clientRepository.findAll(pageable);
    }

    public List<Client> findAllActive(Boolean active, String name) {
        return clientRepository.findByActiveAndName(active, name);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    @Transactional
    public Client create(ClientDTO clientDTO) {
        Client client = new Client();

        mapDtoToEntity(clientDTO, client, false);

        client.setActive(true);

        client.setOrders(List.of());

        return clientRepository.save(client);
    }

    @Transactional
    public Client update(Long id, ClientDTO clientDTO) {
        Client client = findById(id);

        mapDtoToEntity(clientDTO, client, true);

        return clientRepository.save(client);
    }

    @Transactional
    public Client toggleActive(Long id) {
        Client client = findById(id);
        client.setActive(!client.isActive());
        return clientRepository.save(client);
    }

    private void mapDtoToEntity(ClientDTO dto, Client entity, boolean partialUpdate) {
        if (!partialUpdate || dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (!partialUpdate || dto.getTelephone() != null) {
            entity.setTelephone(dto.getTelephone());
        }
        if (!partialUpdate || dto.getStreet() != null) {
            entity.setStreet(dto.getStreet());
        }
        if (!partialUpdate || dto.getNumber() != null) {
            entity.setNumber(dto.getNumber());
        }
        if (!partialUpdate || dto.getDistrict() != null) {
            entity.setDistrict(dto.getDistrict());
        }
        if (!partialUpdate || dto.getZipCode() != null) {
            entity.setZipCode(dto.getZipCode());
        }
        if (!partialUpdate || dto.getComplement() != null) {
            entity.setComplement(dto.getComplement());
        }
    }
}
