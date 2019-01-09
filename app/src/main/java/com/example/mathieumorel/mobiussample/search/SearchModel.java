package com.example.mathieumorel.mobiussample.search;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchModel {

    public abstract String query();

    public abstract SearchResult searchResult();

    public abstract boolean waitingForResult();

    public static SearchModel create(String query, SearchResult searchResult, boolean waitingForResult) {
        return toBuilder().query(query).searchResult(searchResult).waitingForResult(waitingForResult).build();
    }

    static Builder toBuilder() {
        return new AutoValue_SearchModel.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder query(String query);

        abstract Builder searchResult(SearchResult result);

        abstract Builder waitingForResult(boolean waiting);

        abstract SearchModel build();
    }
}
