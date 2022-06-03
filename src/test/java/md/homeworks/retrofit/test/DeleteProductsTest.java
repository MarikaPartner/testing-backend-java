package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.Data;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import java.util.List;
import java.util.Objects;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Удаление товаров (DELETE /api/v1/products/{id})")
@Data
public class DeleteProductsTest extends BaseTest {
    private static ProductDto productDto;
    private List<Integer> productIdsBefore;
    private List<Integer> productIdsAfter;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        // Создаем новый объект-товар
        productDto = makeProductDto(1, new Faker().food().dish());
    }

    @SneakyThrows
    @Test
    @DisplayName("При удалении существующего товара он исчезает из списка товаров")
    @Severity(SeverityLevel.CRITICAL)
    void createProductTest() {

        // Отправляем запрос на создание нового товара
        Response<ProductDto> productDtoResponse = getProductsService().createProduct(productDto)
                .execute();
        assertThat(productDtoResponse.isSuccessful()).isTrue();

        // Получаем id созданного товара
        Integer productId = Objects.requireNonNull(productDtoResponse.body()).getId();
        assertThat(productId).isNotNull();

        // Получаем список id всех товаров каталога
        productIdsBefore = getIdsAllProducts();

        // Удаляем созданный товар
        deleteProductFromCatalog(productId);

        // Получаем список id всех продуктов каталога
        productIdsAfter = getIdsAllProducts();

        // Проверяем, что созданный товар исчез из списка товаров
        assertThat(productIdsBefore).contains(productId);
        assertThat(productIdsAfter).doesNotContain(productId);
    }
}