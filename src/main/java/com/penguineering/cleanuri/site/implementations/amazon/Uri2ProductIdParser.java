package com.penguineering.cleanuri.site.implementations.amazon;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uri2ProductIdParser {
    static final Pattern idPattern = Pattern.compile("https?://www\\.amazon\\.de/.*dp/([^/\\?]*)([/\\?].*|$)");

    public Uri2ProductIdParser() {
    }

    public Optional<String> getProductId(URI uri) {
        String uriString = uri.toString();
        Matcher matcher = idPattern.matcher(uriString);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        return Optional.ofNullable(matcher.group(1));
    }
}