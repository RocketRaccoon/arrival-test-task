package com.arrival.qa.search.module;

import com.arrival.qa.search.context.SearchContext;
import com.arrival.qa.search.context.SearchContextProvider;
import com.arrival.qa.search.engine.SearchEngine;
import com.arrival.qa.search.engine.SearchEngineContext;
import com.arrival.qa.search.engine.SearchEngineProvider;
import com.arrival.qa.search.engine.google.GoogleContextProvider;
import com.arrival.qa.search.engine.yandex.YandexContextProvider;
import com.google.inject.Binder;
import com.google.inject.Module;

public class GoogleModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(SearchContext.class).toProvider(SearchContextProvider.class);
        binder.bind(SearchEngineContext.class).toProvider(GoogleContextProvider.class);
        binder.bind(SearchEngine.class).toProvider(SearchEngineProvider.class);
    }

}
