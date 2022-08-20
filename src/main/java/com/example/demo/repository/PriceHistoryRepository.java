package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PriceHistory;
public interface PriceHistoryRepository extends  JpaRepository<PriceHistory, Integer>  {
}
