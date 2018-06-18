package com.arrival.qa.auth.yandex.context;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:AuthContext.properties"})
public interface AuthContext extends Config {
    String firstname();
    String lastname();
    String username();
    String password();
    String authUrl();
    String profileUrl();
    String host();
}
