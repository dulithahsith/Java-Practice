package com.gtngroup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.annotations.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class APIActionsFullTest {
    private static Playwright playwright;
    private static APIRequestContext apiContext;
    private static JsonNode selectedDataSet;
    private static JsonNode endpoints;
    private static String dataSetName = System.getenv("DATA_SET_NAME");

    @BeforeClass
    public static void setup() throws IOException {
        playwright = Playwright.create();

        String parametersContent = Files.readString(Paths.get("parameters.json"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode parameters = mapper.readTree(parametersContent);

        for (JsonNode dataSet : parameters) {
            if (dataSet.get("name").asText().equals(dataSetName)) {
                selectedDataSet = dataSet;
                break;
            }
        }

        String endpointsContent = Files.readString(Paths.get("endpoints.json"));
        endpoints = mapper.readTree(endpointsContent);

        apiContext = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(selectedDataSet.get("serverUrl").asText()));
    }

    @Test
    public void getUserProfileWithValidParameters() throws IOException {
        String username = "gtn_api_user";
        String endpointUrl = endpoints.get("userProfileEndpoint").asText();

        APIResponse response = apiContext.get(endpointUrl, RequestOptions.create()
                .setQueryParam("username", username)
                .setQueryParam("debug", "true"));

        assertThat(response).isOK();

        JsonNode jsonResponse = new ObjectMapper().readTree(response.text());
        assertThat((Locator) jsonResponse.get("status")).hasValue(String.valueOf(200));
    }


    @AfterClass
    public static void tearDown() {
        if (apiContext != null) {
            apiContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
