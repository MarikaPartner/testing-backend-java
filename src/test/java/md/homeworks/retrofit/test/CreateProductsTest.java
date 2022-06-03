package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.Data;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;

@DisplayName("Создание новых товаров (POST /api/v1/products)")
@Data
public class CreateProductsTest extends BaseTest {
    private static ProductDto productDto;
    private List<Integer> productIdsBefore;
    private List<Integer> productIdsAfter;

    @SneakyThrows
    @BeforeEach
    void setUp() {

        // Создаем новый объект-товар
        productDto = makeProductDto(1, new Faker().food().fruit());
    }

    @SneakyThrows
    @Test
    @DisplayName("Создаётся товар  с нужными параметрами, id присваивается автоматически, в списке продуктов появляется созданный продукт")
    @Severity(SeverityLevel.CRITICAL)
    void createProductTest() {

        // Получаем список id всех товаров каталога
        productIdsBefore = getIdsAllProducts();

        // Отправляем запрос на создание нового товара
        Response<ProductDto> productDtoResponse = getProductsService().createProduct(productDto)
                .execute();
        assertThat(productDtoResponse.isSuccessful()).isTrue();

        // Получаем id созданного продукта и проверяем, что он присвоен
        Integer productId = Objects.requireNonNull(productDtoResponse.body()).getId();
        assertThat(productId).isNotNull();

        // Проверяем, что созданный товар соответствует заданному
        assertThat(productDtoResponse.body())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(productDto);

        // Получаем список id всех продуктов каталога
        productIdsAfter = getIdsAllProducts();

        // Проверяем, что созданный товар появился в списке товаров каталога
        assertThat(productIdsBefore).doesNotContain(productId);
        assertThat(productIdsAfter).contains(productId);

        // Удаляем созданный товар
        deleteProductFromCatalog(productId);
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