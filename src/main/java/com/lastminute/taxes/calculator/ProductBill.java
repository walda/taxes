package com.lastminute.taxes.calculator;

import com.lastminute.taxes.engine.ProductTaxes;
import com.lastminute.taxes.tokenizer.Product;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
class ProductBill {
    private Product product;
    private ProductTaxes productTaxes;

}
