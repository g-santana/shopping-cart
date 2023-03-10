package com.navalia.shoppingcart.service.impl;

import com.navalia.shoppingcart.constant.ItemEnum;
import com.navalia.shoppingcart.dto.request.OrderRequest;
import com.navalia.shoppingcart.dto.response.ClosedOrderResponse;
import com.navalia.shoppingcart.dto.response.OrderResponse;
import com.navalia.shoppingcart.entity.Cart;
import com.navalia.shoppingcart.entity.Item;
import com.navalia.shoppingcart.exception.InvalidOrderException;
import com.navalia.shoppingcart.exception.ItemNotInCartException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTests {

    @InjectMocks
    private CartServiceImpl cartServiceImpl;

    private OrderResponse getExpectedResponseForSuccessfulAddToCart() {
        return OrderResponse.builder()
                .message("Order placed. Item(s) added to cart.")
                .build();
    }

    private OrderResponse getExpectedResponseForSuccessfulRemoveFromCart() {
        return OrderResponse.builder()
                .message("Order placed. Item(s) removed from shopping cart.")
                .build();
    }

    private OrderResponse getExpectedResponseForSuccessfulEmptyCart() {
        return OrderResponse.builder()
                .message("Cart is now empty.")
                .build();
    }

    private ClosedOrderResponse getExpectedResponseForSuccessfulCloseOrderExampleCase1() {
        var cart = new Cart();
        var items = new ArrayList<Item>();

        items.add(Item.builder().itemData(ItemEnum.T_SHIRT).amount(3).build());

        cart.setItems(items);

        return ClosedOrderResponse.builder()
                .orderedItems(cart)
                .totalPrice(25.98)
                .build();
    }

    private ClosedOrderResponse getExpectedResponseForSuccessfulCloseOrderExampleCase2() {
        var cart = new Cart();
        var items = new ArrayList<Item>();

        items.add(Item.builder().itemData(ItemEnum.T_SHIRT).amount(2).build());
        items.add(Item.builder().itemData(ItemEnum.JEANS).amount(2).build());

        cart.setItems(items);

        return ClosedOrderResponse.builder()
                .orderedItems(cart)
                .totalPrice(62.99)
                .build();
    }

    private ClosedOrderResponse getExpectedResponseForSuccessfulCloseOrderExampleCase3() {
        var cart = new Cart();
        var items = new ArrayList<Item>();

        items.add(Item.builder().itemData(ItemEnum.T_SHIRT).amount(1).build());
        items.add(Item.builder().itemData(ItemEnum.JEANS).amount(2).build());
        items.add(Item.builder().itemData(ItemEnum.DRESS).amount(3).build());

        cart.setItems(items);

        return ClosedOrderResponse.builder()
                .orderedItems(cart)
                .totalPrice(91.3)
                .build();
    }

    private ClosedOrderResponse getExpectedResponseForSuccessfulCloseOrderExampleCase4() {
        var cart = new Cart();
        var items = new ArrayList<Item>();

        items.add(Item.builder().itemData(ItemEnum.T_SHIRT).amount(3).build());
        items.add(Item.builder().itemData(ItemEnum.JEANS).amount(2).build());
        items.add(Item.builder().itemData(ItemEnum.DRESS).amount(4).build());

        cart.setItems(items);

        return ClosedOrderResponse.builder()
                .orderedItems(cart)
                .totalPrice(132.6)
                .build();
    }

    @Test
    void testSuccessfulRemoveFromCartWithNoItemsAdded() throws InvalidOrderException {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        var response = cartServiceImpl.addToCart(orderRequest);
        Assertions.assertEquals(getExpectedResponseForSuccessfulAddToCart().getMessage(), response.getMessage());
    }

    @Test
    void testSuccessfulAddToCartWithItemsAdded() throws InvalidOrderException {
        var orderRequest1 = OrderRequest.builder().itemId(1).amount(3).build();
        var orderRequest2 = OrderRequest.builder().itemId(1).amount(1).build();

        cartServiceImpl.addToCart(orderRequest1);
        var response = cartServiceImpl.addToCart(orderRequest2);
        Assertions.assertEquals(getExpectedResponseForSuccessfulAddToCart().getMessage(), response.getMessage());
    }

    @Test
    void testFailAddToCartWithInvalidData() {
        var orderRequest = OrderRequest.builder().itemId(5).amount(2).build();

        Assertions.assertThrows(InvalidOrderException.class, () -> cartServiceImpl.addToCart(orderRequest));
    }

    @Test
    void testSuccessfulRemoveFromCart() throws InvalidOrderException, ItemNotInCartException {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        cartServiceImpl.addToCart(orderRequest);
        var response = cartServiceImpl.removeFromCart(orderRequest);
        Assertions.assertEquals(getExpectedResponseForSuccessfulRemoveFromCart().getMessage(), response.getMessage());
    }

    @Test
    void testSuccessfulRemoveFromCartSetToZero() throws InvalidOrderException, ItemNotInCartException {
        var orderRequestAddOne = OrderRequest.builder().itemId(1).amount(1).build();
        var orderRequestRemoveFive = OrderRequest.builder().itemId(1).amount(5).build();


        cartServiceImpl.addToCart(orderRequestAddOne);
        var response = cartServiceImpl.removeFromCart(orderRequestRemoveFive);
        Assertions.assertEquals(getExpectedResponseForSuccessfulRemoveFromCart().getMessage(), response.getMessage());
    }

    @Test
    void testFailRemoveFromCartWithInvalidData() {
        var orderRequest = OrderRequest.builder().itemId(5).amount(2).build();

        Assertions.assertThrows(InvalidOrderException.class, () -> cartServiceImpl.addToCart(orderRequest));
    }

    @Test
    void testFailRemoveFromCartWithNoItemInCart() {
        var orderRequest = OrderRequest.builder().itemId(1).amount(3).build();

        Assertions.assertThrows(ItemNotInCartException.class, () -> cartServiceImpl.removeFromCart(orderRequest));
    }

    @Test
    void testSuccessfulEmptyCart() throws InvalidOrderException {
        var orderRequest = OrderRequest.builder().itemId(1).amount(1).build();

        cartServiceImpl.addToCart(orderRequest);

        var response = cartServiceImpl.emptyCart();

        Assertions.assertEquals(getExpectedResponseForSuccessfulEmptyCart().getMessage(), response.getMessage());
    }

    @Test
    void testSuccessfulCloseOrderCase1() throws InvalidOrderException {
        var orderRequest = OrderRequest.builder().itemId(1).amount(3).build();

        cartServiceImpl.addToCart(orderRequest);

        var response = cartServiceImpl.closeOrder();

        Assertions.assertEquals(getExpectedResponseForSuccessfulCloseOrderExampleCase1().getTotalPrice(), response.getTotalPrice());
    }

    @Test
    void testSuccessfulCloseOrderCase2() throws InvalidOrderException {
        var orderRequest1 = OrderRequest.builder().itemId(1).amount(2).build();
        var orderRequest2 = OrderRequest.builder().itemId(2).amount(2).build();

        cartServiceImpl.addToCart(orderRequest1);
        cartServiceImpl.addToCart(orderRequest2);

        var response = cartServiceImpl.closeOrder();

        Assertions.assertEquals(getExpectedResponseForSuccessfulCloseOrderExampleCase2().getTotalPrice(), response.getTotalPrice());
    }

    @Test
    void testSuccessfulCloseOrderCase3() throws InvalidOrderException {
        var orderRequest1 = OrderRequest.builder().itemId(1).amount(1).build();
        var orderRequest2 = OrderRequest.builder().itemId(2).amount(2).build();
        var orderRequest3 = OrderRequest.builder().itemId(3).amount(3).build();

        cartServiceImpl.addToCart(orderRequest1);
        cartServiceImpl.addToCart(orderRequest2);
        cartServiceImpl.addToCart(orderRequest3);

        var response = cartServiceImpl.closeOrder();

        Assertions.assertEquals(getExpectedResponseForSuccessfulCloseOrderExampleCase3().getTotalPrice(), response.getTotalPrice());
    }

    @Test
    void testSuccessfulCloseOrderCase4() throws InvalidOrderException {
        var orderRequest1 = OrderRequest.builder().itemId(1).amount(3).build();
        var orderRequest2 = OrderRequest.builder().itemId(2).amount(2).build();
        var orderRequest3 = OrderRequest.builder().itemId(3).amount(4).build();

        cartServiceImpl.addToCart(orderRequest1);
        cartServiceImpl.addToCart(orderRequest2);
        cartServiceImpl.addToCart(orderRequest3);

        var response = cartServiceImpl.closeOrder();

        Assertions.assertEquals(getExpectedResponseForSuccessfulCloseOrderExampleCase4().getTotalPrice(), response.getTotalPrice());
    }
}
