package com.hari.service;

import com.hari.model.Product;
import com.hari.model.User;
import com.hari.model.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
