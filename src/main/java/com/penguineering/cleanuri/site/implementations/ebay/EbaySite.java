package com.penguineering.cleanuri.site.implementations.ebay;

import com.penguineering.cleanuri.site.AbstractSiteBase;
import com.penguineering.cleanuri.site.Canonizer;
import com.penguineering.cleanuri.site.Extractor;
import com.penguineering.cleanuri.site.SiteDescriptor;

import java.net.URI;
import java.util.Optional;

public class EbaySite extends AbstractSiteBase {
    private static final Uri2ArticleIdParser URI_PARSER = new Uri2ArticleIdParser();

    public EbaySite() {
        super(new SiteDescriptor.Builder("Ebay")
                .description("Ebay is an American multinational e-commerce corporation.")
                .site(URI.create("https://www.ebay.de/"))
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
        return Optional.of(new EbayCanonizer(uri));
    }

    @Override
    public Optional<Extractor> newExtractor(URI uri) {
        return Optional.of(new EbayExtractor(uri));
    }
}