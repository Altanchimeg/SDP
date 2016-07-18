package com.skytel.sdp.ui.information;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.adapter.ChargeCardPackageTypeAdapter;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Altanchimeg on 7/18/2016.
 */

public class InfoNewsFragment extends Fragment{
    String TAG = InfoNewsFragment.class.getName();

    private OkHttpClient mClient;
    private Context mContext;
    private CustomProgressDialog mProgressDialog;

    private ListView mInfoNewsTypeLis;
        public InfoNewsFragment() {
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mProgressDialog = new CustomProgressDialog(getActivity());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.info_news, container, false);
            mContext = getActivity();
            mClient = new OkHttpClient();

     /*       mInfoNewsTypeLis = (ListView) rootView.findViewById(R.id.info_type_list_view);
            mInfoNewsTypeLis.setAdapter(new InfoNewsTypeAdapter(getActivity()));
            mInfoNewsTypeLis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
                }
            });*/

            return rootView;
        }
 /*   public void runChargeFunction() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_CHARGE);
        url.append("?card_type=" + mCardType.getName());
        url.append("&phone=" + mChargeCardPhoneNumber.getText().toString());
        url.append("&pin=" + mChargeCardPinCode.getText().toString());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");
        System.out.println(mPrefManager.getAuthToken());

        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader(Constants.PREF_AUTH_TOKEN, mPrefManager.getAuthToken())
                .build();

        mClient.newCall(request).enqueue(new Callback() {
                                             @Override
                                             public void onFailure(Call call, IOException e) {
                                                 mProgressDialog.dismiss();
                                                 System.out.println("onFailure");
                                                 e.printStackTrace();
                                                 getActivity().runOnUiThread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                                                         // Used for debug
                                                     }
                                                 });
                                             }

                                             @Override
                                             public void onResponse(Call call, Response response) throws IOException {
                                                 mProgressDialog.dismiss();

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

                                                     Log.d(TAG, "result_code " + result_code);

                                                     if (result_code == Constants.RESULT_CODE_SUCCESS) {

                                                         String dealer_id = jsonObj.getString("dealer_id");
                                                         final String balance = jsonObj.getString("balance");

                                                         Log.d(TAG, "dealer_id " + dealer_id);
                                                         Log.d(TAG, "balance " + balance);

                                                         mPrefManager.saveDealerBalance(balance);
                                                         if (sBalanceUpdateListener != null) {
                                                             sBalanceUpdateListener.onBalanceUpdate();
                                                         }
                                                         Log.d(TAG, "Show the success message to user");
                                                         try {
                                                             getActivity().runOnUiThread(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     Toast.makeText(mContext, "Success!", Toast.LENGTH_LONG).show();
                                                                     mChargeCardPhoneNumber.setText("");
                                                                     mChargeCardPinCode.setText("");
                                                                 }
                                                             });
                                                         } catch (Exception ex) {
                                                             ex.printStackTrace();
                                                         }

                                                     } else {
                                                         final String result_msg = jsonObj.getString("result_msg");
                                                         Log.d(TAG, "result_msg " + result_msg);
                                                         getActivity().runOnUiThread(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 Toast.makeText(mContext, "" + result_msg, Toast.LENGTH_LONG).show();
                                                                 mChargeCardPhoneNumber.setText("");
                                                                 mChargeCardPinCode.setText("");

                                                             }
                                                         });

                                                     }


                                                 } catch (JSONException e) {
                                                     getActivity().runOnUiThread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             Toast.makeText(mContext, "Алдаатай хариу ирлээ", Toast.LENGTH_LONG).show();
                                                         }
                                                     });
                                                     e.printStackTrace();

                                                 }
                                             }
                                         }

        );
    }
*/

}