package com.lastminute.taxes.calculator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesResult {

    private static final String PRODUCT_PATTERN = "%s%s %s: %.2f\n";
    private static final String SALES_TAXES_PATTERN = "Sales Taxes: %.2f\n";
    private static final String TOTAL_PATTERN = "Total: %.2f\n";

    private List<Pair<ProductBill, Double>> calculatedProductBills = Collections.emptyList();
    private double salesTaxes;
    private double total;


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        for(Pair<ProductBill, Double> product : calculatedProductBills) {
            var productBill = product.getLeft().getProduct();
            sb.append(String.format(PRODUCT_PATTERN, productBill.getQuantity(),
                    productBill.isImported() ? " IMPORTED" : "", productBill.getName(), product.getRight()));
        }

        sb.append(String.format(SALES_TAXES_PATTERN, salesTaxes));
        sb.append(String.format(TOTAL_PATTERN, total));

        return sb.toString();
    }

}
