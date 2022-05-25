package md.homeworks.restassured.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}