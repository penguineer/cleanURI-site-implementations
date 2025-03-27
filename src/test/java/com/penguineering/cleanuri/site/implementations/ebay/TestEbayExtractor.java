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
            "'testdata/ebay/ebay-267193750017.html', '267193750017', 'Dell Latitude 7200 2-in-1 LTE i5-8365U 16GB 512GB Win 11 Pro 12,3\" FP SC FID DE', 'https://i.ebayimg.com/thumbs/images/g/j-AAAOSwTVhhjig8/s-l500.jpg'",
            "'testdata/ebay/ebay-334077595032.html', '334077595032', 'LEGO® Creator Expert 10283 NASA-Spaceshuttle „Discovery“ NEU OVP EOL', 'https://i.ebayimg.com/thumbs/images/g/~PYAAOSwd2lnwquZ/s-l500.jpg'"
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