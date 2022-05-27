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
public class IngredientsItem {
    @JsonProperty("name")
    private String name;
    private double amount;
    public IngredientsItem(String name) {
        this.name = name;
    }
}