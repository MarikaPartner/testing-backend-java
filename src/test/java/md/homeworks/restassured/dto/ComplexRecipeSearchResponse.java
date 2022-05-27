package md.homeworks.restassured.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplexRecipeSearchResponse {

	private int number;
	private int totalResults;
	private int offset;
	private List<ResultsItem> results;

}