package com.arrival.qa.auth.yandex.module;

import com.arrival.qa.auth.yandex.context.AuthContext;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.arrival.qa.auth.yandex.context.AuthContextProvider;

public class YandexAuthModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(AuthContext.class).toProvider(AuthContextProvider.class);
    }
}
