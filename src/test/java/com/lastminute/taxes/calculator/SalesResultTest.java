package com.lastminute.taxes.calculator;

import com.lastminute.taxes.engine.ProductTaxes;
import com.lastminute.taxes.tokenizer.Product;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesResultTest {

    @Test
    public void when_toString() {
        var toString = new SalesResult(
                Arrays.asList(
                        Pair.of(new ProductBill(Product.builder().imported(false).name("name").price(1.0).quantity(1).build(),
                                new ProductTaxes("name", 1.0, 2.0)), 1.05),
                        Pair.of(new ProductBill(Product.builder().imported(true).name("name2").price(2.0).quantity(1).build(),
                                new ProductTaxes("name2", 3.0, 4.0)), 2.15)
                ),0.2, 3.2).toString();

        assertThat(toString)
            .isNotNull()
            .isEqualTo("1 name: 1,05\n1 IMPORTED name2: 2,15\nSales Taxes: 0,20\nTotal: 3,20\n");
    }

    @Test
    public void when_toString_Empty_SalesResult() {
        assertThat(new SalesResult().toString())
                .isNotNull()
                .isEqualTo("Sales Taxes: 0,00\nTotal: 0,00\n");
    }
}
