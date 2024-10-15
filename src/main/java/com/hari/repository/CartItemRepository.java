package com.hari.repository;

import com.hari.model.Cart;
import com.hari.model.CartItem;
import com.hari.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
