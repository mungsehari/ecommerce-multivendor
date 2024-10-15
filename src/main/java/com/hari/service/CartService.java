package com.hari.service;

import com.hari.model.Cart;
import com.hari.model.CartItem;
import com.hari.model.Product;
import com.hari.model.User;

public interface CartService {

    public CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    );

    public Cart findUserCart(User user);

}
