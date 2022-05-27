package md.homeworks.restassured.extensions;

import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD}) // Указываем, что аннотация @Retention может использоваться над классами и методами
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({AllureJunit5.class, SpoonApiTestExtension.class, CommonApiTestExtension.class}) // Аннотация указывает, что SpoonApiTest включает указанные признаки
public @interface SpoonApiTest {

}