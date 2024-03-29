package com.penguineering.cleanuri.site.implementations.ebay;

import com.penguineering.cleanuri.site.Extractor;
import com.penguineering.cleanuri.site.data.Pricing;
import com.penguineering.cleanuri.site.data.ProductDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class EbayExtractor implements Extractor {
    private final URI uri;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Document> doc;
    private BiConsumer<Level, Throwable> exceptionHandler;

    public EbayExtractor(URI uri) {
        this.uri = uri;
    }

    // Additional constructor for testing
    protected EbayExtractor(Document doc) {
        this.uri = null; // URI is not needed when a Document is provided
        this.doc = Optional.ofNullable(doc);
    }
    @Override
    public EbayExtractor withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
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
        return doc
                .flatMap(document -> new ProductDataExtractor(document)
                        .withExceptionHandler(exceptionHandler)
                        .extractProductDescription());
    }

    @Override
    public Optional<Pricing> extractPricing() {
        // no pricing information are extracted from the ebay site
        return Optional.empty();
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