package com.skytel.sdp.ui.newnumber;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.*;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.skytel.sdp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Altanchimeg on 4/14/2016.
 */
public class NumberChoiceFragment extends Fragment {

    ArrayList<String> mList;

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
        mList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            mList.add("9111 11" + i);
        }
        GridView mNewNumbersList = (GridView) rootView.findViewById(R.id.newNumbersList);
        mNewNumbersList.setAdapter(new NumberChoiceAdapter(getActivity(), mList));

        Button mNumberChoiceOrder = (Button) rootView.findViewById(R.id.numberChoiceOrder);
        mNumberChoiceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Order", Toast.LENGTH_SHORT).show();
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


}
