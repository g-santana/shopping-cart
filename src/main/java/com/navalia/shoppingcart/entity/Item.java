package com.navalia.shoppingcart.entity;

import com.navalia.shoppingcart.constant.ItemEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Comparable<Item> {
    private int amount;
    private ItemEnum itemData;

    @Override
    public int compareTo(Item item) {
        double anotherPrice = item.getItemData().getPrice();

        return Double.compare(itemData.getPrice(), anotherPrice);
    }
}
