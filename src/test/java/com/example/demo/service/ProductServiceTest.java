package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.request.ProductRequestPayload;
import com.example.demo.dto.response.ProductResponsePayload;
import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.Product;
import com.example.demo.exception.PriceInfoNotExistException;
import com.example.demo.exception.ProductInfoNotExistException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.ProductTypeEnum;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.ProductChangeHistoryRepository;
import com.example.demo.repository.ProductRepository;

@SpringBootTest
@Transactional
public class ProductServiceTest {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PriceHistoryRepository priceHistoryRepository;
    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;
    
    // 상품 정보 정상적으로 등록이 되어야 한다.
    @Test
    void shouldCreateUpdateAndDeleteProduct() throws Exception {
        ProductRequestPayload postAndPutProductPayload = 
                        new ProductRequestPayload(ProductTypeEnum.APM, "Application", 0.1F, 
                                                     "CORE", new BigDecimal(25000.00));
        ProductResponsePayload productResponsePayload = productService.registerNewProduct(postAndPutProductPayload);
        
        Product findProduct = productRepository.findById(productResponsePayload.getProduct().getId()).get();

        assertEquals(productResponsePayload.getProduct().getProductType(), findProduct.getProductType());
        assertEquals(productResponsePayload.getProduct().getName(), findProduct.getName());
        assertEquals(productResponsePayload.getProduct().getMinCpu(), findProduct.getMinCpu());
        assertEquals(productResponsePayload.getProduct().getChargeUnit(), findProduct.getChargeUnit());
        assertEquals(productResponsePayload.getPriceHistory().getPrice(), findProduct.getPriceHistories().get(0).getPrice());

        // Product 정보를 수정한다.
        findProduct.setMinCpu(0.2F);
        PriceHistory priceHistory = new PriceHistory(new BigDecimal(30000.00));
        findProduct.add(priceHistory);
        Product editedProduct = productRepository.save(findProduct);
        assertEquals(editedProduct.getMinCpu(), 0.2F);
        assertEquals(priceHistoryRepository.findFirstByProductOrderByCreateDateDesc(editedProduct).getPrice(), new BigDecimal(30000.00));

        // 해당 상품을 지운다.
        productRepository.delete(editedProduct);
        ProductNotFoundException e = assertThrows(ProductNotFoundException.class,
                    () -> productService.deleteProduct(editedProduct.getId())); //예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("No product found with this id: " + editedProduct.getId());
    }


    // 상품 등록 정보가 부분적으로 null 인 경우
    @Test
    void shouldReturnProductInfoNotExistException() throws Exception {
        ProductRequestPayload postAndPutProductPayload = 
                        new ProductRequestPayload(ProductTypeEnum.APM, null, null, 
                                                     "CORE", new BigDecimal(25000.00));
        ProductInfoNotExistException e = assertThrows(ProductInfoNotExistException.class,
                    () -> productService.registerNewProduct(postAndPutProductPayload));//예외가 발생해야 한다.
               
        assertThat(e.getMessage()).isEqualTo("Product info is needed to register new product");
    }

    // 상품 가격 정보가 등록되어 있지 않을 경우
    @Test
    void shouldReturnPriceInfoNotExistException() throws Exception {
        ProductRequestPayload postAndPutProductPayload = 
                        new ProductRequestPayload(ProductTypeEnum.APM, "Application", 0.1F, 
                                                     "CORE", null);
        PriceInfoNotExistException e = assertThrows(PriceInfoNotExistException.class,
                    () -> productService.registerNewProduct(postAndPutProductPayload));//예외가 발생해야 한다.
               
        assertThat(e.getMessage()).isEqualTo("Price info is needed to register new product");
    }
    
}
