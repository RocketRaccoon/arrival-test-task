package com.arrival.qa.search.context;

import com.google.inject.Provider;
import org.aeonbits.owner.ConfigFactory;

public class SearchContextProvider implements Provider<SearchContext> {

    @Override
    public SearchContext get() {
        return ConfigFactory.create(SearchContext.class);
    }

}
