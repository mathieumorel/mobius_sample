package com.example.mathieumorel.mobiussample.search;

import com.spotify.mobius.Next;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;
import static com.spotify.mobius.Next.noChange;

public class SearchLoop {

    private SearchApi mApi;

    public SearchLoop(SearchApi api) {
        mApi = api;
    }

    public static Next<SearchModel, SearchEffect> update(SearchModel model, SearchEvent event) {
        return event.map(
                textChanged -> {
                    // always send a search request if the text changes, and
                    // mark that we are waiting for results
                    return next(model.toBuilder()
                                    .query(textChanged.text())
                                    .waitingForResult(true)
                                    .build(),
                            effects(SearchEffect.searchRequest(textChanged.text())));
                },

                searchResult -> {
                    // ignore search results if we're not waiting for anything,
                    // this allows us to drop events that arrive too late.
                    if (!model.waitingForResult()) {
                        return noChange();
                    }

                    // if the result query matches the model query, store the result
                    // and stop waiting for any further results.
                    if (model.query().equals(searchResult.query())) {
                        return next(model.toBuilder()
                                .searchResult(searchResult.results())
                                .waitingForResult(false)
                                .build());
                    }

                    // if we are waiting for results, but this wasn't the result
                    // we are waiting for, just update the model with this
                    // intermediate result.
                    return next(model.toBuilder()
                            .searchResult(searchResult.results())
                            .build());
                },

                searchError -> {
                    // ignore search errors if we're not waiting for anything,
                    // this allows us to drop errors if we already have a response to
                    // the query we're after.
                    if (!model.waitingForResult()) {
                        return noChange();
                    }

                    // if the query matches the model query, we need to tell the
                    // user.
                    if (model.query().equals(searchError.query())) {
                        return dispatch(effects(SearchEffect.showErrorMessage("search request failed")));
                    }

                    // otherwise, just ignore the error, there are other requests in
                    // flight that may succeed.
                    return noChange();
                }
        );
    }

    public Observable<SearchEvent> effectHandler(Observable<SearchEffect> effects) {

        return effects.ofType(SearchEffect.SearchRequest.class)
                .debounce(200, TimeUnit.MILLISECONDS)
                .flatMap(request ->
                        mApi.searchForText(request.query())
                                .map(result -> SearchEvent.searchResult(request.query(), result))
                                .onErrorReturn(err -> SearchEvent.searchError(request.query())));
    }
}
