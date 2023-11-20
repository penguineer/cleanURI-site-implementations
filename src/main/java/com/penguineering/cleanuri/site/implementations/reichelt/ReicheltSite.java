package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.AbstractSiteBase;
import com.penguineering.cleanuri.site.Canonizer;
import com.penguineering.cleanuri.site.Extractor;
import com.penguineering.cleanuri.site.SiteDescriptor;

import java.net.URI;
import java.util.Optional;

public class ReicheltSite extends AbstractSiteBase {
    private static final Uri2ArticleIdParser URI_PARSER = new Uri2ArticleIdParser();

    public ReicheltSite() {
        super(new SiteDescriptor.Builder("Reichelt")
                .description("Reichelt Elektronik is a German electronics retailer.")
                .site(URI.create("https://www.reichelt.de/"))
                .author("Stefan Haun")
                .license("MIT")
                .build());
    }

    @Override
    public boolean canProcessURI(URI uri) {
        return URI_PARSER
                .getArticleId(uri)
                .isPresent();
    }

    @Override
    public Optional<Canonizer> newCanonizer(URI uri) {
        return Optional.of(new ReicheltCanonizer(uri));
    }

    @Override
    public Optional<Extractor> newExtractor(URI uri) {
        return Optional.of(new ReicheltExtractor(uri));
    }
}