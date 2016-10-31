package com.skytel.sdp.ui.plan;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.skytel.sdp.R;
import com.skytel.sdp.adapter.NewNumberReportAdapter;
import com.skytel.sdp.entities.Plan;
import com.skytel.sdp.ui.newnumber.SortableNewNumberReportTableView;

import java.util.ArrayList;
import java.util.List;

public class PlanFragment extends Fragment {

    private RelativeLayout mPlanNewUserTableViewContainer;
    private List<Plan> mNewNumberReportArrayList;
    private PieChart pieChart;

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

        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);
        setData(4, 100);

        return rootView;
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            values.add(new Entry((float) ((Math.random() * range) + range / 5), 10));
            xVals.add(i+"");
        }

        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(xVals,dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
