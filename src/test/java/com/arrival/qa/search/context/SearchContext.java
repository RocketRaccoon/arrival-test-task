package com.arrival.qa.search.context;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:SearchContext.properties"})
public interface SearchContext extends Config {

    String text();

    String domain();

}
