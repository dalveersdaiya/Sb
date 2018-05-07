package in.ajm.sb.api.service;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface TeacherService {

    /**
     * Without Headers
     */
//    @FormUrlEncoded
//    @POST("students/login")
//    Call<JsonObject> login(@FieldMap Map<String, String> fields);

    /**
     * For Headers
     * @param headers
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST("teachers/login")
    Call<JsonObject> login(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> fields);
}
