package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductPayload;
import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.Product;
import com.example.demo.exception.PriceInfoNotExistException;
import com.example.demo.exception.ProductNotFoundException;
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
        if(priceHistory == null)
            throw new PriceInfoNotExistException("상품 등록을 위해서는 가격 정보도 입력해야 합니다.");
        product.add(priceHistory);
        product = productRepository.save(product);
        priceHistoryRepository.save(priceHistory);
        return product;
    }

    @Transactional
    public List<ProductPayload> getAllProducts() {
        List<Product> products = productRepository.findAll();
        
        if(products.isEmpty()) return null;

        List<ProductPayload> productPayloads = getProductPayloadList(products);
        return productPayloads;
    }

    @Transactional
    public Page<ProductPayload> getProducts(Pageable pageable) {
        Page<Product> pagedProducts = productRepository.findAll(pageable);

        if(pagedProducts.getContent().isEmpty()) return null;

        // List<Product>를 PriceHistory 엔티티 추가하여 List<ProductPayload>로 만들어 준 후에 Page<ProductPayload>로 다시 바꿔준다.
        List<Product> products = pagedProducts.getContent();
        List<ProductPayload> productPayloads = getProductPayloadList(products);
        Page<ProductPayload> pagedProductPayloads = new PageImpl<>(productPayloads, pagedProducts.getPageable(), pagedProducts.getTotalPages());
        
        return pagedProductPayloads;
    }

    @Transactional
    public Product deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: " + productId));
        productRepository.delete(product);
        return product;
    }

    @Transactional
    public Product editProductInfo(Integer productId, ProductPayload productPayload) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: " + productId));

        product.setProductType(productPayload.getProduct().getProductType());
        product.setMinCpu(productPayload.getProduct().getMinCpu());
        product.setChargeUnit(productPayload.getProduct().getChargeUnit());
        product.setName(productPayload.getProduct().getName());

        PriceHistory priceHistory = productPayload.getPriceHistory();
        
        if(priceHistory != null) {
            product.add(productPayload.getPriceHistory());
            priceHistoryRepository.save(priceHistory);
        }
        product = productRepository.save(product);
        return product;
    }

    // List<Product>에 PriceHistory에서 가장 최근의 가격 정보 추가해서 List<ProductPayLoad>로 리턴하는 메서드
    private List<ProductPayload> getProductPayloadList(List<Product> products) {
        List<ProductPayload> productPayloads = new ArrayList<>();

        for(Product product : products) {
            ProductPayload productPayload = new ProductPayload();
            productPayload.setProduct(product);
            // 해당 프로덕트가 가진 PriceHistory 중 가장 최근에 등록된 정보를 가져온다.
            PriceHistory priceHistory = priceHistoryRepository.findFirstByProductOrderByCreateDateDesc(product);
            productPayload.setPriceHistory(priceHistory);
            productPayloads.add(productPayload);
        }
        return productPayloads;
    }

    

    
}
