package md.homeworks.restassured.extensions;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static md.homeworks.restassured.config.SpoonConfig.spoonConfig;

public class SpoonApiTestExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        RestAssured.baseURI = spoonConfig.baseURI();
        RestAssured.requestSpecification = new RequestSpecBuilder()   // Используем, чтобы задать параметры одинаковые для всех запросов
                .setContentType(ContentType.JSON)
                .addQueryParam("apiKey", spoonConfig.apiKey())
                .build();
    }
}