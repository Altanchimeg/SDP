package com.skytel.sdp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.skytel.sdp.adapter.LeftMenuListAdapter;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.ui.TabRegistrationFragment;
import com.skytel.sdp.ui.TabServiceFragment;
import com.skytel.sdp.ui.TabSettingsFragment;
import com.skytel.sdp.ui.TabSkyDealerFragment;
import com.skytel.sdp.ui.feedback.FeedbackFragment;
import com.skytel.sdp.ui.information.InformationFragment;
import com.skytel.sdp.ui.TabNewNumberFragment;
import com.skytel.sdp.ui.plan.PlanFragment;
import com.skytel.sdp.utils.ConfirmDialog;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.PrefManager;

public class MainActivity extends AppCompatActivity implements ConfirmDialog.OnDialogConfirmListener {
    String TAG = MainActivity.class.getName();

    public static int currentMenu = Constants.MENU_NEWNUMBER;
    private ListView leftMenuListView;
    private LeftMenuListAdapter leftMenuListAdapter;
    private FragmentTransaction transaction;
    private Context context;
    private DataManager dataManager;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        dataManager = new DataManager(this);
        prefManager = new PrefManager(this);

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
                        //                   logoutDialog();
                        DialogFragment newFragment = ConfirmDialog.newInstance(
                                R.string.confirm,R.string.app_name);
                        newFragment.show(getFragmentManager(), "dialog");

                        break;
                }
                currentMenu = position;
                leftMenuListAdapter.notifyDataSetChanged();
            }
        });

        new LongOperation().execute();

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

/*
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
                // User Logout here
            }
        });
    }
*/

    @Override
    public void onDialogConfirm() {
        Toast.makeText(this, "Confirmed", Toast.LENGTH_LONG).show();

        prefManager.setIsLoggedIn(false);
        dataManager.resetCardTypes();

        finish();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private class LongOperation extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if (dataManager.resetCardTypes()) {
                    dataManager.setCardTypes();
                }

                return true;
            } catch (
                    Exception e
                    )

            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //  progressDialog.dismiss();
            if (result) {
                Log.d(TAG, "AsyncTask Finished");

            } else {
                Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Load Asyns started");
            //   progressDialog = new ProgressDialog(context);
            //   progressDialog.setCancelable(true);
            //  progressDialog.show(context, getResources().getString(R.string.please_wait), getResources().getString(R.string.checking));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
