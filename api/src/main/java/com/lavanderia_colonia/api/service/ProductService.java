package com.lavanderia_colonia.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.model.Product;
import com.lavanderia_colonia.api.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll(String code) {
        if (code == null)
            return productRepository.findAll();

        return productRepository.findAllByCode(code);
    }

}
