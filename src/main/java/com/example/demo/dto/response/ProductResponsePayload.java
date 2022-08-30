package com.example.demo.dto.response;

import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.Product;

public class ProductResponsePayload {
    private Product product;
    private PriceHistory priceHistory;

    public ProductResponsePayload() {
    }

    public ProductResponsePayload(Product product, PriceHistory priceHistory) {
        this.product = product;
        this.priceHistory = priceHistory;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PriceHistory getPriceHistory() {
        return priceHistory;
    }
    
    public void setPriceHistory(PriceHistory priceHistory) {
        this.priceHistory = priceHistory;
    }
   
}
