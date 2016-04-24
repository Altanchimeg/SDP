package com.skytel.sdp.ui.information;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skytel.sdp.R;


public class InformationFragment extends Fragment {


    public InformationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_information, container, false);
        return rootView;
    }

}
