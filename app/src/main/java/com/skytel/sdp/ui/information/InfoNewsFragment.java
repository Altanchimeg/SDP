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
import com.skytel.sdp.adapter.InfoNewsTypeAdapter;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.entities.InfoNewsType;
import com.skytel.sdp.entities.SalesReport;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Altanchimeg on 7/18/2016.
 */

public class InfoNewsFragment extends Fragment {
    String TAG = InfoNewsFragment.class.getName();

    private OkHttpClient mClient;
    private Context mContext;
    private CustomProgressDialog mProgressDialog;

    private ListView mInfoNewsTypeListview;
    private ArrayList<InfoNewsType> mInfoNewsTypeArrayList;

    public InfoNewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new CustomProgressDialog(getActivity());
        mInfoNewsTypeArrayList = new ArrayList<InfoNewsType>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_news, container, false);
        mContext = getActivity();
        mClient = new OkHttpClient();

        try {
            mProgressDialog.show();
            runGetInfoNewsList(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mInfoNewsTypeListview = (ListView) rootView.findViewById(R.id.info_type_list_view);
        mInfoNewsTypeListview.setAdapter(new InfoNewsTypeAdapter(getActivity(), mInfoNewsTypeArrayList));
        mInfoNewsTypeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "" + position, Toast.LENGTH_LONG).show();

                try {
                    mProgressDialog.show();
                    runGetInfoNewsList(mInfoNewsTypeArrayList.get(position).getCategoryId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    public void runGetInfoNewsList(int categoryId) throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_SKYTEL_MN_URL);
        url.append(Constants.FUNCTION_GET_INFO_NEWS_TYPE);
        url.append("?category=" + categoryId);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });


        Request request = new Request.Builder()
                .url(url.toString())
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
                                                     int error_code = jsonObj.getInt("error_code");
                                                     String message = jsonObj.getString("message");


                                                     Log.d(TAG, "error_code: " + error_code);
                                                     Log.d(TAG, "message: " + message);


                                                     JSONObject jsonObjCategory = jsonObj.getJSONObject("result");
                                                     JSONArray jArray = jsonObjCategory.getJSONArray("categoryList");
                                                     Log.d(TAG, "*****JARRAY*****" + jArray.length());
                                                     mInfoNewsTypeArrayList.clear();
                                                     for (int i = 0; i < jArray.length(); i++) {
                                                         JSONObject jsonData = jArray.getJSONObject(i);

                                                         int categoryId = jsonData.getInt("id");
                                                         String name = jsonData.getString("name");

                                                         Log.d(TAG, "INDEX:       " + i);

                                                         Log.d(TAG, "categoryId: " + categoryId);
                                                         Log.d(TAG, "name: " + name);

                                                         InfoNewsType infoNewsType = new InfoNewsType();
                                                         // DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                         infoNewsType.setId(i);
                                                         infoNewsType.setCategoryId(categoryId);
                                                         infoNewsType.setName(name);


                                                         mInfoNewsTypeArrayList.add(i, infoNewsType);
                                                     }
                                                    //TODO: get News info list

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


}