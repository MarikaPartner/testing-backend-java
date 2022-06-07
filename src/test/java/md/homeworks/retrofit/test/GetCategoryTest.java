package md.homeworks.retrofit.test;

import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import md.homeworks.retrofit.dto.CategoryDto;
import md.homeworks.retrofit.dto.ProductDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import retrofit2.Response;
import java.util.*;
import java.util.stream.Collectors;

import static md.homeworks.retrofit.util.RetrofitUtil.getCategoryService;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Получение товаров категории по id (GET /api/v1/categories/{id})")
public class GetCategoryTest extends BaseTest {
    private static List<CategoryDto> categoryDtoList = new ArrayList<>();
    private static List<Integer> productDtoIdList = new ArrayList<>();

    @SneakyThrows
    @BeforeAll
    static void setUp() {

        // Создаем объекты-категории категорий
        categoryDtoList.add(makeCategoryDto(1, 3, new Faker().food().vegetable()));
        categoryDtoList.add(makeCategoryDto(2, 4, new Faker().commerce().productName()));

        // Очищаем каталог
        cleanCatalog();

        // Создаем в каталоге товары всех категорий, получаем их id
        for (CategoryDto category : categoryDtoList) {
            for (ProductDto productDto : category.getProducts()) {
                Response<ProductDto> productDtoResponse = getProductsService().createProduct(productDto)
                        .execute();
                assertThat(productDtoResponse.isSuccessful()).isTrue();
                productDtoIdList.add(Objects.requireNonNull(productDtoResponse.body()).getId());
            }
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @DisplayName("По существующему id получаем все товары только указанной категории")
    @Severity(SeverityLevel.CRITICAL)
    @CsvSource(value = {"1", "2"})
    void getCategoryTest(Integer id) {

        // Отправляем запрос на получение всех товаров заданной категории
        Response<CategoryDto> getCategoryResponse = getCategoryService().getCategory(id)
                .execute();
        assertThat(getCategoryResponse.isSuccessful()).isTrue();

        // Проверяем, что полученная категория: имеет заданный id, что в ней все товары категории и только этой категории
        assertThat(getCategoryResponse.body().getId()).isEqualTo(id);
        List<String> productTitles = getCategoryResponse.body().getProducts()
                .stream()
                .map(productDto -> productDto.getCategoryTitle())
                .collect(Collectors.toList());
        assertThat(productTitles).containsOnly(getCategoryResponse.body().getTitle());

        int productNumber = (categoryDtoList.stream()
                .filter(categoryDto -> categoryDto.getId() == id).findFirst().get())
                .getProducts()
                .size();
        assertThat(getCategoryResponse.body().getProducts().size()).isEqualTo(productNumber);
    }

    @SneakyThrows
    @Test
    @DisplayName("По несуществуещему id получаем ошибку 404 'Unable to find category with id: {id}'")
    @Severity(SeverityLevel.NORMAL)
    void getCategoryByNonExistentIdTest() {

        // Получаем несуществующий id (находим максимальный и увеличиваем на 1)
        Integer nonExistentId = getIdsAllProducts()
                .stream()
                .mapToInt(n -> n)
                .max()
                .getAsInt() + 1;

        // Отправляем запрос на получение всех товаров несуществующей категории и проверяем ошибку
        Response<CategoryDto> categoryDtoResponse = getCategoryService().getCategory(nonExistentId)
                .execute();
        assertThat(categoryDtoResponse.code()).isEqualTo(404);
        assertThat(getErrorMessage(categoryDtoResponse)).isEqualTo("Unable to find category with id: " + nonExistentId);
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        // Удаляем созданные товары
        for (Integer productId : productDtoIdList) {
            deleteProductFromCatalog(productId);
        }
    }
}