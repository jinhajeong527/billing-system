package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.dto.request.PaginationRequestPayload;
import com.example.demo.dto.request.ProductRequestPayload;
import com.example.demo.dto.response.ProductResponsePayload;
import com.example.demo.dto.response.ProductsResonsePayload;
import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.Product;
import com.example.demo.exception.PriceInfoNotExistException;
import com.example.demo.exception.ProductInfoNotExistException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.ProductChangeHistoryRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
    // Product CUD Operation 성공적으로 수행 후 CudApiCallLoggingAspect AOP 클래스에서 @AfterReturning을 통해 호출 로그 기록되도록 함
    @Autowired
    ProductRepository productRepository;

    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Product registerNewProduct(ProductRequestPayload productPayload) {
        // 입력되지 않은 상품 정보가 있는지 체크한다.
        if(checkIfThereAnyNullProductInfo(productPayload))
            throw new ProductInfoNotExistException("Product info is needed to register new product");
        LocalDateTime createDate = LocalDateTime.now();
        Product product = new Product(productPayload.getName(), productPayload.getMinCpu(), 
                                      productPayload.getChargeUnit(), productPayload.getProductType(),
                                      createDate, createDate);
        BigDecimal price = productPayload.getPrice();
        if(price == null)
            throw new PriceInfoNotExistException("Price info is needed to register new product");
        PriceHistory priceHistory = new PriceHistory(price);
        product.add(priceHistory);
        product = productRepository.save(product);
        return product;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ProductResponsePayload> getAllProducts() {
        List<Product> products = productRepository.findAll();
        
        if(products.isEmpty()) return null;

        List<ProductResponsePayload> productPayloads = getProductPayloadList(products);
        return productPayloads;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ProductResponsePayload getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: " + productId));
        PriceHistory priceHistory = priceHistoryRepository.findFirstByProductOrderByCreateDateDesc(product);
        ProductResponsePayload productPayload = new ProductResponsePayload(product, priceHistory);
        return productPayload;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ProductsResonsePayload getProducts(PaginationRequestPayload paginationPayload) {
        Pageable pageable = makePageRequest(paginationPayload);
        Page<Product> pagedProducts = productRepository.findAll(pageable);
        if(pagedProducts.getContent().isEmpty()) return null;

        // List<Product>를 PriceHistory 엔티티 추가하여 List<ProductPayload>로 만들어 준 후에 Page<ProductPayload>로 다시 바꿔준다.
        List<Product> products = pagedProducts.getContent();
        List<ProductResponsePayload> productPayloads = getProductPayloadList(products);
        ProductsResonsePayload productListPayload = new ProductsResonsePayload(productPayloads, pagedProducts.getTotalPages(), pagedProducts.getNumber());

        return productListPayload;
    }

    @Transactional
    public Product deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: " + productId));
        productRepository.delete(product);
        return product;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Product editProductInfo(Integer productId, ProductRequestPayload productPayload) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("No product found with this id: " + productId));
        
        if(productPayload.getName() != null) product.setName(productPayload.getName());
        if(productPayload.getProductType() != null) product.setProductType(productPayload.getProductType());
        if(productPayload.getMinCpu() != null) product.setMinCpu(productPayload.getMinCpu());
        if(productPayload.getChargeUnit() != null) product.setChargeUnit(productPayload.getChargeUnit());

        if(productPayload.getPrice() != null) {
            PriceHistory priceHistory = new PriceHistory(productPayload.getPrice());
            product.add(priceHistory);
            priceHistoryRepository.save(priceHistory);
        }

        product.setUpdateDate(LocalDateTime.now());
        product = productRepository.save(product);
        return product;
    }

    // Product의 List<PriceHistory>에서 가장 최근의 가격 정보 추가해서 List<ProductPayLoad>로 리턴하는 메서드
    public List<ProductResponsePayload> getProductPayloadList(List<Product> products) {
        List<ProductResponsePayload> productPayloads = new ArrayList<>();
        for(Product product : products) {
            // 해당 프로덕트가 가진 PriceHistory 중 가장 최근에 등록된 정보를 가져온다.
            PriceHistory priceHistory = priceHistoryRepository.findFirstByProductOrderByCreateDateDesc(product);
            ProductResponsePayload productPayload = new ProductResponsePayload(product, priceHistory);
            productPayloads.add(productPayload);
        }
        return productPayloads;
    }

    private boolean checkIfThereAnyNullProductInfo(ProductRequestPayload productPayload) {
        return (productPayload.getName() == null || productPayload.getMinCpu() == null || 
                productPayload.getChargeUnit() == null || productPayload.getProductType() == null);
    }

    private Pageable makePageRequest(PaginationRequestPayload paginationPayload) {
        Sort sort = null;
        if(paginationPayload.getSort() != null) {
            if("desc".equals(paginationPayload.getOrder()))
                sort = Sort.by(paginationPayload.getSort()).descending();
            else
                sort = Sort.by(paginationPayload.getSort());
        }
        Pageable pageble = PageRequest.of(paginationPayload.getPage(), paginationPayload.getSize(), sort);
        return pageble;
    }
    
}
