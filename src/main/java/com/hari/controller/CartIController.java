package com.hari.controller;

import com.hari.model.Cart;
import com.hari.model.CartItem;
import com.hari.model.Product;
import com.hari.model.User;
import com.hari.request.AddItemRequest;
import com.hari.response.ApiResponse;
import com.hari.service.CartItemService;
import com.hari.service.CartService;
import com.hari.service.ProductService;
import com.hari.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartIController {
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    private ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findUserCart(user);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }


    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest request,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Product product=productService.findProductById(request.getProductId());
        CartItem item=cartService.addCartItem(
                user,
                product,
                request.getSize(),
                request.getQuantity()
        );
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Item added to cart successfully");
        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);

        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Item removed from cart successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem=null;
        if (cartItem.getQuantity()>0){
            updatedCartItem=cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        }
        return  new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
    }



}
