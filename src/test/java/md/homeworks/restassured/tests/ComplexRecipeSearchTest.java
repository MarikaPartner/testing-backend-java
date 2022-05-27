package md.homeworks.restassured.tests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import md.homeworks.restassured.dto.ComplexRecipeSearchResponse;
import md.homeworks.restassured.dto.ResultsItem;
import md.homeworks.restassured.endpoints.SpoonEndpoints;
import md.homeworks.restassured.extensions.SpoonApiTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;

@DisplayName("Поиск рецептов")
@SpoonApiTest
public class ComplexRecipeSearchTest {
    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;

    @BeforeAll
    static void beforeAll() {
        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("offset", 0)
                .addQueryParam("number", 10)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("offset", Matchers.equalTo(0))
                .expectBody("number", Matchers.equalTo(10))
                .build();
    }

    @Test
    @DisplayName("Поиск без запроса")
    @Severity(SeverityLevel.CRITICAL)
    public void SearchRecipesHasNotQueryTest() {

        given()
                .spec(requestSpecification)
                .get(SpoonEndpoints.RECIPE_COMPLEXSEACH.getEndpoint())
                .then()
                .spec(responseSpecification)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .body("results[0]", Matchers.hasKey("id"))
                .body("results[0]", Matchers.hasKey("title"))
                .body("results[0]", Matchers.hasKey("image"))
                .body("results[0]", Matchers.hasKey("imageType"));
    }

    @ParameterizedTest
    @DisplayName("Поиск по запросу: Запрос - слово")
    //@CsvSource(value = {"pizza", "soup"})
    @CsvSource(value = {"pizza"})
    @Severity(SeverityLevel.CRITICAL)
    public void SearchRecipesQueryIsWordTest(String query) {
        //JsonPath jsonPath = given()
                given()
                .queryParam("query", query)
                .spec(requestSpecification)
                .get(SpoonEndpoints.RECIPE_COMPLEXSEACH.getEndpoint())
                .then()
                .spec(responseSpecification)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .body("results.title", Matchers.everyItem(Matchers.containsStringIgnoringCase(query)));
    }

    @ParameterizedTest
    @DisplayName("Поиск по запросу: Запрос - фраза")
    @CsvSource(value = {"tomato soup", "pasta with chicken"})
    @Severity(SeverityLevel.CRITICAL)
    public void SearchRecipesQueryIsPhraseTest(String query) {
        JsonPath jsonPath = given()
                .queryParam("query", query)
                .spec(requestSpecification)
                .get("/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                //.body("results.title", Matchers.everyItem(Matchers.(query)))
                .extract()
                .jsonPath();
        List<String> recipeTitles = jsonPath.get("results.title");
        for(String recipeTitle : recipeTitles) {
            assertThat(recipeTitle.toLowerCase()).contains(query.toLowerCase().split(" "));
        }
    }

    @Test
    @DisplayName("Поиск с запросом детальной информации по рецептам")
    @Severity(SeverityLevel.NORMAL)
    public void SearchRecipesWithDetailedInformationTest() {
        given()
                .queryParam("addRecipeInformation", true)
                .spec(requestSpecification)
                .get(SpoonEndpoints.RECIPE_COMPLEXSEACH.getEndpoint())
                .then()
                .spec(responseSpecification)
                .body("results", Matchers.hasSize(Matchers.not(0)))
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
                .queryParam("addRecipeInformation", false)
                .spec(requestSpecification)
                .get(SpoonEndpoints.RECIPE_COMPLEXSEACH.getEndpoint())
                .then()
                .spec(responseSpecification)
                .body("results", Matchers.hasSize(Matchers.not(0)))
                .body("results[0].size()", Matchers.equalTo(4))
                .body("results[0]", Matchers.hasKey("id"))
                .body("results[0]", Matchers.hasKey("title"))
                .body("results[0]", Matchers.hasKey("image"))
                .body("results[0]", Matchers.hasKey("imageType"));
    }

    @ParameterizedTest
    @DisplayName("Поиск рецептов по кухне")
    @Severity(SeverityLevel.NORMAL)
    @CsvSource(value = {"African", "American", "Chinese"," Japanese"})
    public void SearchRecipesByCuisineTest(String cuisine) {
        ComplexRecipeSearchResponse complexRecipeSearchResponse = given()
                .queryParam("addRecipeInformation", true)
                .queryParam("cuisine", cuisine)
                .spec(requestSpecification)
                .get(SpoonEndpoints.RECIPE_COMPLEXSEACH.getEndpoint())
                .then()
                .spec(responseSpecification)
                .extract()
                .as(ComplexRecipeSearchResponse.class);

        List<List<String>> cuisines = complexRecipeSearchResponse.getResults().stream()
                .map(resultsItem -> resultsItem.getCuisines())
                .toList();

        for (List<String> recipeCuisines: cuisines) {
            assertThat(recipeCuisines).contains(cuisine);
        }
    }

    @ParameterizedTest
    @DisplayName("Поиск рецептов по типу питания")
    @Severity(SeverityLevel.NORMAL)
    @CsvSource(value = {"breakfast", "main course", "dessert"})
    public void SearchRecipesByMealTypeTest(String mealType) {
        ComplexRecipeSearchResponse complexRecipeSearchResponse = given()
                .queryParam("addRecipeInformation", true)
                .queryParam("type", mealType)
                .spec(requestSpecification)
                .get(SpoonEndpoints.RECIPE_COMPLEXSEACH.getEndpoint())
                .then()
                .spec(responseSpecification)
                .extract()
                .as(ComplexRecipeSearchResponse.class);

        List<List<String>> dishTypes = complexRecipeSearchResponse.getResults().stream()
                .map(resultsItem -> resultsItem.getDishTypes())
                .toList();

        System.out.println(dishTypes);

        for (List<String> recipeMealTypes: dishTypes) {
            assertThat(recipeMealTypes).contains(mealType);
        }
    }
}