package com.lastminute.taxes.engine;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ProductTaxes {

    private String name;
    private double taxes;
    private double importationTaxes;

}
