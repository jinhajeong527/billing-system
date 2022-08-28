package com.example.demo.dto;

import java.util.List;

public class ProductListPayload {
    private List<ProductPayload> productListPayload;
    private Integer totalPages;
    private Integer currentPage;

    public ProductListPayload() {
    }

    public ProductListPayload(List<ProductPayload> productListPayload, Integer totalPages, Integer currentPage) {
        this.productListPayload = productListPayload;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public List<ProductPayload> getProductListPayload() {
        return productListPayload;
    }

    public void setProductListPayload(List<ProductPayload> productListPayload) {
        this.productListPayload = productListPayload;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    
}
