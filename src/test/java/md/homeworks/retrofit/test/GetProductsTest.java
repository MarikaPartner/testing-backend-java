package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Получение списка всех товаров каталога (GET /api/v1/products)")
public class GetProductsTest extends BaseTest {
    private static List<ProductDto> productDtoList = new ArrayList<>();
    private static List<Integer> productDtoIdList = new ArrayList<>();

    @SneakyThrows
    @Test
    @DisplayName("При запросе списка товаров получаем список всех товаров каталога")
    @Severity(SeverityLevel.CRITICAL)
    void getProductInfoTest() {

        // Очищаем каталог
        cleanCatalog();

        // Создаем список объектов-товаров
        makeProductDtoList(5, 1, new Faker().food().vegetable());

        // Отправляем запросы на создание новых товаров и создаем список id созданных товаров
        for (ProductDto productDto : productDtoList) {
            Response<ProductDto> productDtoResponse = getProductsService().createProduct(productDto)
                    .execute();
            assertThat(productDtoResponse.isSuccessful()).isTrue();
            Integer productId = Objects.requireNonNull(productDtoResponse.body()).getId();
            assertThat(productId).isNotNull();
            productDtoIdList.add(productId);
        }

        // Отправляем запрос на получения списка товаров каталога и проверяем, что все товары там есть
        assertThat(getIdsAllProducts())
                .containsExactlyElementsOf(productDtoIdList);

        // Удаляем созданные товары
        for (Integer productId : productDtoIdList) {
            deleteProductFromCatalog(productId);
        }
    }
}