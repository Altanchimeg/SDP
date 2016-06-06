package com.skytel.sdp.ui.plan;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skytel.sdp.R;


public class PlanFragment extends Fragment {


    public PlanFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.plan, container, false);

        Toast.makeText(getActivity(), getText(R.string.plan)+" хэсэг нь энэхүү хувилбарт ажиллахгүй байгаа. ", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Та хувилбарт багтсан хэсгийг сонгон тестэлнэ үү. Баярлалаа", Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
