package com.skytel.sdp.ui.skydealer;

import android.content.Context;
import android.util.AttributeSet;

import com.skytel.sdp.R;
import com.skytel.sdp.entities.SalesReport;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

public class SortableSalesReportTableView extends SortableTableView<SalesReport> {
    private String[] REPORT_HEADER = {"Phone", "Value", "Success", "Type", "Date"};

    public SortableSalesReportTableView(Context context) {
        this(context, null);
    }

    public SortableSalesReportTableView(Context context, AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableSalesReportTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);
        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, REPORT_HEADER);
        simpleTableHeaderAdapter.setTextColor(context.getResources().getColor(R.color.colorSkytelBlue));
        setHeaderAdapter(simpleTableHeaderAdapter);

        int rowColorEven = context.getResources().getColor(android.R.color.white);
        int rowColorOdd = context.getResources().getColor(R.color.colorTabBackground);
        setDataRowColorizer(TableDataRowColorizers.alternatingRows(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        setColumnWeight(0, 3);
        setColumnWeight(1, 2);
        setColumnWeight(2, 2);
        setColumnWeight(3, 2);
        setColumnWeight(4, 3);

        setColumnComparator(0, SalesReportComparator.getSalesReportPhoneComparator());
//        setColumnComparator(1, SalesReportComparator.getSalesReportPhoneComparator());

    }

}
