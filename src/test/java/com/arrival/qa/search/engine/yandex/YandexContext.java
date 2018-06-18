package com.arrival.qa.search.engine.yandex;

import com.arrival.qa.search.engine.SearchEngineContext;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:YandexContext.properties"})
public interface YandexContext extends SearchEngineContext, Config {
}
