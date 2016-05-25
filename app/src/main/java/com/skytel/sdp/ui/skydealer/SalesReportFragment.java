package com.skytel.sdp.ui.skydealer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.PrefManager;

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


public class SalesReportFragment extends Fragment implements Constants {


    String TAG = PostPaidPaymentFragment.class.getName();

    private OkHttpClient client;
    private Context mContext;
    private DataManager mDataManager;
    private PrefManager prefManager;

    public SalesReportFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.skydealer_report, container, false);


        mContext = getActivity();
        mDataManager = new DataManager(mContext);
        client = new OkHttpClient();
        prefManager = new PrefManager(mContext);

        try {
            runChargeCardReportFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    public void runChargeCardReportFunction() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_DEALER_REPORT);
        url.append("?trans_type=" + REPORT_DEALER_CHARGECARD_TYPE);
        url.append("&len=" + 10);
        url.append("&from=" + 0);
        url.append("&is_success=" + "null");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");
        System.out.println(prefManager.getAuthToken(Constants.PREF_AUTH_TOKEN) + "");

        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader("AUTH_TOKEN", prefManager.getAuthToken(Constants.PREF_AUTH_TOKEN))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //       progressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //     progressDialog.dismiss();
                        Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug
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
                    String result_msg = jsonObj.getString("result_msg");
                    String dealer_id = jsonObj.getString("dealer_id");

                    Log.d(TAG, "result_code: "+result_code);
                    Log.d(TAG, "result_msg: "+result_msg);
                    Log.d(TAG, "dealer_id: "+dealer_id);

                    JSONArray jArray = jsonObj.getJSONArray("transactions");

                    Log.d(TAG, "*****JARRAY*****" + jArray.length());
                    for(int i=0;i<5;i++) {
                        JSONObject jsonData = jArray.getJSONObject(i);

                        String date = jsonData.getString("date");
                        Boolean success = jsonData.getBoolean("success");
                        String type = jsonData.getString("type");
                        String value = jsonData.getString("value");
                        String phone = jsonData.getString("phone");

                        Log.d(TAG, "INDEX:       "+i);

                        Log.d(TAG, "date: "+date);
                        Log.d(TAG, "success: "+success);
                        Log.d(TAG, "type: "+type);
                        Log.d(TAG, "value: "+value);
                        Log.d(TAG, "phone: "+phone);


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
