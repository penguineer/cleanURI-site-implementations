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

public class TestEbayExtractor {
    @ParameterizedTest
    @CsvSource({
            "'testdata/ebay/ebay-401619364415.html', '401619364415', 'LM2596S XL6009 DC-DC Boost Buck Step Up Down Voltage Power Converter Module', 'https://i.ebayimg.com/images/g/odYAAOSwqeVbyFgy/s-l500.jpg'",
            "'testdata/ebay/ebay-404683105546.html', '404683105546', 'ELEGOO Neptune 4 MAX 3D Drucker 500mm/s High Speed FDM Printer Large Size', 'https://i.ebayimg.com/images/g/IbIAAOSwpHdlgWgm/s-l500.jpg'"
    })
    public void testEbayExtractor(String fileName,
                                   String expectedId,
                                   String expectedName,
                                   String expectedImageStr) throws IOException {
        Optional<String> expectedIdOpt = Optional.ofNullable(expectedId);
        Optional<String> expectedNameOpt = Optional.ofNullable(expectedName);
        Optional<URI> expectedImageOpt = Optional.ofNullable(expectedImageStr).map(URI::create);

        URL resourceUrl = getClass().getClassLoader().getResource(fileName);
        assertNotNull(resourceUrl, "Test data file not found");
        File file = new File(resourceUrl.getFile());
        Document doc = Jsoup.parse(file, "UTF-8");

        EbayExtractor ebayExtractor = new EbayExtractor(doc);

        Optional<ProductDescription> productDescriptionOpt = ebayExtractor.extractProductDescription();

        assertTrue(productDescriptionOpt.isPresent());

        ProductDescription productDescription = productDescriptionOpt.get();
        assertEquals(expectedIdOpt, productDescription.getId());
        assertEquals(expectedNameOpt, productDescription.getName());
        assertEquals(expectedImageOpt, productDescription.getImage());
    }
}