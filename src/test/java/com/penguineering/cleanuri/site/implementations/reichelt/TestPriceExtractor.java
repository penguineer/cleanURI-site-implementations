package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.data.Pricing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class TestPriceExtractor {
    /**
     * This is a parameterized test using CSV source as input data.
     * The CSV source has three columns:
     *
     * <ul>
     *   <li>fileName: Relative path to the HTML file to parse.</li>
     *   <li>expectedUnitPriceStr: Expected unit price as a string.</li>
     *   <li>expectedDiscountsStr: String of expected discounts. Each discount is a
     *   quantity-price pair, separated by a colon. Multiple discounts are comma-separated.</li>
     * </ul>
     *
     * Each CSV source row represents a separate test case.
     */    @ParameterizedTest
    @CsvSource({
            "testdata/reichelt/reichelt-p10228.html, 0.07, ''",
            "testdata/reichelt/reichelt-p58170.html, 2.20, '10:2.090,50:1.870,100:1.650'"
    })
    public void testExtractPricing(String fileName, String expectedUnitPriceStr, String expectedDiscountsStr) throws IOException {
        BigDecimal expectedUnitPrice = new BigDecimal(expectedUnitPriceStr);
        Map<Integer, BigDecimal> expectedDiscounts = parseDiscounts(expectedDiscountsStr);

        // Load the test data file
        URL resourceUrl = getClass().getClassLoader().getResource(fileName);
        assertNotNull(resourceUrl, "Test data file not found");
        File testDataFile = new File(resourceUrl.getFile());
        Document doc = Jsoup.parse(testDataFile, "UTF-8", "");

        // Create a PriceExtractor
        PriceExtractor priceExtractor = new PriceExtractor(doc);

        // Extract the pricing
        Optional<Pricing> pricingOpt = priceExtractor.extractPricing();

        // Check if the pricing was extracted
        assertTrue(pricingOpt.isPresent());

        // Check if the extracted pricing matches the expected pricing
        Pricing pricing = pricingOpt.get();
        assertEquals(Optional.of(expectedUnitPrice), pricing.getUnitPrice());

        Map<Integer, BigDecimal> actualDiscounts = new HashMap<>();
        pricing.streamDiscounts().forEach(discount -> actualDiscounts.put(discount.quantity(), discount.unitPrice()));

        assertEquals(expectedDiscounts, actualDiscounts);
    }

    private Map<Integer, BigDecimal> parseDiscounts(String discountsStr) {
        Map<Integer, BigDecimal> discounts = new HashMap<>();
        if (!discountsStr.isEmpty()) {
            String[] discountPairs = discountsStr.split(",");
            for (String pair : discountPairs) {
                String[] parts = pair.split(":");
                discounts.put(Integer.parseInt(parts[0]), new BigDecimal(parts[1]));
            }
        }
        return discounts;
    }
}
