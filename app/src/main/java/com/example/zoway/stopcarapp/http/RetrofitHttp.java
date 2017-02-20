package com.example.zoway.stopcarapp.http;


import android.content.Context;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.zoway.stopcarapp.adapter.MainRecyclerAdapter;
import com.example.zoway.stopcarapp.api.config.Config;
import com.example.zoway.stopcarapp.bean.ParkingOrderListBean;
import com.example.zoway.stopcarapp.bean.PartSeatBean;
import com.example.zoway.stopcarapp.bean.UIsBean;
import com.example.zoway.stopcarapp.service.ParkingWebSocket;
import com.example.zoway.stopcarapp.util.SharedPreferencesUtils;

import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Platform;
import okhttp3.internal.http.HttpEngine;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static okhttp3.internal.Platform.INFO;


/**
 * Created by Administrator on 2016/11/29.
 * 网络工具
 *
 *
 */
public class RetrofitHttp {
    private static Retrofit retrofit = null;
    private static ParkingWebSocket mWebSocket;

    public static Retrofit getnstance(final Context context){
        if (retrofit == null){

            //设置数据
            String host = (String) SharedPreferencesUtils.getParam(context, Config.AppHost, Config.serverAppHost);
            String path = (String) SharedPreferencesUtils.getParam(context, Config.AppPath, Config.serverAppPath);
            String ip = (String) SharedPreferencesUtils.getParam(context,Config.IP,Config.server_ip);
            Integer port = (Integer) SharedPreferencesUtils.getParam(context,Config.PORT,Config.serverPort);

            //打印

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                @Override
//                public void log(String message) {
//                    //打印retrofit日志
//                    Log.i("RetrofitLog","retrofitBack = "+message);
//                }
//            });
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            CookieJar cookieJar = new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
//                    SharedPreferencesUtils.setParam(context,"cookies",cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
//                    List<Cookie> cookies =  (ArrayList<Cookie>)SharedPreferencesUtils.getParam(context, "cookies", null);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            };

            //代理
            Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,port));

//            ReadCookiesInterceptor readCookiesInterceptor = new ReadCookiesInterceptor(context);
//            SaveCookiesInterceptor saveCookiesInterceptor = new SaveCookiesInterceptor(context);

            OkHttpClient build = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)

                    .addInterceptor(httpLoggingInterceptor)
