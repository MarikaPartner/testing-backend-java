package md.homeworks.restassured.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMealToMealPlanResponse{

	private Integer id;
	private String status;
}