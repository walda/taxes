package com.lastminute.taxes;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TaxesApplicationIT {

    private final static String TEST1 = "1 BOOK 12.49\n1 CD 14.99\n1 CHOCOLATE 0.85";
    private final static String TEST2 = "1 IMPORTED CHOCOLATE 10.00\n1 IMPORTED PERFUME 47.50\n";
    private final static String TEST3 = "1 IMPORTED PERFUME 27.99\n1 PERFUME 18.99\n1 HEADACHE_PILLS 9.75\n1 IMPORTED CHOCOLATE 11.25\n";
    private final static String INVALID_INPUT = "a PERFUME 27.21";

    @BeforeEach
    public void init() {
        Locale.setDefault(new Locale("es", "ES"));
    }

    @Test
    public void execute_Taxes_For_Example_1() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(TEST1.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        TaxesApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
        .isEqualTo("1 BOOK: 12,49\n1 CD: 16,49\n1 CHOCOLATE: 0,85\nSales Taxes: 1,50\nTotal: 29,83\n\n");
    }

    @Test
    public void execute_Taxes_For_Example_2() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(TEST2.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        TaxesApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("1 IMPORTED CHOCOLATE: 10,50\n1 IMPORTED PERFUME: 54,65\nSales Taxes: 7,65\nTotal: 65,15\n\n");
    }

    @Test
    public void execute_Taxes_For_Example_3() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(TEST3.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        TaxesApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("1 IMPORTED PERFUME: 32,19\n1 PERFUME: 20,89\n1 HEADACHE_PILLS: 9,75\n1 IMPORTED CHOCOLATE: 11,85\nSales Taxes: 6,70\nTotal: 74,68\n\n");
    }

    @Test
    public void execute_Taxes_For_Invalid_Input() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(INVALID_INPUT.getBytes()));
        System.setErr(new PrintStream(byteArrayOutputStream));

        TaxesApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("Error at line 1: 'Token 'a PERFUME 27.21' could not be parsed'\n");
    }

}
