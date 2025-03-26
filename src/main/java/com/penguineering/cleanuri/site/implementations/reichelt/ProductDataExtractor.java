package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.data.ProductDescription;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class ProductDataExtractor {
    private final Document document;
    private BiConsumer<Level, Throwable> exceptionHandler;

    public ProductDataExtractor(Document document) {
        this.document = document;
    }

    public ProductDataExtractor withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public Optional<ProductDescription> extractProductDescription() {
        try {
            Element productElement = document.selectFirst("#product > section.productHead");
            if (productElement == null)
                return Optional.empty();

            Optional<Element> productIdElement = Optional.ofNullable(productElement.selectFirst("div.productBuyArea > div.productBuy > p > span"));
            Optional<Element> productNameElement = Optional.ofNullable(productElement.selectFirst("div.productBuyArea > div.productBuy > h1"));
            Optional<Element> productImageElement = Optional.ofNullable(document.selectFirst("img[itemprop=image]"));

            ProductDescription.Builder productDescriptionBuilder = new ProductDescription.Builder();

            productIdElement.map(Element::ownText).ifPresent(productDescriptionBuilder::setId);
            productNameElement.map(Element::text).ifPresent(productDescriptionBuilder::setName);

            productImageElement
                    .map(element -> element.absUrl("src"))
                    .map(this::stripQueryParameters)
                    .ifPresent(productDescriptionBuilder::setImage);

            return productDescriptionBuilder.build();
        } catch (Exception e) {
            if (exceptionHandler != null)
                exceptionHandler.accept(Level.SEVERE, e);

            return Optional.empty();
        }
    }

    private URI stripQueryParameters(String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment());
        } catch (URISyntaxException e) {
            if (exceptionHandler != null)
                exceptionHandler.accept(Level.SEVERE, e);
            return null;
        }
    }
}