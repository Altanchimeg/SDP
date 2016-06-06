package com.skytel.sdp.ui.information;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skytel.sdp.R;


public class InformationFragment extends Fragment {


    public InformationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.information, container, false);

        Toast.makeText(getActivity(), getText(R.string.information)+" хэсэг нь энэхүү хувилбарт ажиллахгүй байгаа. ", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Та хувилбарт багтсан хэсгийг сонгон тестэлнэ үү. Баярлалаа", Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
