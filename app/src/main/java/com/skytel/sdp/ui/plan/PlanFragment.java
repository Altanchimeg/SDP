package com.skytel.sdp.ui.plan;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skytel.sdp.R;
import com.skytel.sdp.adapter.NewNumberReportAdapter;
import com.skytel.sdp.entities.Plan;
import com.skytel.sdp.ui.newnumber.SortableNewNumberReportTableView;

import java.util.List;


public class PlanFragment extends Fragment {

    private RelativeLayout mPlanNewUserTableViewContainer;
    private List<Plan> mNewNumberReportArrayList;

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

        mPlanNewUserTableViewContainer = (RelativeLayout) rootView.findViewById(R.id.planNewUserTableViewContainer);

        SortableNewNumberReportTableView sortableNewNumberReportTableView = new SortableNewNumberReportTableView(getActivity());
        mPlanNewUserTableViewContainer.removeAllViews();
        mPlanNewUserTableViewContainer.addView(sortableNewNumberReportTableView);
        sortableNewNumberReportTableView.setColumnCount(getResources().getInteger(R.integer.new_number_report_column));
        sortableNewNumberReportTableView.setHeaderBackgroundColor(Color.TRANSPARENT);
       // sortableNewNumberReportTableView.setDataAdapter(new NewNumberReportAdapter(getActivity(), mNewNumberReportArrayList));

        return rootView;
    }

}
