package com.lastminute.taxes;


import com.lastminute.taxes.calculator.CalculatorService;
import com.lastminute.taxes.calculator.SalesResult;
import com.lastminute.taxes.exception.ProductNotFoundException;
import com.lastminute.taxes.exception.UnparseableTokenException;
import com.lastminute.taxes.tokenizer.Product;
import com.lastminute.taxes.tokenizer.ScannerWrapper;
import com.lastminute.taxes.tokenizer.TokenizerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TaxesApplicationTests {

	@Mock
	private CalculatorService calculatorService;
	@Mock
	private ScannerWrapper scannerWrapper;
	@Mock
	private TokenizerService tokenizerService;
	@Mock
	private SalesResult salesResult;
	@InjectMocks
	private TaxesApplication taxesApplication;

	@Test
	public void when_TaxesApplication_Run() throws Exception {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.hasNext()).thenReturn(true, false);
		when(scannerWrapper.nextLine()).thenReturn("newLine");
		when(tokenizerService.parseToken("newLine")).thenReturn(Product.builder().build());
		when(calculatorService.calculateTaxes(Collections.singletonList(Product.builder().build())))
				.thenReturn(salesResult);
		when(salesResult.toString()).thenReturn("Sales Result toString");

		taxesApplication.run(new String[0]);

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("Sales Result toString\n");

		verify(scannerWrapper, times(2)).hasNext();
		verify(scannerWrapper).nextLine();
		verify(tokenizerService).parseToken("newLine");
		verify(calculatorService).calculateTaxes(Collections.singletonList(Product.builder().build()));

	}

	@Test
	public void when_TaxesApplication_Run_UnparseableTokenException_Is_Raised() throws Exception {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setErr(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.hasNext()).thenReturn(true);
		when(scannerWrapper.nextLine()).thenReturn("newLine");
		when(tokenizerService.parseToken("newLine")).thenThrow(new UnparseableTokenException("newLine"));

		taxesApplication.run(new String[0]);

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("Error at line 1: 'Token 'newLine' could not be parsed'\n");

		verify(scannerWrapper).hasNext();
		verify(scannerWrapper).nextLine();
		verify(tokenizerService).parseToken("newLine");

	}

	@Test
	public void when_TaxesApplication_Run_ProductNotFoundException_Is_Raised() throws Exception {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setErr(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.hasNext()).thenReturn(true);
		when(scannerWrapper.nextLine()).thenReturn("newLine");
		when(tokenizerService.parseToken("newLine")).thenThrow(new ProductNotFoundException("newLine"));

		taxesApplication.run(new String[0]);

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("ERROR: Product 'newLine' not found\n");

		verify(scannerWrapper).hasNext();
		verify(scannerWrapper).nextLine();
		verify(tokenizerService).parseToken("newLine");

	}

	@AfterEach
	public void tearDown() {
		verifyNoMoreInteractions(scannerWrapper, tokenizerService, calculatorService, salesResult);
	}

}
