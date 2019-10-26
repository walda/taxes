package com.lastminute.taxes.tokenizer;

import com.lastminute.taxes.exception.UnparseableTokenException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class TokenizerService {

    private static final Pattern PRODUCT_TOKEN =
            Pattern.compile("^(?<quantity>[0-9]+)(?<imported>\\s\\bIMPORTED\\b)?\\s(?<name>[a-zA-Z_]+)\\s(?<price>[0-9]+(\\.[0-9]{2})?)$");

    public Product parseToken(String text) {
        isValidPattern(PRODUCT_TOKEN, text);

        var matcher = PRODUCT_TOKEN.matcher(text.trim());
        matcher.find();

        return Product.builder()
                .quantity(Integer.parseInt(matcher.group("quantity")))
                .name(matcher.group("name"))
                .price(Double.parseDouble(matcher.group("price")))
                .imported(Objects.nonNull(matcher.group("imported")) ? true : false)
                .build();
    }

    private static void isValidPattern(Pattern pattern, String text) {
        if(!pattern.matcher(text).matches()) {
            throw new UnparseableTokenException(text);
        }
    }
}
