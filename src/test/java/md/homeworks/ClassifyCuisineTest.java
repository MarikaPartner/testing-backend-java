package md.homeworks;

import md.homeworks.extensions.SpoonApiTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Map;

import static io.restassured.RestAssured.given;

@DisplayName("Определение кухни")
@SpoonApiTest
public class ClassifyCuisineTest {

    @Test
    @DisplayName( "Проверка ответа")
    public void gettingResponseTest() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of("title", "sushi"))
                .post("/recipes/cuisine")
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
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
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of("title", mealTitle))
                .post("/recipes/cuisine")
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("", Matchers.notNullValue())
                .body("cuisine", Matchers.equalTo(cuisine));
    }
}