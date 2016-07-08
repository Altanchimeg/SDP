package com.skytel.sdp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skytel.sdp.entities.RegistrationReport;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by Altanchimeg on 7/6/2016.
 */

public class RegReportDealerAdapter  extends TableDataAdapter<RegistrationReport> {
    private Context mContext;

    public RegReportDealerAdapter(Context context, List<RegistrationReport> data) {
        super(context, data);
        mContext = context;
    }
    // put columns to charge card table
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        RegistrationReport registrationReport = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderPhone(registrationReport);
                break;
            case 1:
                renderedView = renderDealerType(registrationReport);
                break;
            case 2:
                renderedView = renderDiscount(registrationReport);
                break;
            case 3:
                renderedView = renderState(registrationReport);
                break;
            case 4:
                renderedView = renderDate(registrationReport);
                break;
        }

        return renderedView;
    }

    //Set columns layout

    private View renderPhone(final RegistrationReport registrationReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(registrationReport.getPhone() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderDealerType(final RegistrationReport registrationReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(registrationReport.getDealerType() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderDiscount(final RegistrationReport registrationReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(registrationReport.getDiscount() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderState(final RegistrationReport registrationReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(registrationReport.getOrderStatus() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }

    private View renderDate(final RegistrationReport registrationReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(registrationReport.getDate().toString() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }


}