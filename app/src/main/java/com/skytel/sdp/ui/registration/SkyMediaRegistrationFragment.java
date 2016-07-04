package com.skytel.sdp.ui.registration;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.entities.DealerChannelType;
import com.skytel.sdp.utils.ConfirmDialog;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.skytel.sdp.utils.PrefManager;
import com.skytel.sdp.utils.Utility;
import com.skytel.sdp.utils.ValidationChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SkyMediaRegistrationFragment extends Fragment implements Constants {

    String TAG = SkyMediaRegistrationFragment.class.getName();
    private CustomProgressDialog mProgressDialog;
    private Context mContext;
    private PrefManager mPrefManager;
    private OkHttpClient mClient;
    private ConfirmDialog mConfirmDialog;

    private EditText mLastName;
    private EditText mFirstName;
    private EditText mRegNumber;
    private EditText mAddressDetail;
    private EditText mAimagCity;
    private EditText mSumDistrict;
    private EditText mKhorooBag;
    private EditText mBuildingStreet;
    private EditText mDoorFence;
    private EditText mDoorNumber;
    private EditText mContactNumber;
    private EditText mOrderDesc;

    private Button mSendOrder;

    private ImageView mFrontImage;
    private ImageView mBackImage;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private boolean isFirst = true;

    public SkyMediaRegistrationFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.skymedia_registration, container, false);

        mContext = getActivity();
        mProgressDialog = new CustomProgressDialog(getActivity());
        mPrefManager = new PrefManager(mContext);
        mClient = new OkHttpClient();

        mConfirmDialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("message", R.string.confirm);
        args.putInt("title", R.string.confirm);
        mConfirmDialog.setArguments(args);
        mConfirmDialog.registerCallback(dialogConfirmListener);

        mAddressDetail = (EditText) rootView.findViewById(R.id.address_detail);
        mLastName = (EditText) rootView.findViewById(R.id.last_name);
        mFirstName = (EditText) rootView.findViewById(R.id.first_name);
        mContactNumber = (EditText) rootView.findViewById(R.id.contact_number);
        mSendOrder = (Button) rootView.findViewById(R.id.send_order);
        mRegNumber = (EditText) rootView.findViewById(R.id.reg_number);
        mOrderDesc = (EditText) rootView.findViewById(R.id.order_desc);
        mAimagCity = (EditText) rootView.findViewById(R.id.aimag_city);
        mSumDistrict = (EditText) rootView.findViewById(R.id.sum_district);
        mKhorooBag = (EditText) rootView.findViewById(R.id.bag_khoroo);
        mBuildingStreet = (EditText) rootView.findViewById(R.id.building_street);
        mDoorFence = (EditText) rootView.findViewById(R.id.door_fence);
        mDoorNumber = (EditText) rootView.findViewById(R.id.door_number);

        mFrontImage = (ImageView) rootView.findViewById(R.id.img_front);
        mBackImage = (ImageView) rootView.findViewById(R.id.img_back);

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

        mSendOrder.setOnClickListener(new View.OnClickListener() {
            //TODO: check image chosen
            @Override
            public void onClick(View v) {
                if(ValidationChecker.isValidationPassed(mAddressDetail) && ValidationChecker.isValidationPassed(mLastName) && ValidationChecker.isValidationPassed(mFirstName) &&
                        ValidationChecker.isValidationPassed(mContactNumber) && ValidationChecker.isValidationPassed(mRegNumber) && ValidationChecker.isValidationPassed(mAimagCity) &&
                        ValidationChecker.isValidationPassed(mSumDistrict) && ValidationChecker.isValidationPassed(mKhorooBag) && ValidationChecker.isValidationPassed(mBuildingStreet) &&
                        ValidationChecker.isValidationPassed(mDoorFence) && ValidationChecker.isValidationPassed(mDoorNumber)){

                    mConfirmDialog.show(getFragmentManager(), "dialog");
                }
                else{
                    Toast.makeText(mContext, "Fill the fields!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void runSendOrder() throws Exception {

        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_NEW_SKYMEDIA);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");
        System.out.println(mPrefManager.getAuthToken());

        RequestBody formBody = new FormBody.Builder()
                //TODO: photo path send needed
                .add("contact",mContactNumber.getText().toString())
                .add("last_name", mLastName.getText().toString())
                .add("first_name",mFirstName.getText().toString())
                .add("register", mRegNumber.getText().toString())
                .add("address",mAddressDetail.getText().toString())
                .add("city",mAimagCity.getText().toString())
                .add("district",mSumDistrict.getText().toString())
                .add("horoo", mKhorooBag.getText().toString())
                .add("apartment",mBuildingStreet.getText().toString())
                .add("entrance",mDoorFence.getText().toString())
                .add("door_no",mDoorNumber.getText().toString())
                .add("contact",mContactNumber.getText().toString())
                .add("description",mOrderDesc.getText().toString())
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
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                            // Used for debug
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

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
                    String result_msg = jsonObj.getString("result_msg");


                    Log.d(TAG, "result_code: " + result_code);
                    Log.d(TAG, "result_msg: " + result_msg);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });


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
        });
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(mContext);

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

        if(isFirst) {

            mFrontImage.setImageBitmap(thumbnail);
        }
        else {

            mBackImage.setImageBitmap(thumbnail);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(isFirst) {

            mFrontImage.setImageBitmap(bm);
        }
        else {
            mBackImage.setImageBitmap(bm);
        }
    }

    private ConfirmDialog.OnDialogConfirmListener dialogConfirmListener = new ConfirmDialog.OnDialogConfirmListener() {

        @Override
        public void onPositiveButton() {
            try {
                mProgressDialog.show();
                runSendOrder();
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
