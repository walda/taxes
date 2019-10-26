package com.lastminute.taxes.tokenizer;

import com.lastminute.taxes.exception.UnparseableTokenException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TokenizerServiceTest {

    @Test
    public void when_parseToken_Product_Token_Is_Parsed() {
        assertThat(new TokenizerService().parseToken("1 TOKEN 10.00"))
            .isEqualTo(Product.builder()
                    .quantity(1)
                    .imported(false)
                    .name("TOKEN")
                    .price(10.00)
                    .build());
    }

    @Test
    public void when_parseToken_Imported_Product_Token_Is_Parsed() {
        assertThat(new TokenizerService().parseToken("1 IMPORTED TOKEN 10.00"))
                .isEqualTo(Product.builder()
                        .quantity(1)
                        .imported(true)
                        .name("TOKEN")
                        .price(10.00)
                        .build());
    }

    @Test
    public void when_parseToken_If_Quantity_Is_Invalid_Exception_Is_Raised() {

        Throwable t = catchThrowable(() -> new TokenizerService().parseToken("1a IMPORTED TOKEN 10.00"));

        assertThat(t)
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Token '1a IMPORTED TOKEN 10.00' could not be parsed");

    }

    @Test
    public void when_parseToken_If_Imported_Is_Invalid_Exception_Is_Raised() {

        Throwable t = catchThrowable(() -> new TokenizerService().parseToken("1 IMPORT TOKEN 10.00"));

        assertThat(t)
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Token '1 IMPORT TOKEN 10.00' could not be parsed");

    }

    @Test
    public void when_parseToken_If_Name_Is_Invalid_Exception_Is_Raised() {

        Throwable t = catchThrowable(() -> new TokenizerService().parseToken("1 IMPORTED TOKEN@ 10.00"));

        assertThat(t)
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Token '1 IMPORTED TOKEN@ 10.00' could not be parsed");

    }

    @Test
    public void when_parseToken_If_Price_Is_Invalid_Exception_Is_Raised() {

        Throwable t = catchThrowable(() -> new TokenizerService().parseToken("1 IMPORTED TOKEN a10.00"));

        assertThat(t)
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Token '1 IMPORTED TOKEN a10.00' could not be parsed");

    }
}
