package md.homeworks.retrofit.endpoints;

import md.homeworks.retrofit.dto.CategoryDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("categories/{id}")
    Call<CategoryDto> getCategory(@Path("id") Integer id);
}
