package md.homeworks.restassured.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse{
    private String status;
	private String username;
	private String spoonacularPassword;
	private String hash;
}