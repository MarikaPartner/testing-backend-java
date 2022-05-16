package md.homeworks;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import md.homeworks.extensions.SpoonApiTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@SpoonApiTest
@DisplayName("Добавление еды в план питания")
public class AddMealToMealPlanTest {
    private static String userName;
    private static String hash;
    private Integer mealId;     // mealId, date, unixTime - статические, так для каждого теста их значения меняются
    private String date;
    private Long unixTime;

    @BeforeAll
    static void beforeAll() {
        Faker faker = new Faker();

        // Создаём пользователя
        JsonPath jsonPath = given()
                .body("{\n" +
                        "    \"username\": \"" + faker.funnyName().name() + "\",\n" +
                        "    \"firstName\": \"" + faker.name().firstName() + "\",\n" +
                        "    \"lastName\": \"" + faker.name().lastName() + "\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\"\n" +
                        "}")
                .post("/users/connect")
                .then()
                .statusCode(200)
                .extract()  // Извлечь
                .body()
                .jsonPath();
        userName = jsonPath.getString("username");
        hash = jsonPath.getString("hash");
    }

    @BeforeEach
    void setUp() {

        // Устанавливаем дату (пусть будет текущая)
        date = LocalDate.now().toString();
        unixTime = Instant.now().getEpochSecond();

        // Запрашиваем план питания пользователя на конкретный день, проверяем, что он пуст
        given()
                .queryParam("hash", hash)
                .get("/mealplanner/{username}/day/{date}", userName, date)
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("No meals planned for that day"));
    }

    @ParameterizedTest
    @DisplayName("Добавление ингридиента в план питания на определенный день")
    @CsvSource(value = {"1,1,1 egg", "2,1,50 g of bread", "3,1,1 apple"})
    @Severity(SeverityLevel.NORMAL)
    void addIngredientToMealPlanTest(Integer slot, Integer position, String name) {
        String type = "INGREDIENTS";

        // Добавляем ингридиент к плану питания
        mealId = given()
                .queryParam("hash", hash)
                .body("{\n" +
                        "    \"date\": " + unixTime + ",\n" +
                        "    \"slot\": " + slot + ",\n" +          // 1- завтрак, 2- обед, 3-ужин
                        "    \"position\": " + position + ",\n" +
                        "    \"type\": \"" + type + "\",\n" +
                        "    \"value\": {\n" +
                        "        \"ingredients\": [\n" +
                        "            {\n" +
                        "                \"name\": \"" + name + "\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}")
                .post("/mealplanner/{username}/items", userName)
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("success"))
                .extract()
                .jsonPath()
                .getInt("id");

        // Запрашиваем план питания пользователя на конкретный день и проверяем, что добавленная еда в нем присутствует
        given()
                .queryParam("hash", hash)
                .get("/mealplanner/{username}/day/{date}", userName, date)
                .then()
                .statusCode(200)
                .body("items.id", Matchers.hasItem(mealId),
                        "items.slot", Matchers.hasItem(slot),
                        "items.value.ingredients[0].name", Matchers.hasItems(name));
    }

    @ParameterizedTest
    @DisplayName("Добавление рецепта в план питания на определенный день")
    @CsvSource(value = {"1,1,635446,1,Blueberry Cinnamon Porridge",
            "2,1,652078,1,Miso Soup With Thin Noodles",
            "3,1,636228,1,Broccoli Tartar"})
    @Severity(SeverityLevel.NORMAL)
    void addRecipeToMealPlanTest(Integer slot, Integer position, Integer id, int servings, String title) {
        String type = "RECIPE";

        // Добавляем рецепт к плану питания
        mealId = given()
                .queryParam("hash", hash)
                .body("{\n" +
                        "    \"date\": " + unixTime + ",\n" +
                        "    \"slot\": " + slot + ",\n" +          // 1- завтрак, 2- обед, 3-ужин
                        "    \"position\": " + position + ",\n" +
                        "    \"type\": \"" + type + "\",\n" +
                        "    \"value\": {\n" +
                        "        \"id\": " + id + ",\n" +
                        "        \"servings\": " + servings + ",\n" +
                        "        \"title\": \"" + title + "\",\n" +
                        "        \"imageType\": \"jpg\",\n" +
                        "    }\n" +
                        "}")
                .post("/mealplanner/{username}/items", userName)
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("success"))
                .extract()
                .jsonPath()
                .getInt("id");

        // Запрашиваем план питания пользователя на конкретный день и проверяем, что добавленная еда в нем присутствует
        given()
                .queryParam("hash", hash)
                .get("/mealplanner/{username}/day/{date}", userName, date)
                .then()
                .statusCode(200)
                .body("items.id", Matchers.hasItems(mealId),
                        "items.slot", Matchers.hasItems(slot),
                        "items.value.title", Matchers.hasItems(title));
    }

    @AfterEach
    void tearDown() {

        // Удаляем добавленную еду
        given()
                .queryParam("hash", hash)
                .delete("/mealplanner/{username}/items/{id}", userName, mealId)
                .then()
                .statusCode(200);
    }
}