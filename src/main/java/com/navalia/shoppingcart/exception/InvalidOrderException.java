package com.navalia.shoppingcart.exception;

public class InvalidOrderException extends Exception {

    private static final long serialVersionUID = 1530121797171485043L;

    public InvalidOrderException(String message) {
        super(message);
    }
}
