package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.Extractor;
import com.penguineering.cleanuri.site.data.Pricing;
import com.penguineering.cleanuri.site.data.ProductDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class ReicheltExtractor implements Extractor {
    private final URI uri;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Document> doc;

    private BiConsumer<Level, Throwable> exceptionHandler;

    public ReicheltExtractor(URI uri) {
        this.uri = uri;
    }

    // Additional constructor for testing
    protected ReicheltExtractor(Document doc) {
        this.uri = null; // URI is not needed when a Document is provided
        this.doc = Optional.ofNullable(doc);
    }

    @Override
    public ReicheltExtractor withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public Optional<String> extractDocumentTitle() {
        loadDocument();
        return doc.map(Document::title);
    }

    @Override
    public Optional<ProductDescription> extractProductDescription() {
        loadDocument();
        return doc.flatMap(document ->
                new ProductDataExtractor(document)
                        .withExceptionHandler(exceptionHandler)
                        .extractProductDescription());
    }

    @Override
    public Optional<Pricing> extractPricing() {
        loadDocument();
        return doc
                .map(d -> new PriceExtractor(d).withExceptionHandler(exceptionHandler))
                .flatMap(PriceExtractor::extractPricing);
    }

    private void loadDocument() {
        if (Objects.isNull(doc))
            doc = downloadHtml();
    }

    private Optional<Document> downloadHtml() {
        try {
            Document doc = Jsoup.connect(uri.toString()).get();
            return Optional.of(doc);
        } catch (Exception e) {
            if (exceptionHandler != null) {
                exceptionHandler.accept(Level.SEVERE, e);
            }
            return Optional.empty();
        }
    }
}