package md.homeworks.restassured.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import md.homeworks.restassured.dto.*;
import md.homeworks.restassured.endpoints.SpoonEndpoints;
import md.homeworks.restassured.extensions.SpoonApiTest;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.*;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@SpoonApiTest
@DisplayName("Добавление еды в план питания")
public class AddMealToMealPlanTest {
    private static CreateUserResponse createUserResponse;
    private static AddMealToMealPlanResponse addMealToMealPlanResponse;
    private static GetMealPlanResponse getMealPlanResponse;
    private static LocalDate date;
    private static Long unixTime;
    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;
    private static RequestSpecification hashParam;

    @BeforeAll
    static void beforeAll() {

        responseSpecification = new ResponseSpecBuilder()
                .expectBody("status", Matchers.equalTo("success"))
                .build();
        createUserResponse = new CreateUserResponse();

        // Создаём пользователя
        Faker faker = new Faker();
        createUserResponse = given()
                .body(CreateUserRequest.builder()
                                .username(faker.funnyName().name())
                                .firstName(faker.name().firstName())
                                .lastName(faker.name().lastName())
                                .email(faker.internet().emailAddress())
                                .build())
                .post(SpoonEndpoints.USERS_CONNECT.getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(CreateUserResponse.class);   // Преобразуем полученный Json в объект класса CreateUserResponse

        //createUserResponse.setUsername("preston-aufderhar");
        //createUserResponse.setHash("af927fb67983ba48e613d34c50fbae4da580ef50");

        hashParam = new RequestSpecBuilder()
                .addQueryParam("hash", createUserResponse.getHash())
                .build();

        // Устанавливаем дату (пусть будет текущая)
        date = LocalDate.now();
        unixTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z"));
    }

    @BeforeEach
    void setUp() {

        // Запрашиваем план питания пользователя на конкретный день, проверяем, что он пуст
        given()
                //.contentType(ContentType.JSON)
                .spec(hashParam)
                .get(SpoonEndpoints.MEALPLANNER_USERNAME_DAY_DATE.getEndpoint(), createUserResponse.getUsername(), date.toString())
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("No meals planned for that day"));
    }

    // Тестовые данные
    public static Stream<AddMealToMealPlanRequest> addMealToMealPlanRequests() {
        return Stream.of(new AddMealToMealPlanRequest(unixTime, 3, 1, "RECIPE", new Value(636228,1,"Broccoli Tartar", "jpg")),
                new AddMealToMealPlanRequest(unixTime, 2, 1, "RECIPE", new Value(1096010,1,"Egg Salad Wrap", "jpg")),
                new AddMealToMealPlanRequest(unixTime, 1, 2, "INGREDIENTS", new Value("1 banana")),
                new AddMealToMealPlanRequest(unixTime, 3, 1, "INGREDIENTS", new Value("1 orange")),
                new AddMealToMealPlanRequest(unixTime, 2, 4, "PRODUCT", new Value(878207, 1,"Hot Dang Southwest Burger","jpeg")),
                new AddMealToMealPlanRequest(unixTime, 1, 1, "PRODUCT", new Value(204593, 1,"Kemps Kemps Select Milk, 1 gl","jpeg")));

        /* Подготовка тестовых данных через builder (заменила из-за громоздкости)
        return Stream.of(AddMealToMealPlanRequest.builder()
                        .date(unixTime)
                        .slot(3)
                        .position(1)
                        .type("RECIPE")
                        .value(Value.builder()
                                .id(636228)
                                .servings(1)
                                .title("Broccoli Tartar")
                                .imageType("jpg")
                                .build())
                        .build(), ...); */
    }

    @ParameterizedTest
    @DisplayName("Добавление еды в план питания на определенный день")
    @MethodSource("addMealToMealPlanRequests")
    @Severity(SeverityLevel.NORMAL)
    void addMealToMealPlanTest(AddMealToMealPlanRequest addMealToMealPlanRequest) {

        // Добавляем еду к плану питания
        addMealToMealPlanResponse = given()
                .spec(hashParam)
                .body(addMealToMealPlanRequest)
                .post(SpoonEndpoints.MEALPLANNER_USERNAME_ITEMS.getEndpoint(), createUserResponse.getUsername())
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .extract()
                .as(AddMealToMealPlanResponse.class);

        // Запрашиваем план питания пользователя на конкретный день
        getMealPlanResponse = given()
                .spec(hashParam)
                .get(SpoonEndpoints.MEALPLANNER_USERNAME_DAY_DATE.getEndpoint(), createUserResponse.getUsername(), date.toString())
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .as(GetMealPlanResponse.class);

        // Проверяем, что добавленная еда присутствует в плане питания
        assertThat(getMealPlanResponse.getItems().get(0).getId())
                .isEqualTo(addMealToMealPlanResponse.getId());

        // Проверяем, что добавленная еда находится в нужном слоте и на нужной позиции, имеет соответствующее название
        SoftAssertions softAssertionsAfter = new SoftAssertions();
        softAssertionsAfter.assertThat(getMealPlanResponse.getItems().get(0).getSlot())
                .isEqualTo(addMealToMealPlanRequest.getSlot());
        softAssertionsAfter.assertThat(getMealPlanResponse.getItems().get(0).getPosition())
                .isEqualTo(addMealToMealPlanRequest.getPosition());
        softAssertionsAfter.assertThat(getMealPlanResponse.getItems().get(0).getValue().getTitle())
                .isEqualTo(addMealToMealPlanRequest.getValue().getTitle());
        softAssertionsAfter.assertAll();
    }

