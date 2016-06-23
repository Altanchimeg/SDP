package com.skytel.sdp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


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

    private ImageView mFrontImage;
    private ImageView mBackImage;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuserinfo);

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

}
