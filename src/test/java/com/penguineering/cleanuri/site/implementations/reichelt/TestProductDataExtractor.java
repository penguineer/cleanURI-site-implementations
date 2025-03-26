package com.penguineering.cleanuri.site.implementations.reichelt;

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

public class TestProductDataExtractor {
    /**
     * This CSV file is used for testing the ProductDataExtractor class.
     * Each row in the CSV file corresponds to one test case.
     * The columns in the CSV file are as follows:
     *
     * <ul>
     *     <li><b>fileName</b>: The name of the HTML file to test. This should be a relative path to the file from the classpath root.</li>
     *     <li><b>expectedId</b>: The expected product id. This should be extracted from the h2 element inside the div#av_articleheader element in the HTML file.</li>
     *     <li><b>expectedName</b>: The expected product name. This should be extracted from the span[itemprop=name] element inside the div#av_articleheader element in the HTML file.</li>
     *     <li><b>expectedImageStr</b>: The expected product image URL. This should be extracted from the src attribute of the img[itemprop=image] element in the HTML file.</li>
     * </ul>
     */
    @ParameterizedTest
    @CsvSource({
            "'testdata/reichelt/reichelt-p10228.html', 'LED 3MM RT', 'LED, 3 mm, bedrahtet, rot, 191 mcd, 50°', 'https://cdn-reichelt.de/resize/600/-/web/xxl_ws/A500/LED3ST.png'",
            "'testdata/reichelt/reichelt-p58170.html', 'DS 18B20Z', 'Digital Thermometer, 1-wire +/- 0,5°C, SO-8', 'https://cdn-reichelt.de/resize/600/-/web/xxl_ws/A200/MSOP-8.png'"
    })
    public void testExtractProductDescription(String fileName,
                                              String expectedId,
                                              String expectedName,
                                              String expectedImageStr) throws IOException {
        Optional<String> expectedIdOpt = Optional.ofNullable(expectedId);
        Optional<String> expectedNameOpt = Optional.ofNullable(expectedName);
        Optional<URI> expectedImageOpt = Optional.ofNullable(expectedImageStr).map(URI::create);

        URL resourceUrl = getClass().getClassLoader().getResource(fileName);
        assertNotNull(resourceUrl, "Test data file not found");
        File testDataFile = new File(resourceUrl.getFile());
        Document doc = Jsoup.parse(testDataFile, "UTF-8", "");

        ProductDataExtractor productDataExtractor = new ProductDataExtractor(doc);

        Optional<ProductDescription> productDescriptionOpt = productDataExtractor.extractProductDescription();

        assertTrue(productDescriptionOpt.isPresent());

        ProductDescription productDescription = productDescriptionOpt.get();
        assertEquals(expectedIdOpt, productDescription.getId());
        assertEquals(expectedNameOpt, productDescription.getName());
        assertEquals(expectedImageOpt, productDescription.getImage());
    }
}