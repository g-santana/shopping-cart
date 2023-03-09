package com.navalia.shoppingcart.service;

import com.navalia.shoppingcart.dto.request.OrderRequest;
import com.navalia.shoppingcart.dto.response.ClosedOrderResponse;
import com.navalia.shoppingcart.dto.response.OrderResponse;
import com.navalia.shoppingcart.exception.InvalidOrderException;
import com.navalia.shoppingcart.exception.ItemNotInCartException;

public interface CartService {

    OrderResponse addToCart(OrderRequest order) throws InvalidOrderException;

    OrderResponse removeFromCart(OrderRequest order) throws InvalidOrderException, ItemNotInCartException;

    OrderResponse emptyCart();

    ClosedOrderResponse closeOrder();

}
