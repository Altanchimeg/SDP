package com.skytel.sdp.ui.newnumber;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skytel.sdp.R;

/**
 * Created by Altanchimeg on 4/18/2016.
 */
public class NumberOrderReportFragment extends Fragment {


    public NumberOrderReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.number_order_report, container, false);
        return  rootView;
    }

}
