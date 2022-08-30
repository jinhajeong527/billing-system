package com.example.demo.dto.request;

public class BillingRequest {
    Integer productId;
    Integer targetYear;
    Integer targetMonth;

    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public Integer getTargetYear() {
        return targetYear;
    }
    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }
    public Integer getTargetMonth() {
        return targetMonth;
    }
    public void setTargetMonth(Integer targetMonth) {
        this.targetMonth = targetMonth;
    }
    
}
