package md.homeworks.restassured.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsItem{
	private int id;
	private int slot;
	private int position;
	private String type;
	private Value value;
}