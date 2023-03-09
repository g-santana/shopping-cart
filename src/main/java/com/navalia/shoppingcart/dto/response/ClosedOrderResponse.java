package com.navalia.shoppingcart.dto.response;

import com.navalia.shoppingcart.entity.Cart;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosedOrderResponse {
    private Cart orderedItems;
    private double totalPrice;
}
