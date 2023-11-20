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
        doc.select("div#av_price_discount td").stream()
                .map(Element::html)
                .map(html -> html.replace("<br>", "|"))
                .map(text -> text.split("\\|"))
                .filter(parts -> parts.length >= 2)
                .forEach(this::addDiscountToPricing);
    }

    private void addDiscountToPricing(String[] parts) {
        Optional<Integer> quantity = parseQuantity(parts[0]);
        Optional<BigDecimal> discountedPrice = parseDiscountedPrice(parts[1]);

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
            return sanitizeText(text).map(BigDecimal::new);
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