package com.penguineering.cleanuri.site.implementations.ebay;

import com.penguineering.cleanuri.site.Canonizer;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EbayCanonizer implements Canonizer {

    private static final Uri2ArticleIdParser URI_PARSER = new Uri2ArticleIdParser();

    public static final String PREFIX = "https://www.ebay.de/itm/";

    private final URI uri;
    private BiConsumer<Level, Throwable> exceptionHandler;

    public EbayCanonizer(URI uri) {
        this.uri = uri;
    }

    @Override
    public EbayCanonizer withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public Optional<URI> canonize() {
        return URI_PARSER
                .getArticleId(uri)
                .map(s -> PREFIX + s)
                .map(URI::create)
                .or(() -> {
                    if (Objects.nonNull(exceptionHandler))
                        exceptionHandler.accept(
                                Level.WARNING,
                                new IllegalArgumentException("URI does not match Ebay pattern!"));
                    return Optional.empty();
                });
    }
}