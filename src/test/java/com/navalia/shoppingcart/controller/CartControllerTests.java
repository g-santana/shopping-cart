package com.navalia.shoppingcart.controller;

import com.navalia.shoppingcart.dto.request.OrderRequest;
import com.navalia.shoppingcart.dto.response.OrderResponse;
import com.navalia.shoppingcart.exception.InvalidOrderException;
import com.navalia.shoppingcart.exception.ItemNotInCartException;
import com.navalia.shoppingcart.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartControllerTests {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setup() {
        cartController = new CartController(cartService);
    }

    @Test
    void testAddMethodOkResponse() {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        var response = cartController.add(orderRequest);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testAddMethodBadRequestResponse() throws InvalidOrderException {
        var orderRequest = OrderRequest.builder().itemId(5).amount(2).build();

        Mockito.when(cartService.addToCart(ArgumentMatchers.any())).thenThrow(InvalidOrderException.class);
        var response = cartController.add(orderRequest);
        Assertions.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testAddMethodInternalServerErrorResponse() throws Exception {
        Mockito.when(cartService.addToCart(ArgumentMatchers.any())).thenThrow(NullPointerException.class);
        var response = cartController.add(null);
        Assertions.assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testRemoveMethodOkResponse() {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        cartController.add(orderRequest);
        var response = cartController.remove(orderRequest);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRemoveMethodBadRequestResponseInvalidOrder() throws InvalidOrderException, ItemNotInCartException {
        var orderRequest = OrderRequest.builder().itemId(5).amount(2).build();

        Mockito.when(cartService.removeFromCart(ArgumentMatchers.any())).thenThrow(InvalidOrderException.class);
        var response = cartController.remove(orderRequest);
        Assertions.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testRemoveMethodBadRequestResponseItemNotInCart() throws InvalidOrderException, ItemNotInCartException {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        Mockito.when(cartService.removeFromCart(ArgumentMatchers.any())).thenThrow(InvalidOrderException.class);
        var response = cartController.remove(orderRequest);
        Assertions.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testRemoveMethodInternalServerErrorResponse() throws Exception {
        Mockito.when(cartService.removeFromCart(ArgumentMatchers.any())).thenThrow(NullPointerException.class);
        var response = cartController.remove(null);
        Assertions.assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testEmptyCartMethodOkResponse() {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        cartController.add(orderRequest);
        var response = cartController.emptyCart();
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testEmptyCartMethodInternalServerErrorResponse() throws Exception {
        Mockito.when(cartService.emptyCart()).thenThrow(NullPointerException.class);
        var response = cartController.emptyCart();
        Assertions.assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testCloseMethodOkResponse() {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        cartController.add(orderRequest);
        var response = cartController.close();
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCloseMethodInternalServerErrorResponse() throws Exception {
        Mockito.when(cartService.closeOrder()).thenThrow(NullPointerException.class);
        var response = cartController.close();
        Assertions.assertEquals(500, response.getStatusCodeValue());
    }
}
