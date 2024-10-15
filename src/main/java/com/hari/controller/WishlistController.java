package com.hari.controller;



import com.hari.model.Product;
import com.hari.model.User;
import com.hari.model.Wishlist;
import com.hari.service.ProductService;
import com.hari.service.UserService;
import com.hari.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Wishlist wishlist=wishlistService.getWishlistByUserId(user);
        return new ResponseEntity<>(wishlist, HttpStatus.ACCEPTED);

    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Product product=productService.findProductById(productId);
        User user=userService.findUserByJwtToken(jwt);
        Wishlist updateWishlist=wishlistService.addProductToWishlist(user,product);
        return ResponseEntity.ok(updateWishlist);

    }
}
