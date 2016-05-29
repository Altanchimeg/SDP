package com.skytel.sdp.ui.skydealer;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PostPaidPaymentFragment extends Fragment {

    String TAG = PostPaidPaymentFragment.class.getName();

    private OkHttpClient client;
    private Context mContext;
    private DataManager mDataManager;
    private PrefManager prefManager;

    // UI Widgets
    private Button mDoPaymentBtn;
    private Button mGetInvoiceBtn;
    private EditText mInvoicePhoneNumber;
    private EditText mPincode;
    private EditText mConfirmCode;

    private String balance = "";

    public PostPaidPaymentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.postpaid_payment, container, false);

        mContext = getActivity();
        mDataManager = new DataManager(mContext);
        client = new OkHttpClient();
        prefManager = new PrefManager(mContext);

        mInvoicePhoneNumber = (EditText) rootView.findViewById(R.id.invoice_phone_number);
        mPincode = (EditText) rootView.findViewById(R.id.pin_code);
        mConfirmCode = (EditText) rootView.findViewById(R.id.confirm_code);
        mGetInvoiceBtn = (Button) rootView.findViewById(R.id.get_invoice_btn);
        mDoPaymentBtn = (Button) rootView.findViewById(R.id.do_payment_btn);

        mGetInvoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    runInvoiceFunction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mDoPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    runPaymentFunction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    public void runInvoiceFunction() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_GET_INVOICE);
        url.append("?phone=" + mInvoicePhoneNumber.getText().toString());
        url.append("&pin=" + mPincode.getText().toString());

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
                    //int result_code = jsonObj.getInt("result_code");
                    //String result_msg = jsonObj.getString("result_msg");
                    String status = jsonObj.getString("status");
                    balance = jsonObj.getString("balance");
                    // Log.d(TAG, "result_code " + result_code);


                   /* if (status == Constants.RESULT_STATUS_SUCCESS) {



                        Log.d(TAG, "dealer_id " + dealer_id);
                        Log.d(TAG, "balance " + balance);

                        Log.d(TAG, "Show the success message to user");


                    } */

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPincode.setEnabled(false);
                            mInvoicePhoneNumber.setEnabled(false);
                            mGetInvoiceBtn.setEnabled(false);
                            mGetInvoiceBtn.setBackgroundColor(getResources().getColor(R.color.colorLighGreen));
                            mGetInvoiceBtn.setText(getResources().getString(R.string.successful));
                            mConfirmCode.setEnabled(true);
                            mDoPaymentBtn.setEnabled(true);
                            mDoPaymentBtn.setBackgroundResource(R.drawable.btn_blue);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void runPaymentFunction() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_DO_PAYMENT);
        url.append("?amount=" + Integer.parseInt(balance));
        url.append("&phone=" + mInvoicePhoneNumber.getText().toString());
        url.append("&pin=" + mPincode.getText().toString());
        url.append("&invoice_num=" + mConfirmCode.getText().toString());

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

                    Log.d(TAG, "result_code " + result_code);
                    Log.d(TAG, "result_mesage  " + result_msg);

                    if (result_code == Constants.RESULT_CODE_SUCCESS) {

                        String dealer_id = jsonObj.getString("dealer_id");
                        String balance = jsonObj.getString("balance");

                        Log.d(TAG, "dealer_id " + dealer_id);
                        Log.d(TAG, "balance " + balance);

                        Log.d(TAG, "Show the success message to user");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
