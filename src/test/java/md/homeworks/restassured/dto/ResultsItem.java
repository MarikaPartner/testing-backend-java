package md.homeworks.restassured.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"diets", "occasions", "analyzedInstructions", "spoonacularSourceUrl",
		"sustainable", "glutenFree", "veryPopular", "healthScore", "aggregateLikes", "creditsText", "dairyFree",
        "openLicense", "veryHealthy", "license", "pricePerServing", "lowFodmap", "weightWatcherSmartPoints",
		"sourceName", "sourceUrl", "summary"})
public class ResultsItem{

	// Основные поля
	private String image;
	private int id;
	private String title;
	private String imageType;

	// Дополнительные поля для ответов с запросом детальной информации
	private int readyInMinutes;
	private int servings;
	private boolean vegetarian;
	private int preparationMinutes;
	private int cookingMinutes;
	private boolean vegan;
	private boolean cheap;
	private List<String> dishTypes;
	private String gaps;
	private List<String> cuisines;
}