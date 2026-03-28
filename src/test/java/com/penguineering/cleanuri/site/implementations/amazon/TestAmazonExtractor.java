package com.penguineering.cleanuri.site.implementations.amazon;

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

public class TestAmazonExtractor {
    @ParameterizedTest
    @CsvSource({
            "'testdata/amazon/amazon-B0C1GYJXPL.html', 'B0C1GYJXPL', 'EMPIRE GAMING - Armor RF800 Wireless Wiederaufladbare Gaming Tastatur und Maus Set QWERTZ (DE-Layout) - Tastatur 2,4GHz RGB - Maus 4800 DPI mit Mauspad - PC PS4 PS5 Xbox One/Serie Mac - Weiß', 'https://m.media-amazon.com/images/I/71z3qo6eRzL.__AC_SX300_SY300_QL70_ML2_.jpg'",
    })
    public void testAmazonExtractor(String fileName,
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

        AmazonExtractor amazonExtractor = new AmazonExtractor(doc);

        Optional<ProductDescription> productDescriptionOpt = amazonExtractor.extractProductDescription();

        assertTrue(productDescriptionOpt.isPresent());

        ProductDescription productDescription = productDescriptionOpt.get();
        assertEquals(expectedIdOpt, productDescription.getId());
        assertEquals(expectedNameOpt, productDescription.getName());
        assertEquals(expectedImageOpt, productDescription.getImage());
    }
}