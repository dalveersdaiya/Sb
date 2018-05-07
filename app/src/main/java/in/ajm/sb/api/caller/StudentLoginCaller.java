package in.ajm.sb.api.caller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.enums.ApiType;
import in.ajm.sb.api.model.student.Student;
import in.ajm.sb.api.params.ApiParams;
import in.ajm.sb.api.service.StudentService;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.PreferencesManager;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLoginCaller extends BaseCaller {

    public static StudentLoginCaller getInstance() {
        return new StudentLoginCaller();
    }

    @Override
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public void post(final Context context, ApiParams apiParams, final APICallback callback, final ApiType apiType) {
        super.post(context, apiParams, callback, apiType);

        final StudentService studentService = retrofit.create(StudentService.class);
        call = studentService.login(getHeaders(),apiParams.mHashMap);
        LoggerCustom.logE("Daiya", call.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    final JsonObject jsonObject = response.body();
                    final JsonObject response_Status = jsonObject.get("response_status").getAsJsonObject();
                    if (response_Status.get("response_type").getAsString().equalsIgnoreCase("success")) {
                        responseMessage = response_Status.get("response_message").getAsString();
                        responseCode = response_Status.get("response_code").getAsInt();
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Gson gson = generateGsonObj();
                                JsonObject studentDataObject = jsonObject.getAsJsonObject("response_data");
                                Student student = gson.fromJson(studentDataObject, Student.class);
                                realm.copyToRealmOrUpdate(student);

                                PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_STUDENT_ID, studentDataObject.get("id").getAsString());
                                PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_AUTH, studentDataObject.get("authentication_token").getAsString());

                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                callback.onResult("", apiType, responseCode, responseMessage);
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                LoggerCustom.printStackTrace(error);
                                callback.onResult("", apiType, responseCode, responseMessage);
                            }
                        });


                    } else {
                        callback.onResult("", apiType, INTERNAL_ERROR, responseMessage);
                    }
                } catch (Exception e) {
                    LoggerCustom.printStackTrace(e);
                    callback.onResult("", apiType, INTERNAL_ERROR, responseMessage);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                LoggerCustom.printStackTrace(t);
                if (call.isCanceled())
                    callback.onResult("Cancelled", apiType, REQUEST_CANCELLED, responseMessage);
                else
                    callback.onResult("Error", apiType, INTERNAL_ERROR, responseMessage);
            }
        });

    }


}
