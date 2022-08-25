package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductChangeHistory;
import com.example.demo.model.OperationEnum;
import com.example.demo.model.ResultEnum;
import com.example.demo.repository.ProductChangeHistoryRepository;

@Aspect
@Component
public class CudApiCallLoggingAspect {

    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;

    @Pointcut("execution(* com.example.demo.controller.ProductController.registerNewProduct(..)) || " + 
              "execution(* com.example.demo.controller.ProductController.editProductInfo(..)) || " +
              "execution(* com.example.demo.controller.ProductController.deleteProduct(..))")
    public void productCUDControllerMethods() {
    }

    @Pointcut("execution(* com.example.demo.service.ProductService.registerNewProduct(..)) || " + 
              "execution(* com.example.demo.service.ProductService.editProductInfo(..)) || " +
              "execution(* com.example.demo.service.ProductService.deleteProduct(..))")
    public void productCUDServiceMethods() {
    }
    
    @AfterReturning(value = "productCUDServiceMethods()", returning = "returnValue")
    public void afterReturningFromProductService(JoinPoint joinPoint, Product returnValue) {
        String methodName = joinPoint.getSignature().getName();
        ProductChangeHistory productChangeHistory = new ProductChangeHistory();

        if("registerNewProduct".equals(methodName)) {
            productChangeHistory = new ProductChangeHistory(returnValue.getId(), returnValue.getName(), returnValue.getProductType(),
                                                            ResultEnum.SUCCESS, OperationEnum.CREATE);
        } else if("editProductInfo".equals(methodName)) {
            productChangeHistory = new ProductChangeHistory(returnValue.getId(), returnValue.getName(), returnValue.getProductType(),
                                                            ResultEnum.SUCCESS, OperationEnum.UPDATE);
        } else if("deleteProduct".equals(methodName)) {
            productChangeHistory = new ProductChangeHistory(returnValue.getId(), returnValue.getName(), returnValue.getProductType(),
                                                            ResultEnum.SUCCESS, OperationEnum.DELETE);
        }
        productChangeHistoryRepository.saveAndFlush(productChangeHistory);
    }
    
    @AfterThrowing(value = "productCUDControllerMethods()", throwing = "exception")
    public void afterExceptionThrownFromProductService (JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        ProductChangeHistory productChangeHistory = new ProductChangeHistory();

        if("registerNewProduct".equals(methodName)) {
            productChangeHistory = new ProductChangeHistory(exception.getMessage(), ResultEnum.FAIL, OperationEnum.CREATE);
        } else if("editProductInfo".equals(methodName)) {
            productChangeHistory = new ProductChangeHistory(exception.getMessage(), ResultEnum.FAIL, OperationEnum.UPDATE);
        } else if("deleteProduct".equals(methodName)) {
            productChangeHistory = new ProductChangeHistory(exception.getMessage(), ResultEnum.FAIL, OperationEnum.DELETE);
        }
        productChangeHistoryRepository.saveAndFlush(productChangeHistory);
    }
}

    