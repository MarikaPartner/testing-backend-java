package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.util.List;
import java.util.Objects;

import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Создание новых товаров (POST /api/v1/products)")
public class CreateProductsNegativeTest extends BaseTest {
    private static ProductDto productDto;

    @SneakyThrows
    @BeforeEach
    void setUp() {

        // Создаем новый объект-товар
        productDto = makeProductDto(1, new Faker().food().fruit());
    }

    @SneakyThrows
    @Test
    @DisplayName("При создании нового продукта с ненулевым id получаем ошибку 400 'Id must be null for new entity'")
    @Severity(SeverityLevel.NORMAL)
    void createProductWithNotNullIdTest() {

        // Присваиваем объекту-продукту id
        productDto.setId((int) (Math.random() * 10));

        // Отправляем запрос на создание нового продукта
        Response<ProductDto> productDtoResponse = getProductsService().createProduct(productDto)
                .execute();

        // Проверяем ошибку
        assertThat(productDtoResponse.code()).isEqualTo(400);
        assertThat(getErrorMessage(productDtoResponse)).isEqualTo("Id must be null for new entity");
    }
}