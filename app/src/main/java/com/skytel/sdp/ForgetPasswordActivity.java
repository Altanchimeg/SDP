package com.skytel.sdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skytel.sdp.utils.ConfirmDialog;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.skytel.sdp.utils.PrefManager;
import com.skytel.sdp.utils.ValidationChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements Constants {
    String TAG = LoginActivity.class.getName();

    private OkHttpClient client;
    private Context context;
    private Button mBtnForget;
    private Button mBtnRecover;
    private EditText mEtPhoneNumber;
    private EditText mEtConfirmCode;
    private EditText mEtNewPassword;
    private PrefManager prefManager;

    private LinearLayout mConfirmPassword;
    private LinearLayout mRecoverPassword;

    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        this.context = this;
        client = new OkHttpClient();
        prefManager = new PrefManager(this);
        progressDialog = new CustomProgressDialog(this);

        mConfirmPassword = (LinearLayout) findViewById(R.id.confirm_password);
        mRecoverPassword = (LinearLayout) findViewById(R.id.recover_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mEtPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mEtConfirmCode = (EditText) findViewById(R.id.confirm_code);
        mEtNewPassword = (EditText) findViewById(R.id.new_pass);

        mBtnForget = (Button) findViewById(R.id.btn_forget);
        mBtnRecover = (Button) findViewById(R.id.btn_recover);

        mBtnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ValidationChecker.isValidationPassed(mEtPhoneNumber)) {
                        Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
                        runForgetFunction();
                        progressDialog.show();
                    } else {
                        Toast.makeText(context, "Please fill the field!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBtnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ValidationChecker.isValidationPassed(mEtConfirmCode) && ValidationChecker.isValidationPassed(mEtNewPassword)) {
                        Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
                        ConfirmDialog confirmDialog = new ConfirmDialog();
                        Bundle args = new Bundle();
                        args.putInt("message", R.string.confirm);
                        args.putInt("title", R.string.confirm);

                        confirmDialog.setArguments(args);
                        confirmDialog.registerCallback(dialogConfirmListener);
                        confirmDialog.show(getFragmentManager(), "dialog");


                    } else {
                        Toast.makeText(context, "Please fill the field!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void runForgetFunction() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(SERVER_URL);
        url.append(FUNCTION_FORGET);
        url.append("?login=" + mEtPhoneNumber.getText().toString());

        System.out.print(url + "\n");

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
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
                    String result_msg = jsonObj.getString("result_msg");


                    if (result_code == RESULT_CODE_SUCCESS) {

                        Log.d(TAG, "result_code " + result_code);
                        Log.d(TAG, "resul_msg " + result_msg);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mConfirmPassword.setVisibility(View.GONE);
                                mRecoverPassword.setVisibility(View.VISIBLE);
                            }
                        });

                    } else {

                        Log.d(TAG, "result_code " + result_code);
                        Log.d(TAG, "result_msg " + result_msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void runRecoverFunction() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(SERVER_URL);
        url.append(FUNCTION_RECOVER);
        url.append("?confirmation=" + mEtConfirmCode.getText().toString());
        url.append("&login=" + mEtPhoneNumber.getText().toString());
        url.append("&newpass=" + mEtNewPassword.getText().toString());

        System.out.print(url + "\n");

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
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
                    final String result_msg = jsonObj.getString("result_msg");


                    if (result_code == RESULT_CODE_SUCCESS) {

                        Log.d(TAG, "result_code " + result_code);
                        Log.d(TAG, "resul_msg " + result_msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Successfully changed!", Toast.LENGTH_LONG).show();


                            }
                        });
                        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "" + result_msg, Toast.LENGTH_SHORT).show();
                                mEtNewPassword.setText("");
                                mEtConfirmCode.setText("");

                            }
                        });
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

    private ConfirmDialog.OnDialogConfirmListener dialogConfirmListener = new ConfirmDialog.OnDialogConfirmListener() {

        @Override
        public void onPositiveButton() {
            //  Toast.makeText(this, "Confirmed", Toast.LENGTH_LONG).show();
            try {
                runRecoverFunction();
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onNegativeButton() {

        }
    };

}
