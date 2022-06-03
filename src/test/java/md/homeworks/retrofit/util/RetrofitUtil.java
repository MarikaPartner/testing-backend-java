package md.homeworks.retrofit.util;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import lombok.experimental.UtilityClass;
import md.homeworks.retrofit.endpoints.CategoryService;
import md.homeworks.retrofit.endpoints.ProductsService;
import md.homeworks.retrofit.log.LoggingInterceptor;
import md.homeworks.retrofit.log.PrettyLogger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static md.homeworks.retrofit.config.MiniMarketConfig.miniMarketConfig;

@UtilityClass
public class RetrofitUtil {

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(miniMarketConfig.baseURI())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient.Builder()                      // Добавляем логирование
                        //.addInterceptor(new LoggingInterceptor())       // Создаем свой класс-интерцептор (перехватчик)
                        .addInterceptor(new HttpLoggingInterceptor(new PrettyLogger()).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(new AllureOkHttp3())
                        .build())
                .build();
    }

    public static CategoryService getCategoryService() {
        return getRetrofit().create(CategoryService.class);
    }

    public static ProductsService getProductsService() {
        return getRetrofit().create(ProductsService.class);
    }
}