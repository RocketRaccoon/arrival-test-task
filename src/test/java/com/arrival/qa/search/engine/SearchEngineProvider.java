package com.arrival.qa.search.engine;


import com.google.inject.Inject;
import com.google.inject.Provider;

public class SearchEngineProvider implements Provider<SearchEngine> {

    private SearchEngineContext searchEngineContext;

    @Inject
    public SearchEngineProvider(SearchEngineContext searchEngineContext) {
        this.searchEngineContext = searchEngineContext;
    }

    @Override
    public SearchEngine get() {
        return new SearchEngine(searchEngineContext);
    }
}
