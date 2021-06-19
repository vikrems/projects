package com.ecommerce.cart.api.controller;

import com.ecommerce.cart.service.dto.RequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("shopping-cart/{cartId}")
public class ShoppingCartController {


    @PutMapping("product/{productId}")
    public ResponseEntity<Void> addToCart(@PathVariable("cartId") String cartId,
                                          @RequestBody @Valid RequestDto requestDto) {

        return ResponseEntity.ok().build();
    }
}
