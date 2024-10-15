package com.hari.service;

import com.hari.exceptions.ProductException;
import com.hari.model.Product;
import com.hari.model.Seller;
import com.hari.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest request, Seller seller);

    public void deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product request) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> searchProduct(String query);

    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );
    List<Product> getProductBySellerId(Long sellerId);


}
