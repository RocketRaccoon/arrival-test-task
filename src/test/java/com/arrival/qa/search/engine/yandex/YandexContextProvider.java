package com.arrival.qa.search.engine.yandex;

import com.arrival.qa.search.engine.SearchEngineContext;
import com.google.inject.Provider;
import org.aeonbits.owner.ConfigFactory;

public class YandexContextProvider implements Provider<SearchEngineContext> {

    @Override
    public SearchEngineContext get() {
        return ConfigFactory.create(YandexContext.class);
    }
}
