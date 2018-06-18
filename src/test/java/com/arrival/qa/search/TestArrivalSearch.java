package com.arrival.qa.search;

import com.arrival.qa.search.context.SearchContext;
import com.arrival.qa.search.engine.SearchEngine;
import com.arrival.qa.search.module.GoogleModule;
import com.arrival.qa.search.module.YandexModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.qameta.allure.Step;
import name.falgout.jeffrey.testing.junit.guice.GuiceExtension;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(GuiceExtension.class)
class TestArrivalSearch {

    private final String SEARCH = "search";
    private final String USER_AGENT = "Mozilla/5.0";

    Logger log = LoggerFactory.getLogger(TestArrivalSearch.class);


    @BeforeEach
    void beforeEachSearch(TestInfo ti) {
        if (ti.getTags().contains(SEARCH)) log.info("This is before method for search findDomainInSearchResult");
    }

    private static Stream<Arguments> searchEngineAndContextProvider() {
        Injector ya = Guice.createInjector(new YandexModule());
        Injector gl = Guice.createInjector(new GoogleModule());
        return Stream.of(
                Arguments.of(ya.getInstance(SearchEngine.class), ya.getInstance(SearchContext.class)),
                Arguments.of(gl.getInstance(SearchEngine.class), gl.getInstance(SearchContext.class))
        );
    }

    @ParameterizedTest
    @Tag(SEARCH)
    @MethodSource("searchEngineAndContextProvider")
    void findDomainInSearchResult(SearchEngine engine, SearchContext ctx) throws IOException {

        Document searchResult = makeSearchRequest(engine.getFullUrl(ctx));
        assertThat(searchResult.title()).contains(engine.getTitle());

        List<String> resultLinks = parseSearchResult(searchResult);
        assertThat(resultLinks).contains(ctx.domain());

    }

    @AfterEach
    void afterEachSearch(TestInfo ti) {
        if (ti.getTags().contains(SEARCH)) log.info("This is after method for search findDomainInSearchResult");
    }

    @Step("Get document from {0}")
    private Document makeSearchRequest(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .get();
    }

    @Step
    private List<String> parseSearchResult(Document document) {
        return document.select("a[href]")
                .stream()
                .map(l -> l.attr("href"))
                .filter(l -> l.startsWith("http"))
                .collect(Collectors.toList());
    }

}
