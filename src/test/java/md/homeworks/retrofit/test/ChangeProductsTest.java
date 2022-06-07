package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import java.util.Objects;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Изменение продуктов (PUT /api/v1/products)")
public class ChangeProductsTest extends BaseTest {
    private ProductDto productDto;
    private Integer productId;

    @SneakyThrows
    @BeforeEach
    void setUp() {

        // Создаем новый объект-товар
        productDto = makeProductDto(1, new Faker().food().fruit());

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
    @DisplayName("При изменении товара получаем измененный товар")
    @Severity(SeverityLevel.CRITICAL)
    void changeProductTest() {

        // Создаем измененный объект-товар
        ProductDto changedProductDto = makeProductDto(1, new Faker().food().ingredient());
        changedProductDto.setId(productId);

        // Отправляем запрос на изменение товара
        Response<ProductDto> changedProductDtoResponse = getProductsService().changeProduct(changedProductDto)
                .execute();
        assertThat(changedProductDtoResponse.isSuccessful()).isTrue();

        // Проверяем, что измененный товар соответствует заданному
        assertThat(changedProductDtoResponse.body())
                .isEqualTo(changedProductDto);
    }

    @SneakyThrows
    @Test
    @DisplayName("При изменении несуществующего товара получаем ошибку 400 'Product with id: {id} doesn't exist'")
    @Severity(SeverityLevel.NORMAL)
    void createProductWithNotNullIdTest() {

        // Получаем несуществующий id (находим максимальный и увеличиваем на 1)
        Integer nonExistentId = getIdsAllProducts()
                .stream()
                .mapToInt(n -> n)
                .max()
                .orElseThrow() + 1;
        productDto.setId(nonExistentId);

        // Отправляем запрос на изменение товара с несуществующим id
        Response<ProductDto> productDtoResponse = getProductsService().changeProduct(productDto)
                .execute();
        assertThat(productDtoResponse.code()).isEqualTo(400);
        assertThat(getErrorMessage(productDtoResponse)).isEqualTo("Product with id: " + nonExistentId + " doesn't exist");
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        // Удаляем созданный товар
        deleteProductFromCatalog(productId);
    }
}