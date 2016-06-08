package com.skytel.sdp.ui.skydealer;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.adapter.NothingSelectedSpinnerAdapter;
import com.skytel.sdp.adapter.SalesReportAdapter;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.entities.SalesReport;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.skytel.sdp.utils.PrefManager;
import com.skytel.sdp.utils.ValidationChecker;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    //private Button mFilterByAll;
    private Button mFilterBySuccess;
    private Button mFilterByFailed;
    private EditText mFilterByPhoneNumber;
    private EditText mFilterByEndDate;
    private EditText mFilterByStartDate;
    private Spinner mFilterByUnitPackage;


    private Button mFilterButtonByEndDate;
    private Button mFilterButtonByStartDate;

    private Spinner mReportType;

    private int selectedFilterButton = FILTER_SUCCESS;
    private  boolean isSuccessFilter = true;

    private int selectedItemId = -1;
    private String [] reportType = null;

    private int mYear;
    private int mMonth;
    private int mDay;

    private final Calendar myCalendar=Calendar.getInstance();;


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

        /*mFilterByAll = (Button) rootView.findViewById(R.id.filterByAll);
        mFilterByAll.setOnClickListener(filterByAllOnClick);*/

        mFilterBySuccess = (Button) rootView.findViewById(R.id.filterBySuccess);
        mFilterBySuccess.setOnClickListener(filterBySuccessOnClick);

        mFilterByFailed = (Button) rootView.findViewById(R.id.filterByFailed);
        mFilterByFailed.setOnClickListener(filterByFailedOnClick);

        mFilterByPhoneNumber = (EditText) rootView.findViewById(R.id.filterByOrderedNumber);
        mFilterByEndDate = (EditText) rootView.findViewById(R.id.filterByEndDate);
        mFilterByStartDate = (EditText) rootView.findViewById(R.id.filterByStartDate);
        mFilterByUnitPackage = (Spinner) rootView.findViewById(R.id.filterByUnitPackage);
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(getActivity(), R.array.skydealer_charge_card_types, android.R.layout.simple_spinner_item);
        mFilterByUnitPackage.setAdapter(new NothingSelectedSpinnerAdapter(adapterFilter,
                R.layout.spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                getActivity()));

        mReportType = (Spinner) rootView.findViewById(R.id.choose_skydealer_report_type);
        ArrayAdapter<CharSequence> adapterReport = ArrayAdapter.createFromResource(getActivity(), R.array.skydealer_report_type, android.R.layout.simple_spinner_item);
        mReportType.setAdapter(new NothingSelectedSpinnerAdapter(adapterReport,
                        R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        mReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                    selectedItemId = (int) mReportType.getSelectedItemId();
                    if (ValidationChecker.isSpinnerSelected(selectedItemId) ) {
                progressDialog.show();

                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                runChargeCardReportFunction(reportType[selectedItemId],100,0,true,"","1900-01-01", currentDateandTime);
                Log.d(TAG, "Report Type: "+reportType[selectedItemId]);
                    } else {
                       // Toast.makeText(getActivity(),getText(R.string.choose_report_type) , Toast.LENGTH_SHORT).show();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        reportType   = getResources().getStringArray(R.array.skydealer_report_type_code);

        mFilterButtonByStartDate = (Button) rootView.findViewById(R.id.btn_start_date);
        mFilterButtonByStartDate.setOnClickListener(filterByStartDateOnClick);
        mFilterButtonByEndDate = (Button) rootView.findViewById(R.id.btn_end_date);
        mFilterButtonByEndDate.setOnClickListener(filterByEndDateOnClick);


        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);




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

                progressDialog.show();
                selectedItemId = (int) mReportType.getSelectedItemId();
                String phone_number =  mFilterByPhoneNumber.getText().toString();
                Boolean isSuccess = isSuccessFilter;

                String start_date = mFilterByStartDate.getText().toString();
                String end_date = mFilterByEndDate.getText().toString();

                runChargeCardReportFunction(reportType[selectedItemId],100,0,isSuccess,phone_number,start_date, end_date);
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
    View.OnClickListener filterByStartDateOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String myFormat = "yyyy-MM-dd"; // In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                            mFilterByStartDate.setText(sdf.format(myCalendar.getTime()));

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
    };
    View.OnClickListener filterByEndDateOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String myFormat = "yyyy-MM-dd"; // In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                            mFilterByEndDate.setText(sdf.format(myCalendar.getTime()));

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
    };



    private void invalidateFilterButtons() {
        switch (selectedFilterButton) {
           /* case FILTER_ALL:
                mFilterByAll.setBackground(getResources().getDrawable(R.drawable.btn_yellow_selected));
                mFilterByAll.setTextColor(Color.WHITE);
                mFilterBySuccess.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterBySuccess.setTextColor(getResources().getColor(R.color.colorSkytelYellow));
                mFilterByFailed.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterByFailed.setTextColor(getResources().getColor(R.color.colorSkytelYellow));
                isSuccessFilter = true;
                break;*/
            case FILTER_SUCCESS:
                //mFilterByAll.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
               // mFilterByAll.setTextColor(getResources().getColor(R.color.colorSkytelYellow));

                mFilterBySuccess.setBackground(getResources().getDrawable(R.drawable.btn_yellow_selected));
                mFilterBySuccess.setTextColor(Color.WHITE);

                mFilterByFailed.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterByFailed.setTextColor(getResources().getColor(R.color.colorSkytelYellow));
                isSuccessFilter = true;
                break;
            case FILTER_FAILED:
               // mFilterByAll.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                //mFilterByAll.setTextColor(getResources().getColor(R.color.colorSkytelYellow));

                mFilterBySuccess.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                mFilterBySuccess.setTextColor(getResources().getColor(R.color.colorSkytelYellow));

                mFilterByFailed.setBackground(getResources().getDrawable(R.drawable.btn_yellow_selected));
                mFilterByFailed.setTextColor(Color.WHITE);
                isSuccessFilter = false;
                break;
        }
    }


    public void runChargeCardReportFunction(String report_type,int length, int from,boolean isSuccess, String phone, String start_date, String end_date) throws Exception {
        progressDialog.show();
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_DEALER_REPORT);
        url.append("?trans_type=" + report_type);
        url.append("&len=" + length);
        url.append("&from=" + from);
        url.append("&is_success=" + isSuccess);
        url.append("&phone=" + phone);
        url.append("&start_date=" + start_date);
        url.append("&end_date=" + end_date);


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


                    Log.d(TAG, "result_code: " + result_code);
                    Log.d(TAG, "result_msg: " + result_msg);

                    JSONArray jArray = jsonObj.getJSONArray("transactions");

                    Log.d(TAG, "*****JARRAY*****" + jArray.length());
                    salesReportArrayList.clear();
                    for (int i = 0; i <jArray.length(); i++) {
                        JSONObject jsonData = jArray.getJSONObject(i);

                        String date = jsonData.getString("date");
                        boolean success = jsonData.getBoolean("success");
                        String card_name = jsonData.getString("card_name");
                        String value = jsonData.getString("value");
                        String phone = jsonData.getString("phone");

                        Log.d(TAG, "INDEX:       " + i);

                        Log.d(TAG, "date: " + date);
                        Log.d(TAG, "success: " + success);
                        Log.d(TAG, "card_name: " + card_name);
                        Log.d(TAG, "value: " + value);
                        Log.d(TAG, "phone: " + phone);

                        SalesReport salesReport = new SalesReport();
                       // DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        salesReport.setId(i);
                        salesReport.setPhone(phone);
                        salesReport.setValue(value);
                        salesReport.setSuccess(success);
                        salesReport.setCardName(card_name);
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
