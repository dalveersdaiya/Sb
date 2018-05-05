package in.ajm.sb.api.caller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.enums.ApiType;
import in.ajm.sb.api.model.teacher.Teacher;
import in.ajm.sb.api.params.ApiParams;
import in.ajm.sb.api.service.TeacherService;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.PreferencesManager;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherLoginCaller extends BaseCaller {

    public static TeacherLoginCaller getInstance() {
        return new TeacherLoginCaller();
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

        final TeacherService teacherService = retrofit.create(TeacherService.class);
        call = teacherService.login(getHeaders(), apiParams.mHashMap);
        LoggerCustom.logE("Daiya", call.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    final JsonObject responseObject = response.body();
                    if (responseObject.get("type").getAsString().equalsIgnoreCase("success")) {
                        responseMessage = responseObject.get("message").getAsString();

                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Gson gson = generateGsonObj();
                                JsonObject teacherDataObject = responseObject.getAsJsonObject("data");
                                Teacher teacher = gson.fromJson(teacherDataObject, Teacher.class);
                                realm.copyToRealmOrUpdate(teacher);

                                PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_STUDENT_ID, teacherDataObject.get("id").getAsString());
                                PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_AUTH, teacherDataObject.get("authentication_token").getAsString());

                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                callback.onResult("", apiType, 0, responseMessage);
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                LoggerCustom.printStackTrace(error);
                                callback.onResult("", apiType, 0, responseMessage);
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
