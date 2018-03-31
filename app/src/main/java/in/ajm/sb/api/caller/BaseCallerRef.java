package in.ajm.sb.api.caller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import in.ajm.sb.adapter.DoubleTypeAdapter;
import in.ajm.sb.adapter.FloatTypeAdapter;
import in.ajm.sb.adapter.IntegerTypeAdapter;
import in.ajm.sb.adapter.LongTypeAdapter;
import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.enums.ApiType;
import in.ajm.sb.api.params.ApiParams;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DSD on 02/09/16.
 */

public abstract class BaseCallerRef {
    public static int INTERNAL_ERROR = 500;
    public static int REQUEST_CANCELLED = 1000;
    protected int V_INTERNAL_ERROR_FOR_NO_RECORDFOUND = 1;
    protected int loadingCount = 0;
    protected String BASE_URL = "";
    Retrofit retrofit;
    Call<JsonObject> call;

    BaseCallerRef() {
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setLenient()
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS);
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS);

//        HACK : This if condition lets you check the Build Type
//        if (!BuildConfig.FLAVOR.equals("live") && BuildConfig.DEBUG) {
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            okHttpClientBuilder.addInterceptor(interceptor).build();
//        }

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public static Gson generateGsonObj() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setLenient()
                .serializeNulls()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(Float.class, new FloatTypeAdapter())
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .registerTypeAdapter(long.class, new LongTypeAdapter())
                .registerTypeAdapter(float.class, new FloatTypeAdapter())
                .registerTypeAdapter(double.class, new DoubleTypeAdapter())
                .create();
    }

    static String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 64;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            try {
                string.append(new String(data, 0, bytesRead));
            } catch (Exception e) {

            }
        }
        gis.close();
        is.close();
        return string.toString();
    }

    private void addDefaultParams(Context context, HashMap<String, String> hashMap) {
        if (hashMap == null)
            hashMap = new HashMap<>();
//        hashMap.put("api_version", Urls.API_VERSION_PARAM);
        hashMap.put("platform", "Android");
//        hashMap.put("device_id", StringHelper.getDeviceId(context));
    }

    public void get(Context context, ApiParams apiParams, APICallback callback, ApiType apiType) {
        addDefaultParams(context, apiParams.mHashMap);
    }

    public Object postSync(Context context, ApiParams apiParams, ApiType apiType) {
        addDefaultParams(context, apiParams.mHashMap);
        return null;
    }

    void post(Context context, ApiParams apiParams, APICallback callback, ApiType apiType) {
        addDefaultParams(context, apiParams.mHashMap);
    }

    public abstract void cancel();
}
