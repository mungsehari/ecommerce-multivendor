package com.hari.controller;

import com.hari.exceptions.ProductException;
import com.hari.model.Product;
import com.hari.model.Seller;
import com.hari.request.CreateProductRequest;
import com.hari.service.ProductService;
import com.hari.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/products")
public class SellerProductController {
    private final ProductService productService;

    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Seller seller=sellerService.getSellerProfile(jwt);

        List<Product> products=productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        Seller seller=sellerService.getSellerProfile(jwt);
        Product product=productService.createProduct(request,seller);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws ProductException {
        try {
            productService.deleteProduct(productId);
           return new ResponseEntity<>(HttpStatus.OK);
        }catch (ProductException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product request
    ) throws ProductException {

            Product product=productService.updateProduct(productId,request);
            return new ResponseEntity<>(product,HttpStatus.OK);


    }

}
