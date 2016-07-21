package com.skytel.sdp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InfoNewsDetailActivity extends AppCompatActivity implements Constants {
    String TAG = InfoNewsDetailActivity.class.getName();

    private int mNewsId;

    private OkHttpClient mClient;
    private Context mContext;
    private CustomProgressDialog mProgressDialog;

    private TextView mContentTitle;
    private ImageView mContentImage;
    private TextView mContentBodyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mClient = new OkHttpClient();
        mProgressDialog = new CustomProgressDialog(this);
        setContentView(R.layout.activity_info_news_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNewsId = extras.getInt("news_id");

            Toast.makeText(this, "News ID: " + mNewsId, Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mContentTitle = (TextView) findViewById(R.id.content_title);
        mContentImage = (ImageView) findViewById(R.id.content_image);
        mContentBodyText = (TextView) findViewById(R.id.content_body);

        try {
            mProgressDialog.show();
            runGetInfoNewsDetail(mNewsId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void runGetInfoNewsDetail(int news_id) throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_SKYTEL_MN_URL);
        url.append(Constants.FUNCTION_GETINFO_NEWS_DETAIL_PART1);
        url.append(news_id);
        url.append(Constants.FUNCTION_GETINFO_NEWS_DETAIL_PART2);


        Toast.makeText(this, "URL: " + url.toString(), Toast.LENGTH_LONG).show();

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        mClient.newCall(request).enqueue(new Callback() {
                                             @Override
                                             public void onFailure(Call call, IOException e) {
                                                 mProgressDialog.dismiss();
                                                 System.out.println("onFailure");
                                                 e.printStackTrace();
                                                 runOnUiThread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                                                     }
                                                 });
                                             }

                                             @Override
                                             public void onResponse(Call call, Response response) throws IOException {
                                                 mProgressDialog.dismiss();

                                                 System.out.println("onResponse");

                                                 if (!response.isSuccessful())
                                                     throw new IOException("Unexpected code " + response);

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

                                                     //TODO: Error occured there
                                                     JSONObject jsonObjCategory = jsonObj.getJSONObject("result");

                                                     JSONObject jObjNews = jsonObjCategory.getJSONObject("content");

                                                     int newsId = jObjNews.getInt("id");
                                                     String title = jObjNews.getString("title");
                                                     String intro = jObjNews.getString("intro");
                                                     final String image = jObjNews.getString("image");
                                                     String createdAt = jObjNews.getString("createdAt");
                                                     String text = jObjNews.getString("text");
                                                     String description = jObjNews.getString("description");


                                                     Log.d(TAG, "newsId: " + newsId);
                                                     Log.d(TAG, "title: " + title);
                                                     Log.d(TAG, "intro: " + intro);
                                                     Log.d(TAG, "image: " + image);
                                                     Log.d(TAG, "createdAt: " + createdAt);
                                                     Log.d(TAG, "text: " + text);
                                                     Log.d(TAG, "description: " + description);

                                                     mContentTitle.setText(title);
                                                     runOnUiThread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             Picasso.with(mContext).load(image).into(mContentImage);
                                                         }
                                                     });
                                                     mContentBodyText.setText(Html.fromHtml(text));


                                                 } catch (JSONException e) {
                                                     runOnUiThread(new Runnable() {
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
