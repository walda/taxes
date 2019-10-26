package com.lastminute.taxes.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnparseableTokenExceptionTest {

    @Test
    public void when_UnparseableTokenException() {
        assertThat(new UnparseableTokenException("token"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Token 'token' could not be parsed");
    }

}
