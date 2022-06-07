package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import java.util.Objects;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Получение информации о товаре (GET /api/v1/products/{id})")
public class GetProductInfoTest extends BaseTest {
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

        // Получаем id созданного продукта
        productId = Objects.requireNonNull(productDtoResponse.body()).getId();
        assertThat(productId).isNotNull();
    }

    @SneakyThrows
    @Test
    @DisplayName("При запросе информации о существующем продукте получаем информацию о требуемом продукте")
    @Severity(SeverityLevel.CRITICAL)
    void getProductInfoTest() {

        // Отправляем запрос на получение информации о товаре
        Response<ProductDto> productDtoInfoResponse = getProductsService().getProduct(productId)
                .execute();
        assertThat(productDtoInfoResponse.isSuccessful()).isTrue();

        // Проверяем, что полученная информация соответствует заданным параметрам
        assertThat(productDtoInfoResponse.body())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(productDto);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        // Удаляем созданный товар
        deleteProductFromCatalog(productId);
    }
}