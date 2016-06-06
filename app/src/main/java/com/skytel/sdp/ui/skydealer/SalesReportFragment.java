package com.skytel.sdp.ui.skydealer;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.adapter.SalesReportAdapter;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.entities.SalesReport;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.skytel.sdp.utils.PrefManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SalesReportFragment extends Fragment implements Constants {

    String TAG = SalesReportFragment.class.getName();

    private OkHttpClient client;
    private Context mContext;
    private DataManager mDataManager;
    private PrefManager prefManager;
    private GridView mSalesReportGrid;
    private SortableTableView mSalesReportTableView;
    private List<SalesReport> salesReportArrayList;
    private CustomProgressDialog progressDialog;
    private Button mSearch;
    private Button mFilterByAll;
    private Button mFilterBySuccess;
    private Button mFilterByFailed;
    private EditText mFilterByPhoneNumber;
    private EditText mFilterByEndDate;
    private EditText mFilterByStartDate;
    private Spinner mFilterByUnitPackage;

    private int selectedFilterButton = FILTER_ALL;
//    private int selectedReportType =

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.skydealer_report, container, false);

        mContext = getActivity();
        mDataManager = new DataManager(mContext);
        client = new OkHttpClient();
        prefManager = new PrefManager(mContext);
        salesReportArrayList = new ArrayList<>();
        progressDialog = new CustomProgressDialog(getActivity());
        mSalesReportTableView = (SortableTableView) rootView.findViewById(R.id.salesReportTableView);

        mSearch = (Button) rootView.findViewById(R.id.search);
        mSearch.setOnClickListener(searchOnClick);

        mFilterByAll = (Button) rootView.findViewById(R.id.filterByAll);
        mFilterByAll.setOnClickListener(filterByAllOnClick);

        mFilterBySuccess = (Button) rootView.findViewById(R.id.filterBySuccess);
        mFilterBySuccess.setOnClickListener(filterBySuccessOnClick);

        mFilterByFailed = (Button) rootView.findViewById(R.id.filterByFailed);
        mFilterByFailed.setOnClickListener(filterByFailedOnClick);

        mFilterByPhoneNumber = (EditText) rootView.findViewById(R.id.filterByOrderedNumber);
        mFilterByEndDate = (EditText) rootView.findViewById(R.id.filterByEndDate);
        mFilterByStartDate = (EditText) rootView.findViewById(R.id.filterByStartDate);
        mFilterByUnitPackage = (Spinner) rootView.findViewById(R.id.filterByUnitPackage);

      /*  for (int i = 0; i < 100; i++) {
            Date date = new Date();
            boolean success = true;
            String type = "1";
            String value = "100";
            String phone = "91109789";

            Log.d(TAG, "INDEX:       " + i);

            Log.d(TAG, "date: " + date);
            Log.d(TAG, "success: " + success);
            Log.d(TAG, "type: " + type);
            Log.d(TAG, "value: " + value);
            Log.d(TAG, "phone: " + phone);

            SalesReport salesReport = new SalesReport();
            salesReport.setId(i);
            salesReport.setPhone(phone);
            salesReport.setSuccess(success);
            salesReport.setType(type);
            salesReport.setValue(value);
            salesReport.setDate(date);

            salesReportArrayList.add(i, salesReport);
        }

        mSalesReportTableView.setDataAdapter(new SalesReportAdapter(getActivity(), salesReportArrayList));
        mSalesReportTableView.addDataClickListener(new SalesReportClickListener());
        mSalesReportTableView.addHeaderClickListener(new SalesReportHeaderClickListener());*/

