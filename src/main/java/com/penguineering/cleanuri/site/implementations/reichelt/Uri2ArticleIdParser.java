package com.penguineering.cleanuri.site.implementations.reichelt;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uri2ArticleIdParser {
    static final Pattern artidPattern = Pattern.compile("^http.*://www\\.reichelt\\.de/.*-p(.*)\\.html.*$");
    static final Pattern artidPattern2 = Pattern.compile("^http.*://www\\.reichelt\\.de/index\\.html\\?ARTICLE=(.*)$");

    public Uri2ArticleIdParser() {
    }

    public Optional<String> getArticleId(URI uri) {
        String uriString = uri.toString();
        Pattern[] patterns = {artidPattern, artidPattern2};

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(uriString);
            if (matcher.find()) {
                return Optional.ofNullable(matcher.group(1));
            }
        }

        return Optional.empty();
    }
}