package com.skytel.sdp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.MaskFilter;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skytel.sdp.entities.Phonenumber;
import com.skytel.sdp.utils.ConfirmDialog;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.skytel.sdp.utils.PrefManager;
import com.skytel.sdp.utils.Utility;
import com.skytel.sdp.utils.ValidationChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NumberUserInfoActivity extends AppCompatActivity implements Constants {
    String TAG = NumberUserInfoActivity.class.getName();

    private String mReservationId;
    private String mRegisterNumber;
    private String mPhoneNumber;

    private EditText mLastName;
    private EditText mFirstName;
    private EditText mRegistrationNumberEt;
    private EditText mSimCardSerial;
    private EditText mHobby;
    private EditText mJob;
    private EditText mContactNumber;
    private EditText mDescription;
    private EditText mChosenNumber;

    private Button mOrderUserInfo;

    private ImageView mFrontImage;
    private ImageView mBackImage;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private boolean isFirst = true;

    private PrefManager mPrefManager;
    private OkHttpClient mClient;
    private ConfirmDialog mConfirmDialog;
    private CustomProgressDialog mProgressDialog;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuserinfo);

        mPrefManager = new PrefManager(this);
        mClient = new OkHttpClient();
        mContext = this;

        mConfirmDialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("message", R.string.confirm);
        args.putInt("title", R.string.confirm);

        mConfirmDialog.setArguments(args);
        mConfirmDialog.registerCallback(dialogConfirmListener);
        mProgressDialog = new CustomProgressDialog(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReservationId = extras.getString("reservation_id");
            mRegisterNumber= extras.getString("register_number");
            mPhoneNumber = extras.getString("phone_number");

            Toast.makeText(this, "Researvation ID: "+ mReservationId,Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mLastName = (EditText) findViewById(R.id.last_name);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mRegistrationNumberEt = (EditText) findViewById(R.id.reg_number);
        mSimCardSerial = (EditText) findViewById(R.id.sim_card_serial);
        mHobby = (EditText) findViewById(R.id.hobby);
        mJob = (EditText) findViewById(R.id.job);
        mContactNumber = (EditText) findViewById(R.id.contact_number);
        mDescription = (EditText) findViewById(R.id.description_order);
        mChosenNumber = (EditText) findViewById(R.id.chosen_number);

        mFrontImage = (ImageView) findViewById(R.id.img_front);
        mBackImage = (ImageView) findViewById(R.id.img_back);

        mOrderUserInfo = (Button) findViewById(R.id.btn_order);

        mOrderUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: imageview chosen validation check needed
                if(ValidationChecker.isValidationPassed(mLastName) && ValidationChecker.isValidationPassed(mFirstName) && ValidationChecker.isValidationPassed(mRegistrationNumberEt) &&
                        ValidationChecker.isValidationPassed(mSimCardSerial) && ValidationChecker.isValidationPassed(mHobby) && ValidationChecker.isValidationPassed(mJob) &&
                        ValidationChecker.isValidationPassed(mContactNumber) && ValidationChecker.isValidationPassed(mChosenNumber) && ValidationChecker.isImageChosen(mFrontImage) &&
                        ValidationChecker.isImageChosen(mBackImage)){

                    mConfirmDialog.show(getFragmentManager(), "dialog");

                }
                else{
                    Toast.makeText(mContext, "Please fill fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegistrationNumberEt.setText(mRegisterNumber);
        mChosenNumber.setText(mPhoneNumber);

        mFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = true;
                selectImage();
            }
        });
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = false;
                selectImage();
            }
        });

    }

    private void runSendUserInfoDetailInfo() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_NEW_NUMBER_DETAIL);
        url.append("?reservation_id="+mReservationId);
        url.append("&last_name="+mLastName.getText().toString());
        url.append("&first_name="+mFirstName.getText().toString());
        url.append("&sim_serial="+mSimCardSerial.getText().toString());
        url.append("&hobby="+mHobby.getText().toString());
        url.append("&work="+mJob.getText().toString());
        url.append("&contact="+mContactNumber.getText().toString());
        url.append("&description="+mDescription.getText().toString());

        Toast.makeText(this, "URL: " + url.toString(), Toast.LENGTH_LONG).show();

        System.out.print(url + "\n");
        System.out.println(mPrefManager.getAuthToken());

        RequestBody formBody = new FormBody.Builder()
                //TODO: photo path send needed
                .add("photo1_path", "")
                .add("photo2_path", "")
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader(Constants.PREF_AUTH_TOKEN, mPrefManager.getAuthToken())
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();

                Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug

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
                    int result_code = jsonObj.getInt("result_code");
                    String result_msg = jsonObj.getString("result_msg");

                    Log.d(TAG, "result_code " + result_code);
                    Log.d(TAG, "result_msg " + result_msg);
                    if (result_code != RESULT_CODE_SUCCESS){
                        //TODO: that toast is exception  < java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()>
                        //Toast.makeText(mContext, "Result: "+result_msg, Toast.LENGTH_SHORT).show();
                        //TODO: if complete GO TO number reservation window

                    }


                } catch (JSONException e) {
                    Toast.makeText(mContext, "Алдаатай хариу ирлээ", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(NumberUserInfoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(NumberUserInfoActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isFirst)
            mFrontImage.setImageBitmap(thumbnail);
        else
            mBackImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(isFirst)
            mFrontImage.setImageBitmap(bm);
        else
            mBackImage.setImageBitmap(bm);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private ConfirmDialog.OnDialogConfirmListener dialogConfirmListener = new ConfirmDialog.OnDialogConfirmListener() {

        @Override
        public void onPositiveButton() {
            try {
                mProgressDialog.show();
                runSendUserInfoDetailInfo();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onNegativeButton() {

        }
    };

}
