package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.demo.model.ProductTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

// 상품
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    // 최소 단위
    @Column(name = "MinCpu", nullable = true)
    private Float minCpu;

    // 단위
    @Column(name = "ChargeUnit", nullable = true)
    private String chargeUnit;
    
    // 상품 유형(Application, Cloud, BSM, etc..)
    @Enumerated(EnumType.STRING)
    @Column(name = "ProductType", nullable = true)
    private ProductTypeEnum productType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product")
    @JsonIgnore
    private List<PriceHistory> priceHistories;

    @Column(name = "CreateDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

    @Column(name = "UpdateDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;

    public Product() {
    }
    
    public Product(String name, Float minCpu, String chargeUnit, ProductTypeEnum productType) {
        this.name = name;
        this.minCpu = minCpu;
        this.chargeUnit = chargeUnit;
        this.productType = productType;
    }
    
    public Product(String name, Float minCpu, String chargeUnit, ProductTypeEnum productType, LocalDateTime createDate,
            LocalDateTime updateDate) {
        this.name = name;
        this.minCpu = minCpu;
        this.chargeUnit = chargeUnit;
        this.productType = productType;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getMinCpu() {
        return minCpu;
    }

    public void setMinCpu(Float minCpu) {
        this.minCpu = minCpu;
    }

    public String getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(String chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public List<PriceHistory> getPriceHistories() {
        return priceHistories;
    }

    public void setPriceHistories(List<PriceHistory> priceHistories) {
        this.priceHistories = priceHistories;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public void add(PriceHistory priceHistory) {
        if(priceHistories == null) {
            priceHistories = new ArrayList<>();
        }
        priceHistories.add(priceHistory);
        priceHistory.setProduct(this);
    }
    
}
