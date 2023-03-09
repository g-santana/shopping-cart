package com.navalia.shoppingcart.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class OrderRequest {
    private int itemId;

    private int amount;
}
