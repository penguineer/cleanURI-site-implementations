package com.penguineering.cleanuri.site.implementations.reichelt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestReicheltCanonizer {

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
            "'https://www.reichelt.de/de/de/shop/produkt/led_3_mm_bedrahtet_rot_191_mcd_50_-10228', 'https://www.reichelt.de/index.html?ARTICLE=10228'",
            "'https://www.reichelt.de/de/de/shop/produkt/digital_thermometer_1-wire_-_0_5_c_so-8-58170', 'https://www.reichelt.de/index.html?ARTICLE=58170'",
            "'https://www.reichelt.de/non-parsable-uri', ''"
    })
    void testCanonize(String inputUri, String expectedOutputUri) {
        ReicheltCanonizer canonizer = new ReicheltCanonizer(URI.create(inputUri));
        Optional<URI> result = canonizer.canonize();

        if (expectedOutputUri.isEmpty()) {
            assertEquals(Optional.empty(), result);
        } else {
            assertTrue(result.isPresent());
            assertEquals(expectedOutputUri, result.get().toString());
        }
    }
}