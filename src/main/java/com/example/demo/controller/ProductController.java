package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.PaginationRequestPayload;
import com.example.demo.dto.request.ProductRequestPayload;
import com.example.demo.dto.response.ProductResponsePayload;
import com.example.demo.dto.response.ProductsResonsePayload;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;


@RestController
@RequestMapping(path = "api/product")
public class ProductController {
    // Exception Handling은 @RestControllerAdvice ControllerExceptionHandler클래스에서 처리한다.
    @Autowired
    ProductService productService;
    
    @PostMapping
    public ResponseEntity<?> registerNewProduct(@RequestBody ProductRequestPayload productPayload) {
        ProductResponsePayload productResponsePayload = productService.registerNewProduct(productPayload);
        return new ResponseEntity<>(productResponsePayload, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponsePayload> productPayloads = productService.getAllProducts();
        if(productPayloads == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productPayloads, HttpStatus.OK);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable(value = "productId") Integer productId) {
        ProductResponsePayload productPayload = productService.getProductById(productId);
        if(productPayload == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productPayload, HttpStatus.OK);
    }

    @PostMapping("/paging") // 파라미터에 page, size, sort를 키로 정해서 값 보내주면 되고, sort의 경우는 name,desc 와 같이 보내줄 수 있다. 
    public ResponseEntity<?> getProducts(@RequestBody PaginationRequestPayload paginationPayload) {
        ProductsResonsePayload productListPayload = productService.getProducts(paginationPayload);
        if(productListPayload == null) 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productListPayload, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "productId") Integer productId) {
        Product product = productService.deleteProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<?> editProductInfo(@PathVariable(value = "productId") Integer productId, @RequestBody ProductRequestPayload productPayload) {
        Product product = productService.editProductInfo(productId, productPayload);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

}
