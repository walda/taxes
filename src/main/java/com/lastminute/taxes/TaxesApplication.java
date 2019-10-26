package com.lastminute.taxes;

import com.lastminute.taxes.calculator.CalculatorService;
import com.lastminute.taxes.exception.ProductNotFoundException;
import com.lastminute.taxes.exception.UnparseableTokenException;
import com.lastminute.taxes.tokenizer.Product;
import com.lastminute.taxes.tokenizer.ScannerWrapper;
import com.lastminute.taxes.tokenizer.TokenizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
@RequiredArgsConstructor
public class TaxesApplication implements CommandLineRunner {

	private static String UNPARSEABLETOKEN_MESSAGE = "Error at line %s: '%s'";

	private final CalculatorService calculatorService;
	private final ScannerWrapper scannerWrapper;
	private final TokenizerService tokenizerService;

	@Override
	public void run(String... args) throws Exception {

		int lineCount = 1;

		try {
			var products = new ArrayList<Product>();

			while(scannerWrapper.hasNext()) {
				products.add(tokenizerService.parseToken(scannerWrapper.nextLine()));
				lineCount++;
			}

			System.out.println(calculatorService.calculateTaxes(products));

		} catch(UnparseableTokenException e) {
			System.err.println(String.format(UNPARSEABLETOKEN_MESSAGE, lineCount, e.getMessage()));
		} catch (ProductNotFoundException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(TaxesApplication.class, args);
	}
}
