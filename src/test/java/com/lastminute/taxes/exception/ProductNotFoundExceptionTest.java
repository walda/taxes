package com.lastminute.taxes.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductNotFoundExceptionTest {

    @Test
    public void when_ProductNotFoundException() {
        assertThat(new ProductNotFoundException("product"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Product 'product' not found");
    }

}
