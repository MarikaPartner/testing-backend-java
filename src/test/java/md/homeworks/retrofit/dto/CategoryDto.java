package md.homeworks.retrofit.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class CategoryDto{

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("products")
	private List<ProductDto> products;
}