package com.skytel.sdp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.skytel.sdp.utils.Constants;


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

        mRegistrationNumberEt.setText(mRegisterNumber);
        mChosenNumber.setText(mPhoneNumber);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
