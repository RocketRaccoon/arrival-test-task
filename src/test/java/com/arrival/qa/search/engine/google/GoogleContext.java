package com.arrival.qa.search.engine.google;

import com.arrival.qa.search.engine.SearchEngineContext;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:GoogleContext.properties"})
public interface GoogleContext extends SearchEngineContext, Config {
}
