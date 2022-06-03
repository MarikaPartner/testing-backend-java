package md.homeworks.retrofit.endpoints;

import md.homeworks.retrofit.dto.ProductDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ProductsService {

    @GET("products/{id}")
    Call<ProductDto> getProduct(@Path("id") Integer id);

    @GET("products")
    Call<List<ProductDto>> getProducts();

    @POST("products")
    Call<ProductDto> createProduct(@Body ProductDto productDto);

    @PUT("products")
    Call<ProductDto> changeProduct(@Body ProductDto productDto);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") Integer id);
}