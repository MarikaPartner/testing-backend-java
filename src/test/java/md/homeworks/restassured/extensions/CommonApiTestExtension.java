package md.homeworks.restassured.extensions;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static md.homeworks.restassured.config.SpoonConfig.spoonConfig;

public class CommonApiTestExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.config = RestAssured.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2)); // Настраиваем Rest-assured на работу с библиотекой Jackson
    }
}
