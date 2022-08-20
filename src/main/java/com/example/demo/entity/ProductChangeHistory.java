package com.example.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.model.OperationEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Table(name = "product_change_history")
public class ProductChangeHistory {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ProductId", nullable = false)
    private Integer productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Operation", nullable = false)
    private OperationEnum operation;

    @Column(name = "CreateDate", nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    public ProductChangeHistory() {
    }

    public ProductChangeHistory(Integer productId, OperationEnum operation) {
        this.productId = productId;
        this.operation = operation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public OperationEnum getOperation() {
        return operation;
    }

    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    

    
    
}
