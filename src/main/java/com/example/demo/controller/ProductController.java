package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.example.demo.dto.ProductPayload;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping(path = "api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<?> registerNewProduct(@RequestBody ProductPayload productPayload) {
        Product product = productService.registerNewProduct(productPayload);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductPayload> productPayloads = productService.getAllProducts();
        if(productPayloads == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productPayloads, HttpStatus.OK);
    }

    @GetMapping("/paging") // 파라미터에 page, size, sort를 키로 정해서 값 보내주면 되고, sort의 경우는 name,desc 와 같이 보내줄 수 있다. 
    public ResponseEntity<?> getProducts(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<ProductPayload> pagedProductPayloads = productService.getProducts(pageable);
        if(pagedProductPayloads == null) 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(pagedProductPayloads, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "productId") Integer productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<?> editProductInfo(@PathVariable(value = "productId") Integer productId, @RequestBody ProductPayload productPayload) {
        Product product = productService.editProductInfo(productId, productPayload);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

}
