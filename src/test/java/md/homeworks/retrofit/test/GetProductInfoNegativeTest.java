package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.Objects;

import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Получение информации о товаре (GET /api/v1/products/{id})")
public class GetProductInfoNegativeTest extends BaseTest {
    private static ProductDto productDto;
    private static Integer productId;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        // Создаем новый объект-товар
        productDto = makeProductDto(1, new Faker().food().vegetable());

        // Отправляем запрос на создание нового товара
        Response<ProductDto> productDtoResponse = getProductsService().createProduct(productDto)
                .execute();
        assertThat(productDtoResponse.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @Test
    @DisplayName("При запросе информации о несуществующем продукте получаем ошибку 404 'Unable to find product with id: {id}'")
    @Severity(SeverityLevel.CRITICAL)
    void getNonExistentProductInfoTest() {
        // Получаем несуществующий id (находим максимальный и увеличиваем на 1)
        Integer nonExistentId = getIdsAllProducts()
                .stream()
                .mapToInt(n -> n)
                .max()
                .orElseThrow() + 1;
        productDto.setId(nonExistentId);

        // Отправляем запрос на получение информации о товаре с несуществующим id
        Response<ProductDto> productDtoResponse = getProductsService().getProduct(nonExistentId)
                .execute();
        assertThat(productDtoResponse.code()).isEqualTo(404);
        assertThat(getErrorMessage(productDtoResponse)).isEqualTo("Unable to find product with id: " + nonExistentId);
    }
}