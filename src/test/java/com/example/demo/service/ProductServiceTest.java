package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.PostAndPutProductPayload;
import com.example.demo.entity.Product;
import com.example.demo.exception.PriceInfoNotExistException;
import com.example.demo.exception.ProductInfoNotExistException;
import com.example.demo.model.ProductTypeEnum;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.ProductChangeHistoryRepository;
import com.example.demo.repository.ProductRepository;

@SpringBootTest
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
    void shouldCreateProduct() throws Exception {
        PostAndPutProductPayload postAndPutProductPayload = 
                        new PostAndPutProductPayload(ProductTypeEnum.APM, "Application", 0.1F, 
                                                     "CORE", new BigDecimal(25000.00));
        Product product = productService.registerNewProduct(postAndPutProductPayload);

        Product findProduct = productRepository.findById(product.getId()).get();

        assertEquals(product.getProductType(), findProduct.getProductType());
        assertEquals(product.getName(), findProduct.getName());
        assertEquals(product.getMinCpu(), findProduct.getMinCpu());
        assertEquals(product.getChargeUnit(), findProduct.getChargeUnit());
        assertEquals(product.getPriceHistories().get(0), findProduct.getPriceHistories().get(0));
    }


    // 상품 등록 정보가 부분적으로 null 인 경우
    @Test
    void shouldReturnProductInfoNotExistException() throws Exception {
        PostAndPutProductPayload postAndPutProductPayload = 
                        new PostAndPutProductPayload(ProductTypeEnum.APM, null, null, 
                                                     "CORE", new BigDecimal(25000.00));
        ProductInfoNotExistException e = assertThrows(ProductInfoNotExistException.class,
                    () -> productService.registerNewProduct(postAndPutProductPayload));//예외가 발생해야 한다.
               
        assertThat(e.getMessage()).isEqualTo("Product info is needed to register new product");
    }
    
    // 상품 가격 정보가 등록되어 있지 않을 경우
    @Test
    void shouldReturnPriceInfoNotExistException() throws Exception {
        PostAndPutProductPayload postAndPutProductPayload = 
                        new PostAndPutProductPayload(ProductTypeEnum.APM, "Application", 0.1F, 
                                                     "CORE", null);
        PriceInfoNotExistException e = assertThrows(PriceInfoNotExistException.class,
                    () -> productService.registerNewProduct(postAndPutProductPayload));//예외가 발생해야 한다.
               
        assertThat(e.getMessage()).isEqualTo("Price info is needed to register new product");
    }
    
}
