package com.penguineering.cleanuri.site.implementations.ebay;

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
            "'testdata/ebay/ebay-401619364415.html', '401619364415', 'LM2596S XL6009 DC-DC Boost Buck Step Up Down Voltage Power Converter Module', 'https://i.ebayimg.com/images/g/odYAAOSwqeVbyFgy/s-l500.jpg'",
            "'testdata/ebay/ebay-404683105546.html', '404683105546', 'ELEGOO Neptune 4 MAX 3D Drucker 500mm/s High Speed FDM Printer Large Size', 'https://i.ebayimg.com/images/g/IbIAAOSwpHdlgWgm/s-l500.jpg'"
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