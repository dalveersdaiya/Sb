package in.ajm.sb.api.service;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by DSD on 02/09/16.
 */

public interface DeviceService {
    @FormUrlEncoded
    @POST("registerdevice")
    Call<JsonObject> register(@FieldMap Map<String, String> fields);
}
