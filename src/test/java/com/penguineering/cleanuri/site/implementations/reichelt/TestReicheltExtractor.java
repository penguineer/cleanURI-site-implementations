package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.data.Pricing;
import com.penguineering.cleanuri.site.data.ProductDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestReicheltExtractor {
    /**
     * This is a parameterized test that uses CSV source as input data.
     * The CSV source contains four columns:
     *
     * <ul>
     *   <li>fileName: The relative path to the HTML file to be parsed.</li>
     *   <li>expectedTitle: The expected title of the document.</li>
     *   <li>isProductDescriptionAvailable: A boolean indicating whether a product description is expected to be present.</li>
     *   <li>isPricingAvailable: A boolean indicating whether pricing information is expected to be present.</li>
     * </ul>
     *
     * Each row in the CSV source represents a separate test case.
     */
    @ParameterizedTest
    @CsvSource({
            "'testdata/reichelt/reichelt-p10228.html', 'HOTTECH SEMICONDUCTOR LED, 3 mm, bedrahtet, rot, 191 mcd, 50° | LEDs, 3 mm günstig kaufen | reichelt elektronik', true, true",
            "'testdata/reichelt/reichelt-p58170.html', 'FREI Digital Thermometer, 1-wire +/- 0,5°C, SO-8 | Temperatur- & Feuchtesensoren günstig kaufen | reichelt elektronik', true, true"
    })
    public void testReicheltExtractor(String fileName,
                                      String expectedTitle,
                                      boolean isProductDescriptionAvailable,
                                      boolean isPricingAvailable) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(fileName);
        assertNotNull(resourceUrl, "Test data file not found");
        File file = new File(resourceUrl.getFile());
        Document doc = Jsoup.parse(file, "UTF-8");

        ReicheltExtractor reicheltExtractor = new ReicheltExtractor(doc);

        Optional<String> titleOpt = reicheltExtractor.extractDocumentTitle();
        assertEquals(expectedTitle, titleOpt.orElse(null));

        Optional<ProductDescription> productDescriptionOpt = reicheltExtractor.extractProductDescription();
        assertEquals(isProductDescriptionAvailable, productDescriptionOpt.isPresent());

        Optional<Pricing> pricingOpt = reicheltExtractor.extractPricing();
        assertEquals(isPricingAvailable, pricingOpt.isPresent());
    }
}