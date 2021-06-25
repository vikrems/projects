package com.ecommerce.cart.api.controller;

import com.ecommerce.cart.service.cart.CartService;
import com.ecommerce.cart.service.dto.RequestDto;
import com.ecommerce.cart.service.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("shopping-cart/{cartId}")
public class ShoppingCartController {

    private final CartService cartService;

    @PutMapping
    public ResponseEntity<Void> upsertCart(@PathVariable("cartId") String cartId,
                                           @RequestBody @Valid RequestDto requestDto) {
        cartService.upsertCart(cartId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> patchCart(@PathVariable("cartId") String cartId,
                                          @RequestBody @Valid RequestDto requestDto) {
        cartService.addToCart(cartId, requestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getCart(@PathVariable("cartId") String cartId) {
        ResponseDto responseDto = cartService.retrieveCart(cartId);
        return ResponseEntity.ok(responseDto);
    }
}
