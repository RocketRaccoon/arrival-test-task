package com.arrival.qa.auth.yandex.context;

import com.google.inject.Provider;
import org.aeonbits.owner.ConfigFactory;

public class AuthContextProvider implements Provider<AuthContext> {
    @Override
    public AuthContext get() {
        return ConfigFactory.create(AuthContext.class);
    }
}
