package com.lastminute.taxes.engine;

import com.lastminute.taxes.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxesService {

    private final static String TAXES_FILE_PATH = "/taxes/taxes.xml";
    private final static String TAX_QUERY_PATTERN = "/taxes/products/product[text()='%s']/..";

    private final DocumentBuilder documentBuilder;
    private final XPath xPath;

    public ProductTaxes getProductTaxes(String productName) {

        try {
            var xmlDocument = documentBuilder.parse(this.getClass().getResourceAsStream(TAXES_FILE_PATH));
            var taxExpression = xPath.compile(String.format(TAX_QUERY_PATTERN, productName));

            var taxNodes = (NodeList) taxExpression.evaluate(xmlDocument, XPathConstants.NODESET);

            var taxNode = Optional.ofNullable(taxNodes.item(0)).orElseThrow(() -> new ProductNotFoundException(productName));

            var tax = taxNode.getAttributes().getNamedItem("tax").getNodeValue();
            var importationTax = taxNode.getAttributes().getNamedItem("importationTax").getNodeValue();

            return new ProductTaxes(productName, Double.parseDouble(tax), Double.parseDouble(importationTax));

        } catch (SAXException | IOException | XPathExpressionException e) {
            log.error("Taxes from product '" + productName + "' not loaded" ,e);
            throw new ProductNotFoundException(productName);
        }
    }

}
