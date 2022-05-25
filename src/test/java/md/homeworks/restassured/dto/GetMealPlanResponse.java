package md.homeworks.restassured.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"nutritionSummary", "nutritionSummaryBreakfast", "nutritionSummaryLunch", "nutritionSummaryDinner"})
public class GetMealPlanResponse{
	private int date;
	private String day;
	private List<ItemsItem> items;
}