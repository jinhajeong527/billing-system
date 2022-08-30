package com.example.demo.dto.response;

import java.util.List;

public class ProductsResonsePayload {
    private List<ProductResponsePayload> productListPayload;
    private Integer totalPages;
    private Integer currentPage;

    public ProductsResonsePayload() {
    }

    public ProductsResonsePayload(List<ProductResponsePayload> productListPayload, Integer totalPages, Integer currentPage) {
        this.productListPayload = productListPayload;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public List<ProductResponsePayload> getProductListPayload() {
        return productListPayload;
    }

    public void setProductListPayload(List<ProductResponsePayload> productListPayload) {
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
