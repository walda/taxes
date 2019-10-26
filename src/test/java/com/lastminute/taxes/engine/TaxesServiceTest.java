package com.lastminute.taxes.engine;

import com.lastminute.taxes.exception.ProductNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TaxesServiceTest {

    @Mock
    private DocumentBuilder documentBuilder;
    @Mock
    private Document document;
    @Mock
    private XPath xPath;
    @Mock
    private NodeList nodeList;
    @Mock
    private XPathExpression xPathExpression;
    @Mock
    private Node node;
    @Mock
    private NamedNodeMap namedNodeMap;
    @InjectMocks
    private TaxesService taxesService;

    @Test
    public void when_getProductTaxes() throws IOException, SAXException, XPathExpressionException {

        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile("/taxes/products/product[text()='name']/.."))
                .thenReturn(xPathExpression);
        when(xPathExpression.evaluate(document, XPathConstants.NODESET))
                .thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(node);
        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getNamedItem("tax")).thenReturn(node);
        when(namedNodeMap.getNamedItem("importationTax")).thenReturn(node);
        when(node.getNodeValue()).thenReturn("5", "10");

        assertThat(taxesService.getProductTaxes("name"))
            .isNotNull()
            .isEqualTo(new ProductTaxes("name", 5, 10));

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/taxes/products/product[text()='name']/..");
        verify(xPathExpression).evaluate(document, XPathConstants.NODESET);
        verify(nodeList).item(0);
        verify(node, times(2)).getAttributes();
        verify(namedNodeMap).getNamedItem("tax");
        verify(namedNodeMap).getNamedItem("importationTax");
        verify(node, times(2)).getNodeValue();

    }

    @Test
    public void when_getProductTaxes_Should_Raise_ProductNotFoundException() throws IOException, SAXException, XPathExpressionException {

        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile("/taxes/products/product[text()='name']/.."))
                .thenReturn(xPathExpression);
        when(xPathExpression.evaluate(document, XPathConstants.NODESET))
                .thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenThrow(new ProductNotFoundException("product"));

        Throwable t = catchThrowable(() -> taxesService.getProductTaxes("name"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product 'product' not found");

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/taxes/products/product[text()='name']/..");
        verify(xPathExpression).evaluate(document, XPathConstants.NODESET);
        verify(nodeList).item(0);

    }

    @Test
    public void shouldRaiseInvalidRuleExceptionWhenIOException() throws IOException, SAXException {
        when(documentBuilder.parse(any(InputStream.class))).thenThrow(new IOException());

        Throwable t = catchThrowable(() -> taxesService.getProductTaxes("name"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product 'name' not found");

        verify(documentBuilder).parse(any(InputStream.class));
    }

    @Test
    public void shouldRaiseInvalidRuleExceptionWhenSAXException() throws IOException, SAXException {
        when(documentBuilder.parse(any(InputStream.class))).thenThrow(new SAXException());

        Throwable t = catchThrowable(() -> taxesService.getProductTaxes("name"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product 'name' not found");

        verify(documentBuilder).parse(any(InputStream.class));
    }

    @Test
    public void shouldRaiseInvalidRuleExceptionWhenXPathExpressionException() throws IOException, SAXException, XPathExpressionException {

        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile(anyString())).thenThrow(new XPathExpressionException(""));

        Throwable t = catchThrowable(() -> taxesService.getProductTaxes("name"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product 'name' not found");

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/taxes/products/product[text()='name']/..");
    }


    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(documentBuilder, xPath, xPathExpression, nodeList, node, namedNodeMap);
    }

}
