package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.Canonizer;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class ReicheltCanonizer implements Canonizer {

    private static final Uri2ArticleIdParser URI_PARSER = new Uri2ArticleIdParser();

    public static final String PREFIX = "https://www.reichelt.de/index.html?ARTICLE=";

    private final URI uri;
    private BiConsumer<Level, Throwable> exceptionHandler;

    public ReicheltCanonizer(URI uri) {
        this.uri = uri;
    }

    @Override
    public ReicheltCanonizer withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
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
                                new IllegalArgumentException("URI does not match Reichelt pattern!"));
                    return Optional.empty();
                });
    }
}