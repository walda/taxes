package com.lastminute.taxes.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Product '%s' not found";

    public ProductNotFoundException(String productName) {
        super(String.format(MESSAGE, productName));
    }

}
