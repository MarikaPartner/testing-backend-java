package md.homeworks;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import md.homeworks.extensions.SpoonApiTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;
import java.util.Map;

@DisplayName("Поиск рецептов")
@SpoonApiTest
public class ComplexRecipeSearchTest {

    @Test
    @DisplayName("Поиск без запроса")
    @Severity(SeverityLevel.CRITICAL)
    public void SearchRecipesHasNotQueryTest() {
        given()
                .queryParams(Map.of("offset", 0,
                        "number", 10))
                .get("/recipes/complexSearch")
                .then()
                .statusCode(200)
                .body("number", Matchers.equalTo(10))
                .body("offset", Matchers.equalTo(0))
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .body("results[0]", Matchers.hasKey("id"))
                .body("results[0]", Matchers.hasKey("title"))
                .body("results[0]", Matchers.hasKey("image"))
                .body("results[0]", Matchers.hasKey("title"))
                .body("results[0]", Matchers.hasKey("imageType"));
    }

    @ParameterizedTest
    @DisplayName("Поиск по запросу: Запрос - слово")
    @CsvSource(value = {"pizza", "soup"})
    @Severity(SeverityLevel.CRITICAL)
    public void SearchRecipesQueryIsWordTest(String query) {
        JsonPath jsonPath = given()
                .queryParams(Map.of("query", query,
                        "offset", 0,
                        "number", 10))
                .get("/recipes/complexSearch")
                .then()
                .statusCode(200)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .extract()
                .jsonPath();
        List<String> recipeTitles = jsonPath.get("results.title");
        for(String recipeTitle : recipeTitles) {
            assertThat(recipeTitle.toLowerCase()).contains(query.toLowerCase());
        }
    }

    @ParameterizedTest
    @DisplayName("Поиск по запросу: Запрос - фраза")
    @CsvSource(value = {"tomato soup", "pasta with shrimp"})
    @Severity(SeverityLevel.CRITICAL)
    public void SearchRecipesQueryIsPhraseTest(String query) {
        JsonPath jsonPath = given()
                .queryParams(Map.of("query", query,
                        "offset", 0,
                        "number", 10))
                .get("/recipes/complexSearch")
                .then()
                .statusCode(200)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .extract()
                .jsonPath();
        List<String> recipeTitles = jsonPath.get("results.title");
        for(String recipeTitle : recipeTitles) {
            assertThat(recipeTitle.toLowerCase()).containsAnyOf(query.split(" "));
        }
    }

    @Test
    @DisplayName("Поиск с запросом детальной информации по рецептам")
    @Severity(SeverityLevel.NORMAL)
    public void SearchRecipesWithDetailedInformationTest() {
        given()
                .queryParams(Map.of("offset", 0,
                        "number", 5,
                        "addRecipeInformation", true))
                .get("/recipes/complexSearch")
                .then()
                .statusCode(200)
                .body("results[0]", Matchers.hasSize(Matchers.not(0)))
                .body("results[0]", Matchers.hasKey("vegetarian"))
                .body("results[0]", Matchers.hasKey("vegan"))
                .body("results[0]", Matchers.hasKey("glutenFree"))
                .body("results[0]", Matchers.hasKey("dairyFree"))
                .body("results[0]", Matchers.hasKey("veryHealthy"))
                .body("results[0]", Matchers.hasKey("veryPopular"))
                .body("results[0]", Matchers.hasKey("sustainable"))
                .body("results[0]", Matchers.hasKey("weightWatcherSmartPoints"))
                .body("results[0]", Matchers.hasKey("gaps"))
                .body("results[0]", Matchers.hasKey("aggregateLikes"))
                .body("results[0]", Matchers.hasKey("creditsText"))
                .body("results[0]", Matchers.hasKey("sourceName"))
                .body("results[0]", Matchers.hasKey("id"))
                .body("results[0]", Matchers.hasKey("title"))
                .body("results[0]", Matchers.hasKey("readyInMinutes"))
                .body("results[0]", Matchers.hasKey("servings"))
                .body("results[0]", Matchers.hasKey("sourceUrl"))
                .body("results[0]", Matchers.hasKey("image"))
                .body("results[0]", Matchers.hasKey("imageType"))
                .body("results[0]", Matchers.hasKey("summary"))
                .body("results[0]", Matchers.hasKey("cuisines"))
                .body("results[0]", Matchers.hasKey("dishTypes"))
                .body("results[0]", Matchers.hasKey("diets"))
                .body("results[0]", Matchers.hasKey("analyzedInstructions"))
                .body("results[0]", Matchers.hasKey("spoonacularSourceUrl"));
    }

    @Test
    @DisplayName("Поиск без запроса детальной информации по рецептам")
    @Severity(SeverityLevel.NORMAL)
    public void SearchRecipesWithoutDetailedInformationTest() {
        given()
                .queryParams(Map.of("offset", 0,
                        "number", 5,
                        "addRecipeInformation", false))
                .get("/recipes/complexSearch")
                .then()
                .statusCode(200)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .body("results[0].size()", Matchers.equalTo(4))
                .body("results[0]", Matchers.hasKey("id"))
                .body("results[0]", Matchers.hasKey("title"))
                .body("results[0]", Matchers.hasKey("image"))
                .body("results[0]", Matchers.hasKey("imageType"));
    }
}