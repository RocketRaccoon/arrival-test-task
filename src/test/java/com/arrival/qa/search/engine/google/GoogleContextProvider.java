package com.arrival.qa.search.engine.google;

import com.arrival.qa.search.engine.SearchEngineContext;
import com.google.inject.Provider;
import org.aeonbits.owner.ConfigFactory;

public class GoogleContextProvider implements Provider<SearchEngineContext> {

    @Override
    public SearchEngineContext get() {
        return ConfigFactory.create(GoogleContext.class);
    }
}
