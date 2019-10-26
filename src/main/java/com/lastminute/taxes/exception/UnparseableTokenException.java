package com.lastminute.taxes.exception;

public class UnparseableTokenException extends RuntimeException {

    private static final String MESSAGE = "Token '%s' could not be parsed";

    public UnparseableTokenException(String text) {
        super(String.format(MESSAGE, text));
    }
}
