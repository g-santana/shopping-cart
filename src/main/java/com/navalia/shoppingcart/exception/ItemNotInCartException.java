package com.navalia.shoppingcart.exception;

public class ItemNotInCartException extends Exception {

    private static final long serialVersionUID = 4026619913730295897L;

    public ItemNotInCartException(String message) {
        super(message);
    }
}
