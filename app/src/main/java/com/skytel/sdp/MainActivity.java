package com.skytel.sdp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.skytel.sdp.adapter.LeftMenuListAdapter;
import com.skytel.sdp.ui.NumberChoiceFragment;
import com.skytel.sdp.ui.NumberOrderFragment;
import com.skytel.sdp.utils.Constants;

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
        leftMenuListAdapter = new LeftMenuListAdapter(this);
        leftMenuListView.setAdapter(leftMenuListAdapter);
        leftMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case Constants.MENU_NEWNUMBER:
                        NumberChoiceFragment numberChoiceFragment = new NumberChoiceFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, numberChoiceFragment)
                                .commit();
                        break;


                }
            }
        });

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
