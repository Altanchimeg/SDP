package com.skytel.sdp.ui.newnumber;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.skytel.sdp.NumberUserInfoActivity;
import com.skytel.sdp.R;
import com.skytel.sdp.adapter.NumberChoiceAdapter;
import com.skytel.sdp.adapter.PriceTypeInfoListAdapter;
import com.skytel.sdp.database.DataManager;
import com.skytel.sdp.entities.Phonenumber;
import com.skytel.sdp.entities.PriceType;
import com.skytel.sdp.utils.Constants;
import com.skytel.sdp.utils.CustomProgressDialog;
import com.skytel.sdp.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Altanchimeg on 4/14/2016.
 */
public class NumberChoiceFragment extends Fragment {

    String TAG = NumberChoiceFragment.class.getName();

    private OkHttpClient mClient;
    private Context mContext;
    private DataManager mDataManager;
    private CustomProgressDialog mProgressDialog;

    private String mSelected_number = "";
    private String mSelected_price_type_id = "";

    ArrayList<Phonenumber> mNumbersArrayList;
    ArrayList<PriceType> mPriceTypeInfoArrayList;
    ArrayList<String> mPrefixArrayList;
    private Spinner mPrefixSpinner;
    private EditText mSearchNumber;
    private Button mSearchButton;
    private Button mNumberChoiceOrder;
    private GridView mNewNumbersGrid;
    private ListView mPriceTypeInfoListView;
    private TextView mChosenNewNumber;
    private Phonenumber mPhoneNumber;
    private PriceType mPriceType;
    private NumberChoiceAdapter mNumberChoiceAdapter;
    private PriceTypeInfoListAdapter mPriceTypeInfoListAdapter;
    private EditText mRegisterNumber;

    private PrefManager mPrefManager;

    public NumberChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.number_choice, container, false);

        mContext = getActivity();
        mDataManager = new DataManager(mContext);
        mClient = new OkHttpClient();
        mNumbersArrayList = new ArrayList<Phonenumber>();
        mPriceTypeInfoArrayList = new ArrayList<PriceType>();
        mPrefixArrayList = new ArrayList<>();
        mProgressDialog = new CustomProgressDialog(getActivity());
        mPrefManager = new PrefManager(mContext);

        mSearchNumber = (EditText) rootView.findViewById(R.id.et_search_number);
        mNewNumbersGrid = (GridView) rootView.findViewById(R.id.newNumbersList);
        mPriceTypeInfoListView = (ListView) rootView.findViewById(R.id.priceTypeInfoListView);
        mPrefixSpinner = (Spinner) rootView.findViewById(R.id.prefix);
        mChosenNewNumber = (TextView) rootView.findViewById(R.id.chosen_new_number);
        mRegisterNumber = (EditText) rootView.findViewById(R.id.register_number);

       /* for (int i = 0; i < 100; i++) {
            Phonenumber pn = new Phonenumber();
            pn.setId(i + 1);
            pn.setPhoneNumber("91109" + (i + 1));
            pn.setPriceType("1");
            mNumbersArrayList.add(pn);
        }*/

        mNumberChoiceAdapter = new NumberChoiceAdapter(mContext, mNumbersArrayList);
        mNewNumbersGrid.setAdapter(mNumberChoiceAdapter);

        mPriceTypeInfoListAdapter = new PriceTypeInfoListAdapter(mContext, mPriceTypeInfoArrayList);
        mPriceTypeInfoListView.setAdapter(mPriceTypeInfoListAdapter);

        mPriceTypeInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, mPriceTypeInfoArrayList.get(position).getPrice(), Toast.LENGTH_SHORT).show();

            }
        });

        try {
            mProgressDialog.show();
            runGetPrefixFunction();

        } catch (Exception e) {
            e.printStackTrace();
        }


        mPrefixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSearchNumber.setText(mPrefixArrayList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSearchButton = (Button) rootView.findViewById(R.id.numberSearch);
        mSearchButton.setOnClickListener(SearchNewNumberOnClick);

        mNumberChoiceOrder = (Button) rootView.findViewById(R.id.numberChoiceOrder);
        mNumberChoiceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "NumberUserInfo", Toast.LENGTH_SHORT).show();
                try {
                    //TODO: if price = 5000₮ send 0, else if price = 10000₮ send 99
                    //Todo: check validation <choose phone number><choose price type>
                    runReserveNumber(mSearchNumber.getText().toString(),mRegisterNumber.getText().toString(), 0,1 );
                } catch (Exception e) {
                    e.printStackTrace();
                }

