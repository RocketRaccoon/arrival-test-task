package com.arrival.qa.auth.yandex;

import com.arrival.qa.auth.yandex.context.AuthContext;
import com.arrival.qa.auth.yandex.module.YandexAuthModule;
import com.google.inject.Inject;
import io.qameta.allure.Step;
import name.falgout.jeffrey.testing.junit.guice.GuiceExtension;
import name.falgout.jeffrey.testing.junit.guice.IncludeModule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(GuiceExtension.class)
@IncludeModule(YandexAuthModule.class)
class TestArrivalAuth {

    private static final String USER_AGENT = "Mozilla/5.0";
    private List<String> cookies;
    @Inject
    private AuthContext ctx;

    private Logger log = LoggerFactory.getLogger(TestArrivalAuth.class);

    @BeforeEach
    void prepareCookieHandler() {
        CookieHandler.setDefault(new CookieManager());
    }

    @Test
    void test() throws Exception {

        log.info("Prepare auth request context");
        String page = getPageContent(ctx.authUrl());
        String postParams = getAuthFormParams(page);

        log.info("Execute auth request");
        postAuthRequest(postParams);
        Document profile = Jsoup.parse(getPageContent(ctx.profileUrl()));

        log.info("Assert profile page");
        assertThat(profile.getElementsByAttributeValue("class", "personal-info__first"))
                .extracting(Element::text).contains(ctx.firstname());
        assertThat(profile.getElementsByAttributeValue("class", "personal-info__last"))
                .extracting(Element::text).contains(ctx.lastname());

    }

    @Step("Get page content from {0}")
    private String getPageContent(String url) throws Exception {

        URL auth = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) auth.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (cookies != null) cookies.forEach(c -> conn.addRequestProperty("Cookie", c.split(";", 1)[0]));
        log.info("Sending 'GET' request to URL: " + url);
        log.info("Response: " + conn.getResponseCode() + " " + conn.getResponseMessage());

        setCookies(conn.getHeaderFields().get("Set-Cookie"));

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.lines().collect(Collectors.joining());
        in.close();

        return response;

    }

    private String getAuthFormParams(String html) {

        log.info("Extracting form's data");

        Document doc = Jsoup.parse(html);

        Elements input = doc.getElementsByClass("passport-Input-Controller");
        List<String> params = new ArrayList<>();
        input.forEach(i -> {
            String key = i.attr("name");
            String val = i.attr("value");
            if (key.equals("login"))
                val = ctx.username();
            else if (key.equals("passwd"))
                val = ctx.password();
            try {
                params.add(key + "=" + URLEncoder.encode(val, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        StringBuilder result = new StringBuilder();
        params.forEach(param -> {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&").append(param);
            }
        });
        return result.toString();
    }

    @Step("Post auth request with {0}")
    private void postAuthRequest(String postParams) throws Exception {

        URL auth = new URL(ctx.authUrl() + "?mode=embeddedauth");
        HttpsURLConnection conn = (HttpsURLConnection) auth.openConnection();
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Host", ctx.host());
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (cookies != null) cookies.forEach(c -> conn.addRequestProperty("Cookie", c.split(";", 1)[0]));
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Referer", ctx.authUrl() + "/add");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        conn.setDoOutput(true);
        conn.setDoInput(true);

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        log.info("Send 'POST' request to URL: " + ctx.authUrl());
        log.info("Post parameters: " + postParams);
        log.info("Response Code: " + responseCode);

    }

    private void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }


}
