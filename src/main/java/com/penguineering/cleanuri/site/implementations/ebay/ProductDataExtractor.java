package com.penguineering.cleanuri.site.implementations.ebay;

import com.penguineering.cleanuri.site.data.ProductDescription;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
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
            Optional<String> id = Optional.ofNullable(document.selectFirst("link[rel=canonical]"))
                    .map(element -> element.attr("href"))
                    .map(href -> href.substring(href.lastIndexOf('/') + 1));

            if (id.isEmpty())
                return Optional.empty();

            Optional<Element> productNameElement = Optional.ofNullable(document.selectFirst("#mainContent > div.vim.d-vi-region.x-atf-center-river--top > div.vim.x-item-title > h1"));
            Optional<Element> productImageElement = Optional.ofNullable(document.selectFirst("#mainImgHldr > div.ux-image-carousel-container > div.ux-image-carousel.img-transition-medium > div.ux-image-carousel-item.active.image > div > div > img"));

            ProductDescription.Builder productDescriptionBuilder = new ProductDescription.Builder();

            id.ifPresent(productDescriptionBuilder::setId);
            productNameElement.map(Element::text).ifPresent(productDescriptionBuilder::setName);

            productImageElement
                    .map(element -> element.absUrl("src"))
                    .map(URI::create)
                    .ifPresent(productDescriptionBuilder::setImage);

            return productDescriptionBuilder.build();
        } catch (Exception e) {
            if (exceptionHandler != null)
                exceptionHandler.accept(Level.SEVERE, e);

            return Optional.empty();
        }
    }
}