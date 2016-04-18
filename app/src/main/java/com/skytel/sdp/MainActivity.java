package com.skytel.sdp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.skytel.sdp.adapter.LeftMenuListAdapter;
import com.skytel.sdp.ui.ChargeCardFragment;
import com.skytel.sdp.ui.DealerRegistrationFragment;
import com.skytel.sdp.ui.FeedbackFragment;
import com.skytel.sdp.ui.HandsetChangeFragment;
import com.skytel.sdp.ui.InformationFragment;
import com.skytel.sdp.ui.NumberChoiceFragment;
import com.skytel.sdp.ui.NumberOrderFragment;
import com.skytel.sdp.ui.PlanFragment;
import com.skytel.sdp.ui.SettingsFragment;
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
                    case Constants.MENU_SKYDEALER:
                        ChargeCardFragment chargeCardFragment = new ChargeCardFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, chargeCardFragment)
                                .commit();
                        break;
                    case Constants.MENU_SERVICE:
                        HandsetChangeFragment handsetChangeFragment = new HandsetChangeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, handsetChangeFragment)
                                .commit();
                        break;
                    case Constants.MENU_REGISTRATION:
                        DealerRegistrationFragment dealerRegistrationFragment = new DealerRegistrationFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, dealerRegistrationFragment)
                                .commit();
                        break;
                    case Constants.MENU_INFORMATION:
                        InformationFragment informationFragment = new InformationFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, informationFragment)
                                .commit();
                        break;
                    case Constants.MENU_PLAN:
                        PlanFragment planFragment = new PlanFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, planFragment)
                                .commit();
                        break;
                    case Constants.MENU_FEEDBACK:
                        FeedbackFragment feedbackFragment = new FeedbackFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, feedbackFragment)
                                .commit();
                        break;
                    case Constants.MENU_SETTINGS:
                        SettingsFragment settingsFragment = new SettingsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_detail_container, settingsFragment)
                                .commit();
                        break;
                    case Constants.MENU_LOGOUT:

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
