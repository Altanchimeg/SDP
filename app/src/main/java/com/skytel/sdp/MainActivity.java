package com.skytel.sdp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.skytel.sdp.adapter.LeftMenuListAdapter;
import com.skytel.sdp.ui.TabRegistrationFragment;
import com.skytel.sdp.ui.TabServiceFragment;
import com.skytel.sdp.ui.TabSettingsFragment;
import com.skytel.sdp.ui.TabSkyDealerFragment;
import com.skytel.sdp.ui.registration.DealerRegistrationFragment;
import com.skytel.sdp.ui.feedback.FeedbackFragment;
import com.skytel.sdp.ui.information.InformationFragment;
import com.skytel.sdp.ui.TabNewNumberFragment;
import com.skytel.sdp.ui.plan.PlanFragment;
import com.skytel.sdp.ui.settings.ChangePasswordFragment;
import com.skytel.sdp.utils.Constants;

public class MainActivity extends AppCompatActivity {
    public static int currentMenu = Constants.MENU_NEWNUMBER;
    ListView leftMenuListView;
    LeftMenuListAdapter leftMenuListAdapter;
    FragmentTransaction transaction;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (savedInstanceState == null) {
            changeMenu(new TabNewNumberFragment());
        }

        leftMenuListView = (ListView) findViewById(R.id.leftMenuListView);
        leftMenuListAdapter = new LeftMenuListAdapter(this);
        leftMenuListView.setAdapter(leftMenuListAdapter);
        leftMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case Constants.MENU_NEWNUMBER:
                        changeMenu(new TabNewNumberFragment());
                        break;
                    case Constants.MENU_SKYDEALER:
                        changeMenu(new TabSkyDealerFragment());
                        break;
                    case Constants.MENU_SERVICE:
                        changeMenu(new TabServiceFragment());
                        break;
                    case Constants.MENU_REGISTRATION:
                        changeMenu(new TabRegistrationFragment());
                        break;
                    case Constants.MENU_INFORMATION:
                        changeMenu(new InformationFragment());
                        break;
                    case Constants.MENU_PLAN:
                        changeMenu(new PlanFragment());
                        break;
                    case Constants.MENU_FEEDBACK:
                        changeMenu(new FeedbackFragment());
                        break;
                    case Constants.MENU_SETTINGS:
                        changeMenu(new TabSettingsFragment());

                        break;
                    case Constants.MENU_LOGOUT:
                        logoutDialog();
                        break;
                }
                currentMenu = position;
                leftMenuListAdapter.notifyDataSetChanged();
            }
        });

    }

    // Login
    public void loginView(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void changeMenu(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction
                .replace(R.id.main_detail_container, fragment)
                .commit();
    }

    private void logoutDialog() {
        final Dialog dialog = new Dialog(context, R.style.MyAlertDialogStyle);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.setCancelable(true);
        dialog.show();
        final Button dialogNo = (Button) dialog
                .findViewById(R.id.dialogNo);
        dialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button dialogYes = (Button) dialog
                .findViewById(R.id.dialogYes);
        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
