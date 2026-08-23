package com.penguineering.cleanuri.site.implementations.amazon;

import com.penguineering.cleanuri.site.Canonizer;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class AmazonCanonizer implements Canonizer {

    private static final Uri2ProductIdParser URI_PARSER = new Uri2ProductIdParser();

    public static final String PREFIX = "https://www.amazon.de/dp/";

    private final URI uri;
    private BiConsumer<Level, Throwable> exceptionHandler;

    public AmazonCanonizer(URI uri) {
        this.uri = uri;
    }

    @Override
    public AmazonCanonizer withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public Optional<URI> canonize() {
        return URI_PARSER
                .getProductId(uri)
                .map(s -> PREFIX + s)
                .map(URI::create)
                .or(() -> {
                    if (Objects.nonNull(exceptionHandler))
                        exceptionHandler.accept(
                                Level.WARNING,
                                new IllegalArgumentException("URI does not match Amazon pattern!"));
                    return Optional.empty();
                });
    }
}