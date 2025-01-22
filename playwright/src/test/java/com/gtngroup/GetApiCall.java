package com.gtngroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class GetApiCall {
    private APIRequestContext  requestContext;
    private Playwright playwright;
    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        APIRequest apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    @Test
    public void getMembersApiTest(){
        APIResponse apiResponse = requestContext.get("http://localhost:8080/api/members");
        System.out.println(apiResponse.status());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = null;
        try {
            jsonResponse = objectMapper.readTree(apiResponse.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---print jsonResponse---");
        System.out.println(jsonResponse.toPrettyString());
        System.out.println("---print url---");
        System.out.println(apiResponse.url());
        System.out.println("---print response headers---");
        Map<String,String> responseHeaders = apiResponse.headers();
        System.out.println(responseHeaders);

        Assert.assertEquals(responseHeaders.get("transfer-encoding"),"chunked");
    }

    @Test
    public void getBooksApiTest(){
        APIResponse apiResponse = requestContext.get("http://localhost:8080/api/books");
        System.out.println(apiResponse.status());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = null;
        try {
            jsonResponse = objectMapper.readTree(apiResponse.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---print jsonResponse---");
        System.out.println(jsonResponse.toPrettyString());
        System.out.println("---print url---");
        System.out.println(apiResponse.url());
        System.out.println("---print response headers---");
        Map<String,String> responseHeaders = apiResponse.headers();
        System.out.println(responseHeaders);

        Assert.assertEquals(responseHeaders.get("transfer-encoding"),"chunked");

    }

    @Test
    public void getSpecificBookTest(){
        APIResponse apiResponse = requestContext.get("http://localhost:8080/api/books/search", RequestOptions.create()
                .setQueryParam("name","harry")
                .setQueryParam("author","rowling"));

        System.out.println("---Print Response text---");
        System.out.println(apiResponse.text());
        Map<String,String> responseHeaders = apiResponse.headers();
        System.out.println(responseHeaders);

        Assert.assertEquals(responseHeaders.get("transfer-encoding"),"chunked");

        JsonNode jsonResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonResponse = objectMapper.readTree(apiResponse.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---print jsonResponse---");
        System.out.println(jsonResponse.toPrettyString());
        System.out.println("---print url---");
        System.out.println(apiResponse.url());
        System.out.println("---print response headers---");
        System.out.println(responseHeaders);
    }

    @Test
    public void borrowBookTest(){

    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }

}
