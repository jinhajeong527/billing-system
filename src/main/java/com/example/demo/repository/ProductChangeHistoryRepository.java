package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductChangeHistory;

public interface ProductChangeHistoryRepository extends JpaRepository<ProductChangeHistory, Integer> {
}
