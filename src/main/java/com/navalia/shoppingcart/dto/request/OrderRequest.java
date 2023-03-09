package com.navalia.shoppingcart.dto.request;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int itemId;

    private int amount;
}
