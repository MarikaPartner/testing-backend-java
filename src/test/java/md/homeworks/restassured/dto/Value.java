package md.homeworks.restassured.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Value{
	private Integer id;
	private int servings;                         // Количество порций
	private String title;
	private String imageType;                     // Тип картинки
	private List <IngredientsItem> ingredients;   // Список ингридиентов
	private String image;                         // Ссылка на картинку

	// Конструктор для типа добавляемой еды: INGREDIENTS
	public Value(String name) {
		this.ingredients = new ArrayList<>();
		ingredients.add(new IngredientsItem(name));
	}

	// Конструктор для типов добавляемой еды: RECIPE, PRODUCT, MENU_ITEM
	public Value(Integer id, int servings, String title, String imageType) {
		this.id = id;
		this.servings = servings;
		this.title = title;
		this.imageType = imageType;
	}
}