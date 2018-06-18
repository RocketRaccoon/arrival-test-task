package com.arrival.qa.search.engine;

import com.arrival.qa.search.context.SearchContext;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SearchEngine {

    final String url;
    final String endpoint;
    final String queryParam;
    final String title;

    public SearchEngine(SearchEngineContext sec) {
        this.url = sec.url();
        this.endpoint = sec.endpoint();
        this.queryParam = sec.queryParam();
        this.title = sec.title();
    }

    public String getFullUrl(SearchContext sc) {
        return url + "/" + endpoint + "?" + queryParam + "=" + sc.text();
    }

}
