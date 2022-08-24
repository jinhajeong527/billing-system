package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductPayload;
import com.example.demo.entity.ProductChangeHistory;
import com.example.demo.model.OperationEnum;
import com.example.demo.repository.ProductChangeHistoryRepository;

@Aspect
@Component
public class CudApiCallLoggingAspect {

    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;

    @Pointcut("execution(* com.example.demo.controller.ProductController.registerNewProduct(..)) || " + 
              "execution(* com.example.demo.controller.ProductController.editProductInfo(..)) || " +
              "execution(* com.example.demo.controller.ProductController.deleteProduct(..))")
    public void productCUDMethods() {
    }
    
    @After("productCUDMethods()")
    public void afterCallingRegisterProductService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object arg = joinPoint.getArgs()[0];

        ProductChangeHistory productChangeHistory = new ProductChangeHistory();

        if("registerNewProduct".equals(methodName)) {
            ProductPayload productPayload = (ProductPayload) arg;
            productChangeHistory = new ProductChangeHistory(productPayload.getProduct().getName(), 
                                                                                 productPayload.getProduct().getProductType(),
                                                                                 OperationEnum.CREATE);
        } else if("editProductInfo".equals(methodName)) {
            Integer productId = (Integer) arg;
            productChangeHistory = new ProductChangeHistory(productId, OperationEnum.UPDATE);
        } else if("deleteProduct".equals(methodName)) {
            Integer productId = (Integer) arg;
            productChangeHistory = new ProductChangeHistory(productId, OperationEnum.DELETE);
        }
        productChangeHistoryRepository.saveAndFlush(productChangeHistory);
    }
}

    