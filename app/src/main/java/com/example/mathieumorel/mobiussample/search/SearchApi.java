package com.example.mathieumorel.mobiussample.search;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SearchApi {

    public Observable<SearchResult> searchForText(String query) {
        return Single.just(SearchResult.create("result")).toObservable();
    }

}
