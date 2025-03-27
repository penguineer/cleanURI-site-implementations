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
            "'https://www.ebay.de/itm/267193750017', 'https://www.ebay.de/itm/267193750017'",
            "'https://www.ebay.de/itm/267193750017?_trkparms=amclksrc%3DITM%26aid%3D1110013%26algo%3DHOMESPLICE.SIMRXI%26ao%3D1%26asc%3D20240229164639%26meid%3D42a96a2c39e74dacb67c07132bf35b89%26pid%3D102015%26rk%3D6%26rkt%3D25%26itm%3D267193750017%26pmt%3D1%26noa%3D0%26pg%3D2332490%26algv%3DSimRXIVIWithPreRankerShuffling%26brand%3DDell&_trksid=p2332490.c102015.m3021&itmprp=cksum%3A26719375001742a96a2c39e74dacb67c07132bf35b89%7Cenc%3AAQAKAAABAB43nRy3r8erO8r9%252Bi%252Bb9SSI8bveKBuDBarbZ3wNTIQvd0TiwNyjTlNC9EqmqeuTK6eCsI6W75Ey42J7Bf%252BcwRj5SXYGthMYsXpt8UrjBv4qoMfvhBOfUldvSlil5%252BbRAq1VIJpPLa1F%252ByYZ87lbThiUOziNon7DPFB8Z4WCnPXOI5BvSzwwpjIl2JKOsLLFfrQfmHsIGXriwtZf3oQdp5q18XHYN15qgeMCccwZGl01h0yosDdrFaAOh1bjW36OQfhRliUfEkBV0zRbgInSDGxUDZQBY4%252B8hj6t6wmijVFPNB52THdMhrfKwmmcgLYcEyhJSSj61aLX9FpzSiIbVjE%253D%7Campid%3APL_CLK%7Cclp%3A2332490&itmmeta=01JQBG8F2W8Y83MBDY5WGAKXGT', 'https://www.ebay.de/itm/267193750017'",
            "'https://www.ebay.de/itm/334077595032?_trkparms=amclksrc%3DITM%26aid%3D777008%26algo%3DPERSONAL.TOPIC%26ao%3D1%26asc%3D20231108131718%26meid%3Df8594255579241569e2bd2a1e17cd576%26pid%3D101910%26rk%3D1%26rkt%3D1%26itm%3D334077595032%26pmt%3D0%26noa%3D1%26pg%3D4375194%26algv%3DFeaturedDealsV2&_trksid=p4375194.c101910.m150506&_trkparms=parentrq%3Ad7053b0d1950ab126c248c23ffffc504%7Cpageci%3A4fdf5dda-0af1-11f0-8a97-3691896cee21%7Ciid%3A1%7Cvlpname%3Avlp_homepage', 'https://www.ebay.de/itm/334077595032'",
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