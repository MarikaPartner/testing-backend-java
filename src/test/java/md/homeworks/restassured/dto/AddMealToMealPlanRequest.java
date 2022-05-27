package md.homeworks.restassured.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMealToMealPlanRequest {
	@JsonProperty("date")
	private long date;    // Дата в секундах
	@JsonProperty("slot")
	private Integer slot;
	@JsonProperty("position")
	private int position;
	@JsonProperty("type")
	private String type;
	@JsonProperty("value")
	private Value value;

	public AddMealToMealPlanRequest(Integer slot, int position, String type, Value value) {
		this.slot = slot;
		this.position = position;
		this.type = type;
		this.value = value;
	}
}