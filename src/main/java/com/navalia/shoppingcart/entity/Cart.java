package com.navalia.shoppingcart.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart {

    private List<Item> items;

    public Cart() {
       this.items = new ArrayList<>();
    }
}
