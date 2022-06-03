package md.homeworks.retrofit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static md.homeworks.retrofit.util.RetrofitUtil.getCategoryService;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)   // При серриализации включаем только ненулевые поля
public class ProductDto {

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("categoryTitle")
    private String categoryTitle;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;
}