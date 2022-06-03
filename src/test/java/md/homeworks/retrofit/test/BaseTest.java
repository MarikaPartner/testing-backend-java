package md.homeworks.retrofit.test;

import md.homeworks.retrofit.dto.CategoryDto;
import md.homeworks.retrofit.dto.ProductDto;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static md.homeworks.retrofit.util.RetrofitUtil.getCategoryService;
import static md.homeworks.retrofit.util.RetrofitUtil.getProductsService;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseTest {

    // Создать объект-категорию
    public static CategoryDto makeCategoryDto(int categoryId, int productDtoNumber, String productTitlesType) throws IOException {
        return new CategoryDto()
                .withId(categoryId)
                .withProducts(makeProductDtoList(productDtoNumber, categoryId, productTitlesType));
    }

    // Создать объект-товар
    public static ProductDto makeProductDto(int categoryId, String titleType) throws IOException {
        return new ProductDto().withCategoryTitle(getCategoryService()
                        .getCategory(categoryId)
                        .execute()
                        .body()
                        .getTitle())
                .withTitle(titleType)
                .withPrice((int) (Math.random() * 500) + 10);
    }

    // Создать список объектов-товаров
    public static List<ProductDto> makeProductDtoList(int productDtoNumber, int categoryId, String titleType) throws IOException {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (int i = 0; i < productDtoNumber; i++) {
            productDtoList.add(makeProductDto(categoryId, titleType));
        }
        return productDtoList;
    }

    // Получить список id всех товаров каталога
    public static List<Integer> getIdsAllProducts() throws IOException {
        return getProductsService().getProducts().execute()
                .body()
                .stream()
                .map(product -> product.getId())
                .toList();
    }

    // Удалить товар из каталога
    public static void deleteProductFromCatalog(Integer productId) throws IOException {
        assertThat(getProductsService().deleteProduct(productId)
                .execute()
                .isSuccessful())
                .isTrue();
    }

    // Очистить каталог
    public static void cleanCatalog() throws IOException {
        List<Integer> productIds = getIdsAllProducts();  // Получаем список id всех продуктов каталога
        for (Integer productId : productIds) {           // Удаляем все товары из каталога
            deleteProductFromCatalog(productId);
        }
        assertThat(getIdsAllProducts()).isEmpty();       // Проверяем, что список пуст
    }

    // Получить сообщение об ошибке
    public static String getErrorMessage(Response response) throws IOException {
        String errorBody = response.errorBody().string();
        return errorBody.substring(errorBody.indexOf("message") + 10, errorBody.indexOf("timestamp")- 3);
    }
}