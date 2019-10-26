package com.lastminute.taxes.calculator;

import com.lastminute.taxes.engine.TaxesService;
import com.lastminute.taxes.tokenizer.Product;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final TaxesService taxesService;

    public SalesResult calculateTaxes(List<Product> productList) {

        return productList.stream()
            .map(this::createProductBill)
                .map(CalculatorService::calculatedProductBill)
                .map(CalculatorService::toSalesResult)
                .collect(Collector.of(
                        CalculatorService.supplySalesResults(),
                        CalculatorService.appendSalesResult(),
                        CalculatorService.toSalesResult()
                ));
    }

    private ProductBill createProductBill(Product product) {
        return new ProductBill(product, taxesService.getProductTaxes(product.getName()));
    }

    private static SalesResult toSalesResult(Pair<ProductBill, Double> calculatedProductBill) {

        var quantity = calculatedProductBill.getLeft().getProduct().getQuantity();
        var price = calculatedProductBill.getLeft().getProduct().getPrice();
        var taxes = calculatedProductBill.getRight();

        var total = quantity * (price + taxes);
        var productTotal = Pair.of(calculatedProductBill.getLeft(), total);

        return new SalesResult(Collections.singletonList(productTotal), taxes, total);
    }

    private static Pair<ProductBill, Double> calculatedProductBill(ProductBill productBill) {
        var price = productBill.getProduct().getPrice();
        var taxRate = ( productBill.getProductTaxes().getTaxes()
                + (productBill.getProduct().isImported() ? productBill.getProductTaxes().getImportationTaxes() : 0))
                / 100;

        var taxTotal = roundUp(price * taxRate, 0.05);

        return Pair.of(productBill, taxTotal);

    }

    private static double roundUp(double value, double upTo) {
        var modulus = value % upTo;
        return value + (modulus != 0 ? (upTo - modulus) : 0);
    }

    private static Supplier<SalesResult> supplySalesResults() {
        return () -> new SalesResult(new ArrayList<>(), 0, 0);
    }

    private static BiConsumer<SalesResult, SalesResult> appendSalesResult() {
        return (current, next) -> {
            current.getCalculatedProductBills().addAll(next.getCalculatedProductBills());
            current.setSalesTaxes(current.getSalesTaxes() + next.getSalesTaxes());
            current.setTotal(current.getTotal() + next.getTotal());
        };
    }
    private static BinaryOperator<SalesResult> toSalesResult() {
        return (current, next) -> new SalesResult();

    }

}