/*
                NumberChoiceUserInfoFragment fragment2 = new NumberChoiceUserInfoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment1, fragment2);
                fragmentTransaction.commit();
*/
            }
        });

        return rootView;
    }


    public void runGetPrefixFunction() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_SKYTEL_MN_URL);
        url.append(Constants.FUNCTION_GET_PREFIX);
        url.append("?service=" + "prepaid");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");

        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mProgressDialog.dismiss();

                System.out.println("onResponse");

                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String resp = response.body().string();
                System.out.println("resp " + resp);

                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    String success = jsonObj.getString("success");

                    Log.d(TAG, "success " + success);

                    JSONArray jArray = jsonObj.getJSONArray("prefix");

                    Log.d(TAG, "*****JARRAY*****" + jArray.length());
                    mPrefixArrayList.clear();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jsonData = jArray.getJSONObject(i);

                        String pref = jsonData.getString("pref");

                        Log.d(TAG, "INDEX:       " + i);

                        Log.d(TAG, "pref: " + pref);


                        mPrefixArrayList.add(pref);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, mPrefixArrayList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            mPrefixSpinner.setAdapter(adapter);
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

    public void runGetSearchNumberList() throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_NUMBER_SKYTEL_URL);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");

        RequestBody formBody = new FormBody.Builder()
                .add("task", "2")
                .add("mask", mSearchNumber.getText().toString())
                .add("mode", "1")
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mProgressDialog.dismiss();

                System.out.println("onResponse");

                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String resp = response.body().string();
                System.out.println("resp " + resp);

                try {
                    JSONObject jsonObj = new JSONObject(resp);

                    JSONArray jArray = jsonObj.getJSONArray("data");

                    Log.d(TAG, "*****JARRAY*****" + jArray.length());
                    mNumbersArrayList.clear();


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jsonData = jArray.getJSONObject(i);

                        String phonenumber = jsonData.getString("PhoneNumber");
                        String price_type = jsonData.getString("priceType");
                        mPhoneNumber = new Phonenumber();
                        mPhoneNumber.setPhoneNumber(phonenumber);
                        mPhoneNumber.setPriceType(price_type);
                        mPhoneNumber.setId(i);

                        Log.d(TAG, "Phonenumber:       " + phonenumber);
                        Log.d(TAG, "price_type: " + price_type);

                        mNumbersArrayList.add(mPhoneNumber);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNewNumbersGrid.setAdapter(mNumberChoiceAdapter);

                            mNewNumbersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "Selected: " + mNumbersArrayList.get(position));
                                    mSelected_number = mNumbersArrayList.get(position).getPhoneNumber();
                                    mSelected_price_type_id = mNumbersArrayList.get(position).getPriceType();
                                    mSearchNumber.setText(mSelected_number);
                                    mChosenNewNumber.setText(mSelected_number);
                                    try {
                                        mProgressDialog.show();
                                        runGetPriceFunction(mSelected_price_type_id);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
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

    public void runGetPriceFunction(String priceTypeId) throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_SKYTEL_MN_URL);
        url.append(Constants.FUNCTION_GET_PRICE);
        url.append("?priceType=" + priceTypeId);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");

        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mProgressDialog.dismiss();

                System.out.println("onResponse");

                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String resp = response.body().string();
                System.out.println("resp " + resp);

                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    boolean success = jsonObj.getBoolean("success");

                    Log.d(TAG, "success " + success);
                    if (success == true) {
                        JSONArray jArray = jsonObj.getJSONArray("numberPrice");

                        Log.d(TAG, "*****JARRAY*****" + jArray.length());
                        mPriceTypeInfoArrayList.clear();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonData = jArray.getJSONObject(i);

                            String name_mn = jsonData.getString("name_mn");
                            String price = jsonData.getString("price");
                            String unit = jsonData.getString("unit");
                            String days = jsonData.getString("days");

                            mPriceType = new PriceType();
                            mPriceType.setPrice(price);
                            mPriceType.setUnit(unit);
                            mPriceType.setDays(days);
                            mPriceType.setId(i);

                            mPriceTypeInfoArrayList.add(mPriceType);

                            Log.d(TAG, "name_mn: " + name_mn);
                            Log.d(TAG, "price: " + price);
                            Log.d(TAG, "unit: " + unit);
                            Log.d(TAG, "days: " + days);

                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PriceTypeInfoListAdapter mPriceTypeInfoListAdapter = new PriceTypeInfoListAdapter(mContext, mPriceTypeInfoArrayList);
                                mPriceTypeInfoListView.setAdapter(mPriceTypeInfoListAdapter);
                                mPriceTypeInfoListAdapter.notifyDataSetChanged();
                            }
                        });
                    }


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

    View.OnClickListener SearchNewNumberOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // mNumbersArrayList.clear();
            /*for (int i = 0; i < 1; i++) {
                Phonenumber pn = new Phonenumber();
                pn.setId(i + 1);
                pn.setPhoneNumber("90109" + (i + 1));
                pn.setPriceType("1");
                mNumbersArrayList.add(pn);
            }*/
            // mNumberChoiceAdapter.notifyDataSetChanged();

            // Comment for Test

            try {
                mProgressDialog.show();
                runGetSearchNumberList();
                mNumberChoiceAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    /**
     *
     * Pre reservation function
     * At first, User choose number and send her/his registration number
     *
     * @param phone         Chosen phone number to buy
     * @param register      User registration number
     * @param price_type    Price type of user /if it 0, 5000₮, else if user want to buy 10000₮, it will be 99/
     * @param service_type  Prepaid is 1, Postpaid is 0
     * @throws Exception
     */

    public void runReserveNumber(final String phone, final String register, Integer price_type, Integer service_type) throws Exception {
        final StringBuilder url = new StringBuilder();
        url.append(Constants.SERVER_URL);
        url.append(Constants.FUNCTION_RESERVE_NUMBER);
        url.append("?phone=" + phone);
        url.append("&register=" + register);
        url.append("&price_type=" + price_type);
        url.append("&service_type=" + service_type);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "URL: " + url.toString(), Toast.LENGTH_LONG).show();
            }
        });

        System.out.print(url + "\n");

        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader(Constants.PREF_AUTH_TOKEN, mPrefManager.getAuthToken())
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                System.out.println("onFailure");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Error on Failure!", Toast.LENGTH_LONG).show();
                        // Used for debug
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mProgressDialog.dismiss();

                System.out.println("onResponse");

                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

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
                    final String reservation_id = jsonObj.getString("reservation_id");
                    Log.d(TAG, "result_code " + result_code);
                    Log.d(TAG, "result_msg " + result_msg);
                    Log.d(TAG, "reservation_id " + reservation_id);

                    if (result_code == Constants.RESULT_CODE_SUCCESS) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mContext, NumberUserInfoActivity.class);
                                intent.putExtra("reservation_id",reservation_id);
                                intent.putExtra("phone_number",phone);
                                intent.putExtra("register_number",register);
                                startActivity(intent);
                            }
                        });

                    }


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

}
