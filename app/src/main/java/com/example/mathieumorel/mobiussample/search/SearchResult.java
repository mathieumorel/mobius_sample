package com.example.mathieumorel.mobiussample.search;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchResult {

    public abstract String value();

    public static SearchResult create(String value) {
        return new AutoValue_SearchResult(value);
    }
}
