package com.skytel.sdp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.PrefManager;
import com.skytel.sdp.utils.ValidationChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity implements Constants {
    String TAG = LoginActivity.class.getName();

    private OkHttpClient client;
    private Context context;
    private Button mBtnLogin;
    private EditText mEtUserName;
    private EditText mEtPassword;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = this;
        client = new OkHttpClient();
        prefManager = new PrefManager(this);

/**
 * If code is running on Debug
 */

if(DEBUG)
{
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
}


        if (prefManager.getIsLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mEtUserName = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ValidationChecker.isValidationPassed(mEtUserName) && ValidationChecker.isValidationPassed(mEtPassword)) {
                        Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
                        runLoginFunction();
                    } else {
                        Toast.makeText(context, "Please fill the field!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void runLoginFunction() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(SERVER_URL);
        url.append(FUNCTION_LOGIN);
        url.append("?login=" + mEtUserName.getText().toString());
        url.append("&pass=" + mEtPassword.getText().toString());

        System.out.print(url + "\n");

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //       progressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //     progressDialog.dismiss();
                        Toast.makeText(context, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug
//                        PrefManager.getSessionInstance().setIsLoggedIn(true);
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("onResponse");

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String resp = response.body().string();
                System.out.println("resp " + resp);

                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    int result_code = jsonObj.getInt("result_code");


                    if (result_code == RESULT_CODE_SUCCESS) {
                        String auth_token = jsonObj.getString("auth_token");

                        prefManager.saveAuthToken(PREF_AUTH_TOKEN, auth_token);

                        Log.d(TAG, "result_code " + result_code);
                        Log.d(TAG, "auth_token " + auth_token);

                        prefManager.setIsLoggedIn(true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String result_msg = jsonObj.getString("result_msg");

                        Log.d(TAG, "result_code " + result_code);
                        Log.d(TAG, "result_msg " + result_msg);


                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void forgetPasswordView(View view){
        Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
        finish();
    }

}
