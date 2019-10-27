package com.lastminute.taxes.calculator;

import com.lastminute.taxes.engine.ProductTaxes;
import com.lastminute.taxes.engine.TaxesService;
import com.lastminute.taxes.tokenizer.Product;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CalculatorServiceTest {

    @Mock
    private TaxesService taxesService;
    @InjectMocks
    private CalculatorService calculatorService;

    @Test
    public void when_calculateTaxes_SaleResult_Is_Returned() {

        when(taxesService.getProductTaxes("name")).thenReturn(new ProductTaxes("name", 1.0, 2.0));
        when(taxesService.getProductTaxes("name2")).thenReturn(new ProductTaxes("name2", 3.0, 4.0));

        var salesResult = calculatorService.calculateTaxes(
                Arrays.asList(
                        Product.builder().imported(false).name("name").price(1.0).quantity(1).build(),
                        Product.builder().imported(true).name("name2").price(2.0).quantity(2).build()));

        assertThat(salesResult)
                    .isNotNull()
                    .isEqualTo(new SalesResult(
                            Arrays.asList(
                                Pair.of(new ProductBill(Product.builder().imported(false).name("name").price(1.0).quantity(1).build(),
                                        new ProductTaxes("name", 1.0, 2.0)), 1.05),
                                Pair.of(new ProductBill(Product.builder().imported(true).name("name2").price(2.0).quantity(2).build(),
                                        new ProductTaxes("name2", 3.0, 4.0)), 4.30)
                            ),0.2, 5.35));

        verify(taxesService).getProductTaxes("name");
        verify(taxesService).getProductTaxes("name2");

    }

    @Test
    public void when_calculateTaxes_SaleResult_Is_Round_Up() {

        when(taxesService.getProductTaxes("name")).thenReturn(new ProductTaxes("name", 1.0, 2.0));

        var salesResult = calculatorService.calculateTaxes(
                Collections.singletonList(Product.builder().imported(false).name("name").price(1.0).quantity(1).build()));

        assertThat(salesResult).isNotNull()
                .isEqualTo(new SalesResult(
                        Collections.singletonList(
                                Pair.of(new ProductBill(Product.builder().imported(false).name("name").price(1.0).quantity(1).build(),
                                        new ProductTaxes("name", 1.0, 2.0)), 1.05)), 0.05, 1.05));

        verify(taxesService).getProductTaxes("name");

    }

    @Test
    public void when_calculateTaxes_SaleResult_Is_Not_Round_Up_If_0() {

        when(taxesService.getProductTaxes("name")).thenReturn(new ProductTaxes("name", 10.0, 2.0));

        var salesResult = calculatorService.calculateTaxes(
                Collections.singletonList(Product.builder().imported(false).name("name").price(1.0).quantity(1).build()));

        assertThat(salesResult).isNotNull()
                .isEqualTo(new SalesResult(
                        Collections.singletonList(
                                Pair.of(new ProductBill(Product.builder().imported(false).name("name").price(1.0).quantity(1).build(),
                                        new ProductTaxes("name", 10.0, 2.0)), 1.10)), 0.10, 1.10));

        verify(taxesService).getProductTaxes("name");

    }

    @Test
    public void when_calculateTaxes_Should_Apply_Importation_Taxes() {

        when(taxesService.getProductTaxes("name")).thenReturn(new ProductTaxes("name", 10.0, 20.0));

        var salesResult = calculatorService.calculateTaxes(
                Collections.singletonList(Product.builder().imported(true).name("name").price(1.0).quantity(1).build()));

        assertThat(salesResult).isNotNull();
        assertThat(salesResult.getCalculatedProductBills().get(0).getRight()).isEqualTo(1.3);
        assertThat(Precision.round(salesResult.getSalesTaxes(), 2)).isEqualTo(0.3);
        assertThat(salesResult.getTotal()).isEqualTo(1.3);

        verify(taxesService).getProductTaxes("name");

    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(taxesService);
    }

}
