package com.penguineering.cleanuri.site.implementations.reichelt;

import com.penguineering.cleanuri.site.ExceptionPassing;
import com.penguineering.cleanuri.site.data.Pricing;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class PriceExtractor implements ExceptionPassing {
    private final Document doc;
    private final Pricing.Builder pricingBuilder;

    private BiConsumer<Level, Throwable> exceptionHandler;

    public PriceExtractor(Document doc) {
        this.doc = doc;
        this.pricingBuilder = new Pricing.Builder();
    }

    @Override
    public PriceExtractor withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public Optional<Pricing> extractPricing() {
        extractUnitPrice();
        extractDiscounts();

        return pricingBuilder.build();
    }

    private void extractUnitPrice() {
        try {
            Optional.ofNullable(doc.selectFirst("meta[itemprop=price]"))
                    .map(unitPriceElement -> unitPriceElement.attr("content"))
                    .map(BigDecimal::new)
                    .ifPresent(pricingBuilder::setUnitPrice);
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        }
    }

    private void extractDiscounts() {
        doc.select("#product > section.productHead > div.productBuyArea > div.productBuy > div.awesomeDropdown.discountValue > ul > li > div.inline-block > a.inline-block")
                .forEach(this::processDiscountElement);
    }

    private void processDiscountElement(Element element) {
        Optional<Integer> quantity = Optional
                .ofNullable(element.selectFirst("p.left.text span.block"))
                .map(Element::text)
                .flatMap(this::parseQuantity);

        Optional<BigDecimal> discountedPrice = Optional
                .ofNullable(element.selectFirst("p.productPrice.right"))
                .map(Element::text)
                .flatMap(this::parseDiscountedPrice);

        if (quantity.isPresent() && discountedPrice.isPresent())
            pricingBuilder.addDiscount(quantity.get(), discountedPrice.get());
    }

    private Optional<Integer> parseQuantity(String text) {
        try {
            return sanitizeText(text).map(Integer::parseInt);
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
            return Optional.empty();
        }
    }

    private Optional<BigDecimal> parseDiscountedPrice(String text) {
        try {
            return sanitizeText(text.replace("\u00a0€", "")).map(BigDecimal::new);
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
            return Optional.empty();
        }
    }

    private Optional<String> sanitizeText(String text) {
        return Optional.ofNullable(text)
                .map(s -> s.replace(",", "."))
                .map(s -> s.replaceAll("[^0-9.]", ""))
                .map(String::trim)
                .filter(s -> !s.isBlank());
    }

    private void handleNumberFormatException(NumberFormatException e) {
        if (exceptionHandler != null)
            exceptionHandler.accept(Level.SEVERE, e);
    }
}
