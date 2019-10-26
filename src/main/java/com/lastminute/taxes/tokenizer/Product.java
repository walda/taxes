package com.lastminute.taxes.tokenizer;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Product {

    private int quantity;
    private boolean imported;
    private String name;
    private double price;
}
