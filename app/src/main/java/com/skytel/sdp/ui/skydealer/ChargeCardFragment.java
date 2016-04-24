package com.skytel.sdp.ui.skydealer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skytel.sdp.R;

/**
 * @author Altanchimeg
 *
 */
public class ChargeCardFragment extends Fragment {


    public ChargeCardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.main_charge_card, container, false);
        return rootView;
    }

}
