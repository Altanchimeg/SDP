package com.skytel.sdp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skytel.sdp.utils.Constants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
    private OkHttpClient client;
    private Context context;
    private Button mBtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = this;
        client = new OkHttpClient();

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    runLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void runLogin() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL+".login.json?login=91919199&pass=asd1234");

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

/*
                try {
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj2 = jsonarray.getJSONObject(i);
                        String f_name = obj2.getString("f_name");
                        String cKal = obj2.getString("cKal");
                        String duration = obj2.getString("duration");
                        // String date = obj2.getString("date");
                        Winner winner = new Winner();
                        winner.setFbName(f_name);
                        winner.setcKal(cKal);
                        winner.setDuration(duration);
                        winner.setDate(new Date());

                        dataManager.createNewWinner(winner);

                        Log.d(TAG, "f_name " + f_name);
                        Log.d(TAG, "cKal " + cKal);
                        Log.d(TAG, "duration " + duration);
                        //  Log.d(TAG, "date " + date);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new ChallengeWinnerAdapter(getActivity()));
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/

                if (resp.equals("1")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //     progressDialog.dismiss();
                            Toast.makeText(context, "Invalid login!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

}
