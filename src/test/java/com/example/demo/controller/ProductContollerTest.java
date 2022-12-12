package com.example.demo.controller;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.dto.response.ProductResponsePayload;
import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.Product;
import com.example.demo.exception.advice.ControllerExceptionHandler;
import com.example.demo.model.ProductTypeEnum;
import com.example.demo.service.ProductService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductContollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    ControllerExceptionHandler controllerExceptionHandler;

    @Test
    public void shouldGetAllProducts() throws Exception {
        List<ProductResponsePayload> list = new ArrayList<>();
        Product product = new Product("Test Jeong Application", 0.1f, "CPU", ProductTypeEnum.APM);
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setId(1);
        priceHistory.setCreateDate(null);
        priceHistory.setPrice(new BigDecimal("25000"));
        priceHistory.setProduct(product);
        ProductResponsePayload productResponsePayload = new ProductResponsePayload(product, priceHistory);
        list.add(productResponsePayload);

        when(productService.getAllProducts()).thenReturn(list);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/product")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()) // 200
                .andReturn();

    }

}
