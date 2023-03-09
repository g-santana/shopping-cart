package com.navalia.shoppingcart.service.impl;

import com.navalia.shoppingcart.constant.ItemEnum;
import com.navalia.shoppingcart.constant.OperationEnum;
import com.navalia.shoppingcart.dto.request.OrderRequest;
import com.navalia.shoppingcart.dto.response.ClosedOrderResponse;
import com.navalia.shoppingcart.dto.response.OrderResponse;
import com.navalia.shoppingcart.entity.Cart;
import com.navalia.shoppingcart.entity.Item;
import com.navalia.shoppingcart.exception.InvalidOrderException;
import com.navalia.shoppingcart.exception.ItemNotInCartException;
import com.navalia.shoppingcart.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class CartServiceImpl implements CartService {

    private static final String LOGGING_PREFIX = "[CartServiceImpl] ";
    private final Cart cart = new Cart();

    @Override
    public OrderResponse addToCart(OrderRequest order) throws InvalidOrderException {
        if (validOrder(order)) {
            var itemData = ItemEnum.valueOfId(order.getItemId());
            var optItem = cart.getItems().stream().filter(i -> i.getItemData().getId() == order.getItemId()).findFirst();

            updateCart(itemData, order, OperationEnum.ADD, optItem);

            return OrderResponse.builder()
                    .message("Order placed. Item(s) added to cart.")
                    .build();
        } else {
            log.error(String.join(" ", LOGGING_PREFIX, "Order with invalid data detected:", order.toString()));
            throw new InvalidOrderException("The order received contains invalid data. This item may not exist with this id or amount.");
        }
    }

    @Override
    public OrderResponse removeFromCart(OrderRequest order) throws InvalidOrderException, ItemNotInCartException {
        if (validOrder(order)) {
            var itemData = ItemEnum.valueOfId(order.getItemId());
            var optItem = cart.getItems().stream().filter(i -> i.getItemData().getId() == order.getItemId()).findFirst();

            if (optItem.isPresent()) {
                updateCart(itemData, order, OperationEnum.REMOVE, optItem);

                return OrderResponse.builder()
                        .message("Order placed. Item(s) removed from shopping cart.")
                        .build();
            } else {
                log.error(String.join(" ", LOGGING_PREFIX, "Attempt to delete non existing item from cart detected:", order.toString()));
                throw new ItemNotInCartException("The cart does not contain this item, so it cannot be deleted.");
            }
        } else {
            log.error(String.join(" ", LOGGING_PREFIX, "Order with invalid data detected:", order.toString()));
            throw new InvalidOrderException("The order received contains invalid data. This item may not exist with this id or amount.");
        }
    }

    @Override
    public OrderResponse emptyCart() {
        cart.setItems(new ArrayList<>());
        return OrderResponse.builder()
                .message("Cart is now empty.")
                .build();
    }

    @Override
    public ClosedOrderResponse closeOrder() {
        return ClosedOrderResponse.builder()
                .orderedItems(cart)
                .totalPrice(applyPromotion())
                .build();
    }

    private boolean validOrder(OrderRequest order) {
        if (Objects.isNull(order) || order.getAmount() <= 0) return false;

        var item = ItemEnum.valueOfId(order.getItemId());

        return Objects.nonNull(item);
    }

    private void updateCart(ItemEnum itemData, OrderRequest order, OperationEnum operation, Optional<Item> optItem) {
        var items = cart.getItems();

        if (optItem.isPresent()) {
            var item = optItem.get();
            if (operation.equals(OperationEnum.REMOVE)) {
                if (item.getAmount() >= order.getAmount()) {
                    items.set(items.indexOf(item), Item.builder().itemData(itemData).amount(item.getAmount() - order.getAmount()).build());
                } else {
                    items.set(items.indexOf(item), Item.builder().itemData(itemData).amount(0).build());
                }
            } else {
                items.set(items.indexOf(item), Item.builder().itemData(itemData).amount(item.getAmount() + order.getAmount()).build());
            }
        } else {
            items.add(Item.builder().itemData(itemData).amount(order.getAmount()).build());
        }

        cart.setItems(items);
    }

    private double applyPromotion() {
        var items = cart.getItems();
        int totalAmountOfItemsInCart = items.stream().mapToInt(Item::getAmount).sum();
        double totalPrice = 0;
        Collections.sort(items);

        if (totalAmountOfItemsInCart >= 3) {
            int discountsToGive = totalAmountOfItemsInCart / 3;
            double discountValue = 0;
            int pos = 0;

            while (discountsToGive > 0) {
                var discountItem = items.get(pos);

                if (discountItem.getAmount() >= discountsToGive) {
                    discountValue += discountItem.getItemData().getPrice() * discountsToGive;
                    discountsToGive = 0;
                } else {
                    discountValue += discountItem.getItemData().getPrice() * discountItem.getAmount();
                    pos += 1;
                    discountsToGive -= discountItem.getAmount();
                }
            }

            totalPrice = items.stream().mapToDouble(i -> i.getItemData().getPrice() * i.getAmount()).sum() - discountValue;
        } else {
            totalPrice = items.stream().mapToDouble(i -> i.getItemData().getPrice() * i.getAmount()).sum();
        }

        return totalPrice;
    }
}
