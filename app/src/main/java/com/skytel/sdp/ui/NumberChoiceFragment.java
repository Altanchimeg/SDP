package com.skytel.sdp.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.*;
import android.widget.TextView;

import com.skytel.sdp.R;

/**
 * Created by bayarkhuu on 4/14/2016.
 */
public class NumberChoiceFragment extends Fragment {


    public NumberChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_number_choice, container, false);
        return rootView;
    }


}
