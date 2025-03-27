package com.example.dreamshops.controller;

import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.model.Cart;
import com.example.dreamshops.response.ApiResponse;
import com.example.dreamshops.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{id}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            Cart cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long id) {
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Success", totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
