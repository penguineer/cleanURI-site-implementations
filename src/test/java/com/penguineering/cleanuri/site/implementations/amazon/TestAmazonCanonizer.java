package com.penguineering.cleanuri.site.implementations.amazon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestAmazonCanonizer {

    @ParameterizedTest
    @CsvSource({
            "'https://www.amazon.de/EMPIRE-GAMING-Wireless-Wiederaufladbare-Layout-Wei%C3%9F/dp/B0C1GYJXPL/ref=pd_ci_mcx_mh_mcx_views_2?pd_rd_w=hhs0x&content-id=amzn1.sym.696812cf-32f6-4cf9-8678-b5b30b3b3913%3Aamzn1.symc.45dc5f4c-d617-4dba-aa26-2cadef3da899&pf_rd_p=696812cf-32f6-4cf9-8678-b5b30b3b3913&pf_rd_r=XHJADA8RAJXWV0Q61ZQH&pd_rd_wg=ra8qC&pd_rd_r=d8f8885e-dd95-4a70-9b55-385bdee5da46&pd_rd_i=B0C1GYJXPL', 'https://www.amazon.de/dp/B0C1GYJXPL'",
            "'https://www.amazon.de/dp/B0C1GYJXPL?th=1', 'https://www.amazon.de/dp/B0C1GYJXPL'",
            "'https://www.amazon.de/dp/B0C1GYJXPL', 'https://www.amazon.de/dp/B0C1GYJXPL'",
            "'https://www.amazon.de/non-parsable-uri', ''"
    })
    void testCanonize(String inputUri, String expectedOutputUri) {
        AmazonCanonizer canonizer = new AmazonCanonizer(URI.create(inputUri));
        Optional<URI> result = canonizer.canonize();

        if (expectedOutputUri.isEmpty()) {
            assertEquals(Optional.empty(), result);
        } else {
            assertTrue(result.isPresent());
            assertEquals(expectedOutputUri, result.get().toString());
        }
    }
}