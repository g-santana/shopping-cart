package com.navalia.shoppingcart.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemEnum {
    T_SHIRT(1, "T-shirt", 12.99),
    JEANS(2,  "Jeans", 25.00),
    DRESS(3,  "Dress", 20.65);

    private final int id;
    private final String name;
    private final double price;

    public static ItemEnum valueOfId(int id) {
        for (ItemEnum i : values()) {
            if (i.id == id) {
                return i;
            }
        }
        return null;
    }
}
