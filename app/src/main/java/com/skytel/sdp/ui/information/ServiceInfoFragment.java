package com.skytel.sdp.ui.information;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skytel.sdp.R;

/**
 * Created by Altanchimeg on 7/18/2016.
 */

public class ServiceInfoFragment extends Fragment {
    public ServiceInfoFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.service_info, container, false);

        return rootView;
    }

}