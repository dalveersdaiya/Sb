package in.ajm.sb.notes;

public class Notes {
    /**
     * //    Hack : How to use SmS Verify catcher
     * NOTE : To get sms verification you use SMSVerify API by Google
     *
     * public class MainActivity extends AppCompatActivity {
     private SmsVerifyCatcher smsVerifyCatcher;

     @Override protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     //init views
     final EditText etCode = (EditText) findViewById(R.id.et_code);
     final Button btnVerify = (Button) findViewById(R.id.btn_verify);

     //init SmsVerifyCatcher
     smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
     @Override public void onSmsCatch(String message) {
     String code = parseCode(message);//Parse verification code
     etCode.setText(code);//set code in edit text
     //then you can send verification code to server
     }
     });

     //set phone number filter if needed
     smsVerifyCatcher.setPhoneNumberFilter("777");
     //smsVerifyCatcher.setFilter("regexp");

     //button for sending verification code manual
     btnVerify.setOnClickListener(new View.OnClickListener() {
     @Override public void onClick(View v) {
     //send verification code to server
     }
     });
     }

      * Parse verification code
      *
      * @param message sms message
     * @return only four numbers from massage string
    private String parseCode(String message) {
    Pattern p = Pattern.compile("\\b\\d{4}\\b");
    Matcher m = p.matcher(message);
    String code = "";
    while (m.find()) {
    code = m.group(0);
    }
    return code;
    }

     @Override protected void onStart() {
     super.onStart();
     smsVerifyCatcher.onStart();
     }

     @Override protected void onStop() {
     super.onStop();
     smsVerifyCatcher.onStop();
     }

      * need for Android 6 real time permissions
     @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
     }
     }

     *
     */

//    **************

    /**
     * Example call device api
     * <p>
     * private void callApi()
     * {
     * String vesioncode = Build.VERSION.RELEASE;
     * String deviceType = Build.VERSION.SDK_INT + "";
     * HashMap<String, String> hashMap = new HashMap<>();
     * hashMap.put("push_token", PreferencesManager.getPreferenceByKey(LoginActivity.this, AppConfigs.PREFERENCE_PUSH_TOKEN));
     * try
     * {
     * if (Device.get().getUserId() == null && Device.get().getOrgId() == null)
     * {
     * hashMap.put("org_id", "");
     * hashMap.put("user_id", "");
     * } else
     * {
     * hashMap.put("org_id", Device.get().getOrgId());
     * hashMap.put("user_id", Device.get().getUserId());
     * }
     * } catch (Exception e)
     * {
     * hashMap.put("org_id", "");
     * hashMap.put("user_id", "");
     * }
     * <p>
     * hashMap.put("device_type", deviceType);
     * hashMap.put("device_os", "ANDROID");
     * hashMap.put("device_version", vesioncode);
     * hashMap.put("lat", "");
     * hashMap.put("lng", "");
     * <p>
     * ApiParams apiParams = new ApiParams();
     * apiParams.mHashMap = hashMap;
     * apiParams.orgId = getSelectedOrgId();
     * DeviceCaller.instance().post(LoginActivity.this, apiParams, this, ApiType.REGISTER);
     * }
     */

}
