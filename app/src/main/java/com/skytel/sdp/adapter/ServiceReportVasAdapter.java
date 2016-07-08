package com.skytel.sdp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skytel.sdp.entities.RegistrationReport;
import com.skytel.sdp.entities.ServiceReport;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by Altanchimeg on 7/8/2016.
 */

public class ServiceReportVasAdapter  extends TableDataAdapter<ServiceReport> {
    private Context mContext;

    public ServiceReportVasAdapter(Context context, List<ServiceReport> data) {
        super(context, data);
        mContext = context;
    }
    // put columns to charge card table
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ServiceReport serviceReport = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderServiceType(serviceReport);
                break;
            case 1:
                renderedView = renderPhone(serviceReport);
                break;
            case 2:
                renderedView = renderIsActivation(serviceReport);
                break;
            case 3:
                renderedView = renderState(serviceReport);
                break;
            case 4:
                renderedView = renderDate(serviceReport);
                break;
        }

        return renderedView;
    }

    //Set columns layout

    private View renderPhone(final ServiceReport serviceReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(serviceReport.getPhone() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderIsActivation(final ServiceReport serviceReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(serviceReport.getIsActivation() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderState(final ServiceReport serviceReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(serviceReport.getOrderStatus() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }

    private View renderDate(final ServiceReport serviceReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(serviceReport.getDate().toString() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }
    private View renderServiceType(final ServiceReport serviceReport) {
        final TextView textView = new TextView(getContext());
        textView.setText(serviceReport.getServiceType().toString() + "");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(18);
        return textView;
    }


}
