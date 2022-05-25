package md.homeworks.restassured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import md.homeworks.restassured.endpoints.SpoonEndpoints;
import md.homeworks.restassured.extensions.SpoonApiTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Map;

import static io.restassured.RestAssured.given;

@DisplayName("Определение кухни")
@SpoonApiTest
public class ClassifyCuisineTest {
    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;

    @BeforeAll
    static void beforeAll() {
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    @DisplayName( "Проверка ответа")
    public void gettingResponseTest() {
        given()
                .spec(requestSpecification)
                .formParams(Map.of("title", "sushi"))
                .post("/recipes/cuisine")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .body("", Matchers.notNullValue())        // Проверяем, что тело ответа не пустое
                .body("", Matchers.hasKey("cuisine"))     // Проверяем, что ответ содержит поля, указанные в документации
                .body("", Matchers.hasKey("cuisines"))
                .body("", Matchers.hasKey("confidence"));
    }

    @ParameterizedTest
    @DisplayName( "Проверка соответствия названия блюда кухне")
    @CsvSource(value = {"sushi,Japanese","pizza,Mediterranean", "Cornish pasty,European", "falafel,Middle Eastern"})
    public void classificationByTitleTest(String mealTitle, String cuisine) {
        given()
                .formParams(Map.of("title", mealTitle))
                .post(SpoonEndpoints.RECIPES_CUISINE.getEndpoint())
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .body("", Matchers.notNullValue())
                .body("cuisine", Matchers.equalTo(cuisine));
    }
}