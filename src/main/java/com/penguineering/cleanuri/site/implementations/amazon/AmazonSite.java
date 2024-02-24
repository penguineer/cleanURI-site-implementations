package com.penguineering.cleanuri.site.implementations.amazon;

import com.penguineering.cleanuri.site.AbstractSiteBase;
import com.penguineering.cleanuri.site.Canonizer;
import com.penguineering.cleanuri.site.Extractor;
import com.penguineering.cleanuri.site.SiteDescriptor;

import java.net.URI;
import java.util.Optional;

public class AmazonSite extends AbstractSiteBase {
    private static final Uri2ProductIdParser URI_PARSER = new Uri2ProductIdParser();

    public AmazonSite() {
        super(new SiteDescriptor.Builder("Amazon")
                .description("Amazon is an American multinational technology company.")
                .site(URI.create("https://www.amazon.de/"))
                .author("Stefan Haun")
                .license("MIT")
                .build());
    }

    @Override
    public boolean canProcessURI(URI uri) {
        return URI_PARSER
                .getProductId(uri)
                .isPresent();
    }

    @Override
    public Optional<Canonizer> newCanonizer(URI uri) {
        return Optional.of(new AmazonCanonizer(uri));
    }

    @Override
    public Optional<Extractor> newExtractor(URI uri) {
        return Optional.of(new AmazonExtractor(uri));
    }
}