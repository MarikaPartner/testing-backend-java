package md.homeworks.retrofit.log;


import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log.info(request.toString());
        Response response = chain.proceed(request);
        log.info(response.toString());
        return response;
    }
}
