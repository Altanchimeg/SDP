package com.skytel.sdp.ui.feedback;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class FeedbackFragment extends Fragment {

    String TAG = FeedbackFragment.class.getName();
    private OkHttpClient client;
    private Context mContext;
    private DataManager mDataManager;
    private PrefManager prefManager;

    // UI Widgets
    private Button mSendFeedback;
    private EditText mPhonenumber;
    private EditText mUserVoice;
    private Spinner mVoiceType;

    private int voice_type_id=1;

    public FeedbackFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback, container, false);

        mContext = getActivity();
        mDataManager = new DataManager(mContext);
        client = new OkHttpClient();
        prefManager = new PrefManager(mContext);

        mUserVoice = (EditText) rootView.findViewById(R.id.et_user_voice);
        mPhonenumber = (EditText) rootView.findViewById(R.id.et_user_phonenumber);
        mVoiceType = (Spinner) rootView.findViewById(R.id.choose_voice);
        mSendFeedback = (Button) rootView.findViewById(R.id.btn_feedback_send);

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    voice_type_id = (int) mVoiceType.getSelectedItemId() + 1;
                    runSendFeedbackFunction();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return rootView;
    }

    public void runSendFeedbackFunction() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_SEND_FEEDBACK);
        url.append("?phone=" + mPhonenumber.getText().toString());
        url.append("&comment_type=" + voice_type_id);
        url.append("&comment=" + mUserVoice.getText().toString());



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
                    Log.d(TAG, "result_msg " + result_msg);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
