package com.skytel.sdp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skytel.sdp.R;
import com.skytel.sdp.entities.SalesReport;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class SalesReportPostPaidPaymentAdapter extends TableDataAdapter<SalesReport> {
    private Context mContext;

    public SalesReportPostPaidPaymentAdapter(Context context, List<SalesReport> data) {
        super(context, data);
        mContext = context;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        SalesReport salesReport = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderPhone(salesReport);
                break;
            case 1:
                renderedView = renderValue(salesReport);
                break;
            case 2:
                renderedView = renderSuccess(salesReport);
                break;
            case 3:
                renderedView = renderDate(salesReport);
                break;
        }

        return renderedView;
    }

    private View renderPhone(final SalesReport salesReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(salesReport.getPhone() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderValue(final SalesReport salesReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(salesReport.getValue() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderSuccess(final SalesReport salesReport) {
        final TextView textView = new TextView(getContext());
//        textView.setText(salesReport.getDate().toString() + "");
        if (salesReport.isSuccess()) {
            textView.setText(mContext.getResources().getString(R.string.successful));
        } else {
            textView.setText(mContext.getResources().getString(R.string.unsuccessful));
        }
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }

    private View renderDate(final SalesReport salesReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(salesReport.getDate().toString() + "");
        //textView.setText("2016/06/01 15:23");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }



}