//                    .addInterceptor(readCookiesInterceptor)
//                    .addInterceptor(saveCookiesInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String scheme = chain.request().url().scheme();
                            Request.Builder builder = chain.request()
                                    .newBuilder()
                                    .addHeader("Content-Type", "application/encrypt-json");
                            return chain.proceed(builder.build());
                        }
                    })
                    .proxy(proxy)
                    .connectTimeout(8 * 3600, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(8 * 3600, TimeUnit.SECONDS)
                    .writeTimeout(8 * 3600, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();


            retrofit = new Retrofit.Builder()
                    .client(build)
                    .baseUrl("http://"+host+"/"+path+"/")
                    .addConverterFactory(new ToStringConverterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            mWebSocket = new ParkingWebSocket(build);

        }

        return retrofit;
    }


    public static void openWebSocket(PartSeatBean partSeatBean, ParkingOrderListBean parkingOrderListBean, MainRecyclerAdapter adapter){
        if (mWebSocket!=null){
            mWebSocket.openWebSocket(partSeatBean,parkingOrderListBean,adapter);
        }
    }

    public static void openWebSocket(UIsBean uIsBean){
        if (mWebSocket!=null){
            mWebSocket.openWebSocket(uIsBean);
        }
    }


    public static void stopWebSocket(){
        if(mWebSocket!=null){
            mWebSocket.closeWebSocket();
        }
    }
    //格式
    public static class ToStringConverterFactory extends Converter.Factory {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/encrypt-json");


        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (String.class.equals(type)) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        return value.string();
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

            if (String.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, value);
                        return requestBody;
                    }
                };
            }
            return null;
        }
    }

    //读取cookie
    public static class ReadCookiesInterceptor implements Interceptor {

        private Context context;

        public ReadCookiesInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            HashSet<String> preferences = (HashSet) getDefaultPreferences(context).getStringSet("cookie",new HashSet<String>());
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }

            return chain.proceed(builder.build());
        }

    }
    //写入cookie
    public static class SaveCookiesInterceptor implements Interceptor {

        private Context context;

        public SaveCookiesInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                getDefaultPreferences(context).edit()
                        .putStringSet("cookie", cookies)
                        .apply();
            }

            return originalResponse;
        }

    }
    //cookie地址
    public static SharedPreferences getDefaultPreferences(Context context){
        return context.getSharedPreferences(SharedPreferencesUtils.FILE_NAME,Context.MODE_PRIVATE);
    }
   //打印
    public static class HttpLoggingInterceptor implements Interceptor {
        private static final Charset UTF8 = Charset.forName("UTF-8");

        public enum Level {
            /** No logs. */
            NONE, /**
             * Logs request and response lines.
             *
             * <p>Example:
             * <pre>{@code
             * --> POST /greeting http/1.1 (3-byte body)
             *
             * <-- 200 OK (22ms, 6-byte body)
             * }</pre>
             */
            BASIC, /**
             * Logs request and response lines and their respective headers.
             *
             * <p>Example:
             * <pre>{@code
             * --> POST /greeting http/1.1
             * Host: example.com
             * Content-Type: plain/text
             * Content-Length: 3
             * --> END POST
             *
             * <-- 200 OK (22ms)
             * Content-Type: plain/text
             * Content-Length: 6
             * <-- END HTTP
             * }</pre>
             */
            HEADERS, /**
             * Logs request and response lines and their respective headers and bodies (if present).
             *
             * <p>Example:
             * <pre>{@code
             * --> POST /greeting http/1.1
             * Host: example.com
             * Content-Type: plain/text
             * Content-Length: 3
             *
             * Hi?
             * --> END GET
             *
             * <-- 200 OK (22ms)
             * Content-Type: plain/text
             * Content-Length: 6
             *
             * Hello!
             * <-- END HTTP
             * }</pre>
             */
            BODY
        }

        public interface Logger {
            void log(String message);

            /** A {@link Logger} defaults output appropriate for the current platform. */
            Logger DEFAULT = new Logger() {
                @Override
                public void log(String message) {
                    Platform.get().log(INFO, message, null);
                    Log.i("RetrofitLog","retrofitBack = "+message);
                }
            };
        }

        public HttpLoggingInterceptor() {
            this(Logger.DEFAULT);
        }

        public HttpLoggingInterceptor(Logger logger) {
            this.logger = logger;
        }

        private final Logger logger;

        private volatile Level level = Level.NONE;

        /** Change the level at which this interceptor logs. */
        public HttpLoggingInterceptor setLevel(Level level) {
            if (level == null)
                throw new NullPointerException("level == null. Use Level.NONE instead.");
            this.level = level;
            return this;

        }

        public Level getLevel() {
            return level;
        }

        @Override
        public Response intercept(Chain chain) throws IOException, UnsupportedCharsetException {
            Level level = this.level;

            Request request = chain.request();
            if (level == Level.NONE) {
                return chain.proceed(request);
            }

            boolean logBody = level == Level.BODY;
            boolean logHeaders = logBody || level == Level.HEADERS;

            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;

            Connection connection = chain.connection();
            Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
            String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
            if (!logHeaders && hasRequestBody) {
                requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
            }
            logger.log(requestStartMessage);

            if (logHeaders) {
                if (hasRequestBody) {
                    // Request body headers are only present when installed as a
                    // network interceptor. Force
                    // them to be included (when available) so there values are
                    // known.
                    if (requestBody.contentType() != null) {
                        logger.log("Content-Type: " + requestBody.contentType());
                    }
                    if (requestBody.contentLength() != -1) {
                        logger.log("Content-Length: " + requestBody.contentLength());
                    }
                }

                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    // Skip headers from the request body as they are explicitly
                    // logged above.
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        logger.log(name + ": " + headers.value(i));
                    }
                }

                if (!logBody || !hasRequestBody) {
                    logger.log("--> END " + request.method());
                } else if (bodyEncoded(request.headers())) {
                    logger.log("--> END " + request.method() + " (encoded body omitted)");

                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    logger.log("");
                    if (isPlaintext(buffer)) {
                        logger.log(buffer.readString(charset));
                        logger.log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                    } else {
                        logger.log("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                    }
                }
            }

            long startNs = System.nanoTime();
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                logger.log("<-- HTTP FAILED: " + e);
                throw e;
            }
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            if (response.body() != null) {
                ResponseBody responseBody = response.body();
                long contentLength = responseBody.contentLength();
                String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
                logger.log("<-- " + response.code() + ' ' + response.message() + ' ' + response.request().url() + " (" + tookMs + "ms"
                        + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

                if (logHeaders) {
                    Headers headers = response.headers();
                    for (int i = 0, count = headers.size(); i < count; i++) {
                        logger.log(headers.name(i) + ": " + headers.value(i));
                    }

                    if (!logBody || !HttpEngine.hasBody(response)) {
                        logger.log("<-- END HTTP");
                    } else if (bodyEncoded(response.headers())) {
                        logger.log("<-- END HTTP (encoded body omitted)");
                    } else {
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE); // Buffer the entire
                        // body.
                        Buffer buffer = source.buffer();

                        Charset charset = UTF8;
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            charset = contentType.charset(UTF8);
                        }

                        if (!isPlaintext(buffer)) {
                            logger.log("");
                            logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                            return response;
                        }

                        if (contentLength != 0) {
                            logger.log("");
                            logger.log(buffer.clone().readString(charset));
                        }

                        logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
                    }
                }
            }

            return response;
        }

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        static boolean isPlaintext(Buffer buffer) throws EOFException {
            try {
                Buffer prefix = new Buffer();
                long byteCount = buffer.size() < 64 ? buffer.size() : 64;
                buffer.copyTo(prefix, 0, byteCount);
                for (int i = 0; i < 16; i++) {
                    if (prefix.exhausted()) {
                        break;
                    }
                    int codePoint = prefix.readUtf8CodePoint();
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false;
                    }
                }
                return true;
            } catch (EOFException e) {
                return false; // Truncated UTF-8 sequence.
            }
        }

        private boolean bodyEncoded(Headers headers) {
            String contentEncoding = headers.get("Content-Encoding");
            return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
        }
    }

}
