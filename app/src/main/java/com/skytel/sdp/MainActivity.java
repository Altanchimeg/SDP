package com.skytel.sdp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.skytel.sdp.ui.skydealer.ChargeCardFragment;
import com.skytel.sdp.utils.BalanceUpdateListener;
import com.skytel.sdp.utils.ConfirmDialog;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.PrefManager;

public class MainActivity extends AppCompatActivity implements BalanceUpdateListener {
    String TAG = MainActivity.class.getName();

    public static int currentMenu = Constants.MENU_NEWNUMBER;
    private ListView leftMenuListView;
    private LeftMenuListAdapter leftMenuListAdapter;
    private FragmentTransaction transaction;
    private Context context;
    private DataManager dataManager;
    private PrefManager prefManager;
    private TextView mDealerName;
    private TextView mDealerBalance;
    private TextView mDealerZone;


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

        mDealerName = (TextView) findViewById(R.id.dealer_name);
        mDealerName.setText(prefManager.getDealerName());
        mDealerBalance = (TextView) findViewById(R.id.dealer_balance);
        mDealerBalance.setText(prefManager.getDealerBalance());
        mDealerZone = (TextView) findViewById(R.id.dealer_zone);
        mDealerZone.setText(prefManager.getDealerZone());

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
                        //logoutDialog();
                        ConfirmDialog confirmDialog = new ConfirmDialog();
                        Bundle args = new Bundle();
                        args.putInt("message", R.string.confirm);
                        args.putInt("title", R.string.confirm);

                        confirmDialog.setArguments(args);
                        confirmDialog.registerCallback(dialogConfirmListener);
                        confirmDialog.show(getFragmentManager(), "dialog");


                        break;
                }
                currentMenu = position;
                leftMenuListAdapter.notifyDataSetChanged();
            }
        });

        new LongOperation().execute();

        ChargeCardFragment.mBalanceUpdateListener = this;

    }

    // Login
    public void loginView(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void changeMenu(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction
                .replace(R.id.main_detail_container, fragment)
                .commit();
    }


    private ConfirmDialog.OnDialogConfirmListener dialogConfirmListener = new ConfirmDialog.OnDialogConfirmListener() {

        @Override
        public void onPositiveButton() {
            //  Toast.makeText(this, "Confirmed", Toast.LENGTH_LONG).show();

            prefManager.setIsLoggedIn(false);
            dataManager.resetCardTypes();

            finish();
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }

        @Override
        public void onNegativeButton() {

        }
    };

    @Override
    public void onBalanceUpdate() {
        Toast.makeText(context, "Balance ", Toast.LENGTH_LONG).show();
        mDealerBalance.setText(prefManager.getDealerBalance());
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
