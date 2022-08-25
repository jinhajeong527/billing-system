package com.example.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.model.OperationEnum;
import com.example.demo.model.ResultEnum;

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

    @Column(name = "ProductId", nullable = true)
    private Integer productId;

    @Column(name = "ProductName", nullable = true)
    private String productName;

    @Column(name = "ProductType", nullable = true)
    private String productType;

    @Column(name = "ErrorMessage", nullable = true)
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "Result", nullable = false)
    private ResultEnum result;

    @Enumerated(EnumType.STRING) // enum 이름을 DB에 저장한다.
    @Column(name = "Operation", nullable = false)
    private OperationEnum operation;

    @Column(name = "CreateDate", nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    public ProductChangeHistory() {
    }

    public ProductChangeHistory(String errorMessage, ResultEnum result, OperationEnum operation) {
        this.errorMessage = errorMessage;
        this.result = result;
        this.operation = operation;
    }
    
    public ProductChangeHistory(Integer productId, String productName, String productType, ResultEnum result,
            OperationEnum operation) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.result = result;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResultEnum getResult() {
        return result;
    }

    public void setResult(ResultEnum result) {
        this.result = result;
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
