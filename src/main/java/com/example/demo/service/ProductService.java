package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductPayload;
import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductChangeHistory;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.OperationEnum;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.ProductChangeHistoryRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;

    @Transactional
    public Product registerNewProduct(ProductPayload productPayload) {
        Product product = productPayload.getProduct();
        PriceHistory priceHistory = productPayload.getPriceHistory();
        product.add(priceHistory);
        product = productRepository.save(product);
        priceHistoryRepository.save(priceHistory);
        saveProductInfoChangeLog(product, OperationEnum.CREATE);
        return product;
    }
    @Transactional
    public List<ProductPayload> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductPayload> productPayloads = new ArrayList<>();
        
        for(Product product : products) {
            ProductPayload productPayload = new ProductPayload();
            productPayload.setProduct(product);
            PriceHistory priceHistory = priceHistoryRepository.findFirstByProductOrderByCreateDateDesc(product);
            productPayload.setPriceHistory(priceHistory);
            productPayloads.add(productPayload);
        }
        
        return productPayloads;
    }

    public Page<Product> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products;
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: "+ productId));
        productRepository.delete(product);
        saveProductInfoChangeLog(product, OperationEnum.DELETE);
    }

    @Transactional
    public Product editProductInfo(Integer productId, ProductPayload productPayload) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: "+ productId));

                product.setProductType(productPayload.getProduct().getProductType());
                product.setMinCpu(productPayload.getProduct().getMinCpu());
                product.setChargeUnit(productPayload.getProduct().getChargeUnit());
                product.setName(productPayload.getProduct().getName());

        PriceHistory priceHistory = productPayload.getPriceHistory();

        if(priceHistory != null) 
            product.add(productPayload.getPriceHistory());
        
        product = productRepository.save(product);
        priceHistoryRepository.save(priceHistory);
        saveProductInfoChangeLog(product, OperationEnum.UPDATE);
        return product;
    }

    private void saveProductInfoChangeLog(Product product, OperationEnum operationEnum) {
        ProductChangeHistory productChangeHistory = new ProductChangeHistory(product.getId(), operationEnum);
        productChangeHistoryRepository.save(productChangeHistory);
    }

    

    
}