    @ParameterizedTest
    @Disabled("Объединён с addRecipeToMealPlanTest в addMealToMealPlanRequest")
    @DisplayName("Добавление ингридиента в план питания на определенный день")
    @CsvSource(value = {"1,1,1 egg", "2,1,50 g of bread", "3,1,1 apple"})
    @Severity(SeverityLevel.NORMAL)
    void addIngredientToMealPlanTest(Integer slot, Integer position, String name) {
        String type = "INGREDIENTS";

        // Добавляем ингридиент к плану питания
        addMealToMealPlanResponse = given()
                .spec(hashParam)
                .body(AddMealToMealPlanRequest.builder()
                        .date(unixTime)
                        .slot(slot)
                        .position(position)
                        .type(type)
                        .value(Value.builder()
                                .ingredients(List.of(IngredientsItem.builder()
                                        .name(name)
                                        .build()))
                                .build())
                        .build())
                .post(SpoonEndpoints.MEALPLANNER_USERNAME_ITEMS.getEndpoint(), createUserResponse.getUsername())
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .extract()
                .as(AddMealToMealPlanResponse.class);

        // Запрашиваем план питания пользователя на конкретный день и проверяем, что добавленная еда в нем присутствует
        given()
                //.contentType(ContentType.JSON)
                .spec(hashParam)
                .get(SpoonEndpoints.MEALPLANNER_USERNAME_DAY_DATE.getEndpoint(), createUserResponse.getUsername(), date.toString())
                .then()
                .statusCode(200)
                .body("items.id", Matchers.hasItem(addMealToMealPlanResponse.getId()),
                        "items.slot", Matchers.hasItem(slot),
                        "items.value.ingredients[0].name", Matchers.hasItems(name));
    }

    @ParameterizedTest
    @Disabled("Объединён с addIngredientToMealPlanTest в addMealToMealPlanRequest")
    @DisplayName("Добавление рецепта в план питания на определенный день")
    @CsvSource(value = {"1,1,635446,1,Blueberry Cinnamon Porridge",
            "2,1,652078,1,Miso Soup With Thin Noodles",
            "3,1,636228,1,Broccoli Tartar"})
    @Severity(SeverityLevel.NORMAL)
    void addRecipeToMealPlanTest(Integer slot, Integer position, Integer id, int servings, String title) {
        String type = "RECIPE";

        // Добавляем рецепт к плану питания
        addMealToMealPlanResponse = given()
                //.contentType(ContentType.JSON)
                .spec(hashParam)
                .body(AddMealToMealPlanRequest.builder()
                        .date(unixTime)
                        .slot(slot)
                        .position(position)
                        .type(type)
                        .value(Value.builder()
                                .id(id)
                                .servings(servings)
                                .title(title)
                                .imageType("jpg")
                                .build())
                        .build())
                .post(SpoonEndpoints.MEALPLANNER_USERNAME_ITEMS.getEndpoint(), createUserResponse.getUsername())
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .extract()
                .as(AddMealToMealPlanResponse.class);

        // Запрашиваем план питания пользователя на конкретный день и проверяем, что добавленная еда в нем присутствует
        given()
                //.contentType(ContentType.JSON)
                .spec(hashParam)
                .get(SpoonEndpoints.MEALPLANNER_USERNAME_DAY_DATE.getEndpoint(), createUserResponse.getUsername(), date.toString())
                .then()
                .statusCode(200)
                .body("items.id", Matchers.hasItems(addMealToMealPlanResponse.getId()),
                        "items.slot", Matchers.hasItems(slot),
                        "items.value.title", Matchers.hasItems(title));
    }

    @AfterEach
    void tearDown() {

        // Удаляем добавленную еду
        given()
                .spec(hashParam)
                .delete(SpoonEndpoints.MEALPLANNER_USERNAME_ITEMS_ID.getEndpoint(), createUserResponse.getUsername(), addMealToMealPlanResponse.getId())
                .then()
                .statusCode(200);
    }
}