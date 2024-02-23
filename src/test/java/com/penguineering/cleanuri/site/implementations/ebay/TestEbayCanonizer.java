package com.penguineering.cleanuri.site.implementations.ebay;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestEbayCanonizer {

    /**
     * This is a parameterized test that uses CSV source as input data.
     * The CSV source contains two columns:
     *
     * <ul>
     *   <li>inputUri: The URI of the HTML page to be canonized.</li>
     *   <li>outputUri: The expected canonized URI. It's null if the URI cannot be parsed by this canonizer.</li>
     * </ul>
     *
     * Each row in the CSV source represents a separate test case.
     */
    @ParameterizedTest
    @CsvSource({
            "'https://www.ebay.de/itm/404683105546?_trkparms=amclksrc%3DITM%26aid%3D1110013%26algo%3DHOMESPLICE.SIMRXI%26ao%3D1%26asc%3D259212%26meid%3Db3cf32a7a40c4c5294dabb305403dd46%26pid%3D101782%26rk%3D4%26rkt%3D10%26itm%3D404683105546%26pmt%3D1%26noa%3D1%26pg%3D4375194%26algv%3DPersonalizedV7_8_4%26brand%3DELEGOO&_trksid=p4375194.c101782.m155878&_trkparms=parentrq%3Ad764df8818d0a77347f8849effff5808%7Cpageci%3Ae81f2a9d-d27f-11ee-80c9-1aef3e56bd6b%7Ciid%3A1%7Cvlpname%3Avlp_homepage', 'https://www.ebay.de/itm/404683105546'",
            "'https://www.ebay.de/itm/314836950296?_trkparms=amclksrc%3DITM%26aid%3D777008%26algo%3DPERSONAL.TOPIC%26ao%3D1%26asc%3D20230811123857%26meid%3Ddf2a7e4af38946aa9ccbb1f4bacb17d2%26pid%3D101770%26rk%3D1%26rkt%3D1%26itm%3D314836950296%26pmt%3D1%26noa%3D1%26pg%3D4375194%26algv%3DRecentlyViewedItemsV2%26brand%3DMarkenlos&_trksid=p4375194.c101770.m146925&_trkparms=parentrq%3Ad72179f318d0a4f161a0a002ffff6880%7Cpageci%3A9f401eff-d275-11ee-92d7-7ead20010a38%7Ciid%3A1%7Cvlpname%3Avlp_homepage', 'https://www.ebay.de/itm/314836950296'",
            "'https://www.ebay.de/non-parsable-uri', ''"
    })
    void testCanonize(String inputUri, String expectedOutputUri) {
        EbayCanonizer canonizer = new EbayCanonizer(URI.create(inputUri));
        Optional<URI> result = canonizer.canonize();

        if (expectedOutputUri.isEmpty()) {
            assertEquals(Optional.empty(), result);
        } else {
            assertTrue(result.isPresent());
            assertEquals(expectedOutputUri, result.get().toString());
        }
    }
}