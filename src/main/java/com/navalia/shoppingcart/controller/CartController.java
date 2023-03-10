package com.navalia.shoppingcart.controller;

import com.navalia.shoppingcart.dto.request.OrderRequest;
import com.navalia.shoppingcart.exception.InvalidOrderException;
import com.navalia.shoppingcart.exception.ItemNotInCartException;
import com.navalia.shoppingcart.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/cart")
public class CartController {

    private static final String LOGGING_PREFIX = "[CartController] ";

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add_item")
    public ResponseEntity<Object> add(@RequestBody OrderRequest order) {
        try {
            return ResponseEntity.ok().body(cartService.addToCart(order));
        } catch (InvalidOrderException ex) {
            log.error(String.join(" ", LOGGING_PREFIX, ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error(String.join(" ", LOGGING_PREFIX, "An unexpected problem occurred:", ex.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/remove_item")
    public ResponseEntity<Object> remove(@RequestBody OrderRequest order) {
        try {
            return ResponseEntity.ok().body(cartService.removeFromCart(order));
        } catch (InvalidOrderException | ItemNotInCartException ex) {
            log.error(String.join(" ", LOGGING_PREFIX, ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error(String.join(" ", LOGGING_PREFIX, "An unexpected problem occurred:", ex.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/empty_cart")
    public ResponseEntity<Object> emptyCart() {
        try {
            return ResponseEntity.ok().body(cartService.emptyCart());
        } catch (Exception ex) {
            log.error(String.join(" ", LOGGING_PREFIX, "An unexpected problem occurred:", ex.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/close_order")
    public ResponseEntity<Object> closeOrder() {
        try {
            return ResponseEntity.ok().body(cartService.closeOrder());
        } catch (Exception ex) {
            log.error(String.join(" ", LOGGING_PREFIX, "An unexpected problem occurred:", ex.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
