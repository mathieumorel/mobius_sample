package com.example.mathieumorel.mobiussample.search;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchModel {
    public abstract String query();
    public abstract SearchResult searchResult();
    public abstract boolean waitingForResult();
}
