package md.homeworks.restassured.endpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SpoonEndpoints {
    RECIPE_COMPLEXSEACH("/recipes/complexSearch"),                       // Поиск рецептов
    USERS_CONNECT("/users/connect"),                                     // Создание пользователя
    MEALPLANNER_USERNAME_ITEMS("/mealplanner/{username}/items"),         // Добавление еды к плану питания
    MEALPLANNER_USERNAME_DAY_DATE("/mealplanner/{username}/day/{date}"), // Получение плана питания
    MEALPLANNER_USERNAME_ITEMS_ID("/mealplanner/{username}/items/{id}"), // Удаление из плана
    RECIPES_CUISINE("/recipes/cuisine");                   // Классификатор кухни

    @Getter
    final String endpoint;
}