/*
        try {
            String report_type = REPORT_DEALER_POSTPAIDPAYMENT_TYPE;
            runChargeCardReportFunction(report_type);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return rootView;
    }

    View.OnClickListener searchOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                // mFilterByPhoneNumber.getText().toString();
                progressDialog.show();
                runChargeCardReportFunction(REPORT_DEALER_CHARGECARD_TYPE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener filterByAllOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedFilterButton = FILTER_ALL;
            invalidateFilterButtons();
        }
    };

    View.OnClickListener filterBySuccessOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedFilterButton = FILTER_SUCCESS;
            invalidateFilterButtons();
        }
    };

    View.OnClickListener filterByFailedOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedFilterButton = FILTER_FAILED;
            invalidateFilterButtons();
        }
    };

    private void invalidateFilterButtons() {
        switch (selectedFilterButton) {
            case FILTER_ALL:
                mFilterByAll.setBackground(getResources().getDrawable(R.drawable.btn_yellow_selected));
                mFilterByAll.setTextColor(Color.WHITE);
                mFilterBySuccess.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterBySuccess.setTextColor(getResources().getColor(R.color.colorSkytelYellow));
                mFilterByFailed.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterByFailed.setTextColor(getResources().getColor(R.color.colorSkytelYellow));
                break;
            case FILTER_SUCCESS:
                mFilterByAll.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterByAll.setTextColor(getResources().getColor(R.color.colorSkytelYellow));

                mFilterBySuccess.setBackground(getResources().getDrawable(R.drawable.btn_yellow_selected));
                mFilterBySuccess.setTextColor(Color.WHITE);

                mFilterByFailed.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterByFailed.setTextColor(getResources().getColor(R.color.colorSkytelYellow));
                break;
            case FILTER_FAILED:
                mFilterByAll.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterByAll.setTextColor(getResources().getColor(R.color.colorSkytelYellow));

                mFilterBySuccess.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterBySuccess.setTextColor(getResources().getColor(R.color.colorSkytelYellow));

                mFilterByFailed.setBackground(getResources().getDrawable(R.drawable.btn_yellow_selected));
                mFilterByFailed.setTextColor(Color.WHITE);

                break;
        }
    }


    public void runChargeCardReportFunction(String report_type) throws Exception {
        progressDialog.show();
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_DEALER_REPORT);
        url.append("?trans_type=" + report_type);
        url.append("&len=" + 40);
        url.append("&from=" + 0);
        url.append("&is_success=" + true);
        // &startDate = null || 2016/05/01
        // &endDate =  null || 2016/06/01
        // &phoneNumber = null || 91109789
        // &unitPackage = 0...123

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");
        System.out.println(prefManager.getAuthToken());

        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader(PREF_AUTH_TOKEN, prefManager.getAuthToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
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
                    String dealer_id = jsonObj.getString("dealer_id");

                    Log.d(TAG, "result_code: " + result_code);
                    Log.d(TAG, "result_msg: " + result_msg);
                    Log.d(TAG, "dealer_id: " + dealer_id);

                    JSONArray jArray = jsonObj.getJSONArray("transactions");

                    Log.d(TAG, "*****JARRAY*****" + jArray.length());
                    for (int i = 0; i <jArray.length(); i++) {
                        JSONObject jsonData = jArray.getJSONObject(i);

                        String date = jsonData.getString("date");
                        boolean success = jsonData.getBoolean("success");
                        String type = jsonData.getString("type");
                        String value = jsonData.getString("value");
                        String phone = jsonData.getString("phone");

                        Log.d(TAG, "INDEX:       " + i);

                        Log.d(TAG, "date: " + date);
                        Log.d(TAG, "success: " + success);
                        Log.d(TAG, "type: " + type);
                        Log.d(TAG, "value: " + value);
                        Log.d(TAG, "phone: " + phone);

                        SalesReport salesReport = new SalesReport();
                       // DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        salesReport.setId(i);
                        salesReport.setPhone(phone);
                        salesReport.setValue(value);
                        salesReport.setSuccess(success);
                        salesReport.setType(type);
                        salesReport.setDate(date);

                        salesReportArrayList.add(i, salesReport);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSalesReportTableView.setDataAdapter(new SalesReportAdapter(getActivity(), salesReportArrayList));
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

    private class SalesReportHeaderClickListener implements TableHeaderClickListener {
        @Override
        public void onHeaderClicked(int columnIndex) {
            String notifyText = "clicked column " + (columnIndex + 1);
            Toast.makeText(getContext(), notifyText, Toast.LENGTH_SHORT).show();
        }
    }

    private class SalesReportClickListener implements TableDataClickListener<SalesReport> {
        @Override
        public void onDataClicked(int rowIndex, SalesReport clickedReport) {
            String clickedCarString = clickedReport.getPhone() + "-" + clickedReport.getValue();
            Toast.makeText(getContext(), clickedCarString, Toast.LENGTH_SHORT).show();
        }
    }

}
