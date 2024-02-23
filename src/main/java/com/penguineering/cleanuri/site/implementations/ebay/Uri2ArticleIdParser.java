package com.penguineering.cleanuri.site.implementations.ebay;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uri2ArticleIdParser {
    static final Pattern idPattern = Pattern.compile("https?://www\\.ebay\\.de/.*/(.*?)(\\?.*)?$");

    public Uri2ArticleIdParser() {
    }

    public Optional<String> getArticleId(URI uri) {
        String uriString = uri.toString();
        Matcher matcher = idPattern.matcher(uriString);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        return Optional.ofNullable(matcher.group(1));
    }
}