package com.skytel.sdp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.skytel.sdp.adapter.LeftMenuListAdapter;
import com.skytel.sdp.ui.NumberChoiceFragment;
import com.skytel.sdp.ui.NumberOrderFragment;

public class MainActivity extends AppCompatActivity {
    ListView leftMenuListView;
    LeftMenuListAdapter leftMenuListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        leftMenuListView = (ListView) findViewById(R.id.leftMenuListView);
        leftMenuListAdapter = new LeftMenuListAdapter(this, getResources().getStringArray(R.array.leftmenu_array));
        leftMenuListView.setAdapter(leftMenuListAdapter);

    }

    // New number choice
    public void numberChoiceView(View v) {
        NumberChoiceFragment numberChoiceFragment = new NumberChoiceFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_detail_container, numberChoiceFragment)
                .commit();
    }

    // New number order
    public void numberOrderView(View v) {
        NumberOrderFragment numberOrderFragment = new NumberOrderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_detail_container, numberOrderFragment)
                .commit();
    }

}
