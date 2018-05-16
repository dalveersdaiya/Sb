package in.ajm.sb.api.caller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.enums.ApiType;
import in.ajm.sb.api.model.UserCredentials;
import in.ajm.sb.api.model.schooladmin.SchoolAdmin;
import in.ajm.sb.api.params.ApiParams;
import in.ajm.sb.api.service.SchoolAdminService;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.PreferencesManager;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolAdminSignUpCaller extends BaseCaller {

    public static SchoolAdminSignUpCaller getInstance() {
        return new SchoolAdminSignUpCaller();
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

        final SchoolAdminService schoolAdminService = retrofit.create(SchoolAdminService.class);
        call = schoolAdminService.signUp(getHeaders(), apiParams.mHashMap);
        LoggerCustom.logE(TAG, call.toString());
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
                                JsonObject schoolAdminDataObject = jsonObject.getAsJsonObject("response_data");
                                SchoolAdmin schoolAdmin = gson.fromJson(schoolAdminDataObject, SchoolAdmin.class);
                                realm.copyToRealmOrUpdate(schoolAdmin);

                                PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_STUDENT_ID, schoolAdminDataObject.get("id").getAsString());
                                PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_AUTH, schoolAdminDataObject.get("authentication_token").getAsString());

                                UserCredentials userCredentials = new UserCredentials();
                                userCredentials.setUserId(schoolAdminDataObject.get("id").getAsString());
                                userCredentials.setSelected(true);
                                userCredentials.setUserImage("file:///storage/emulated/0/Android/data/in.ajm.sb.beta/temp/42A91CDCCFA4455587AC1FC276A186D5.jpg");
                                userCredentials.setUserType(String.valueOf(AppConfigs.SCHOOL_ADMIN_TYPE));
                                realm.copyToRealmOrUpdate(userCredentials);

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
