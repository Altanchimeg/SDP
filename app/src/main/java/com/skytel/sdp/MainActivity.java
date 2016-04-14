package com.skytel.sdp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.skytel.sdp.ui.NumberChoiceFragment;
import com.skytel.sdp.ui.NumberOrderFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

    }

    public void numberChoiceView(View v) {
        NumberChoiceFragment numberChoiceFragment = new NumberChoiceFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_detail_container, numberChoiceFragment)
                .commit();
    }

    public void numberOrderView(View v) {
        NumberOrderFragment numberOrderFragment = new NumberOrderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_detail_container, numberOrderFragment)
                .commit();
    }

}
