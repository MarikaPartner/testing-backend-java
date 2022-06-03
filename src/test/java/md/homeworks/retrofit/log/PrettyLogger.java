package md.homeworks.retrofit.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

public class PrettyLogger implements HttpLoggingInterceptor.Logger {
    @SneakyThrows
    @Override
    public void log(String s) {                            // s - полученное сообщение, например, объект json
        ObjectMapper objectMapper = new ObjectMapper();    // Обеспечивает серриализацию и десерриализацию
        String trimmedString = s.trim();

        // Если полученное сообщение является json - выводим его в красивом виде,
        // если приходит не json - выводим как есть
        if ((trimmedString.startsWith("{") && trimmedString.endsWith("}"))
                || (trimmedString.startsWith("[") && trimmedString.endsWith("]"))) {
            Object object = objectMapper.readValue(s, Object.class);
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            Platform.get().log(Platform.INFO, prettyJson, null);
        } else {
            Platform.get().log(Platform.INFO, s, null);
        }
    }
}
