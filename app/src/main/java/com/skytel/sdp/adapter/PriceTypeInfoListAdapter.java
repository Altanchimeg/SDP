package com.skytel.sdp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skytel.sdp.R;
import com.skytel.sdp.entities.PriceType;
import com.skytel.sdp.utils.Constants;

import java.util.List;

public class PriceTypeInfoListAdapter extends BaseAdapter implements Constants {
    String TAG = PriceTypeInfoListAdapter.class.getName();
    private Context context;
    private String[] mPackageTypes;
    private LayoutInflater mInflater;

    public PriceTypeInfoListAdapter(Context context, List<PriceType> priceTypes) {
        this.context = context;
        mPackageTypes = context.getResources().getStringArray(R.array.skydealer_charge_card_types);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPackageTypes.length;
    }

    @Override
    public Object getItem(int position) {
        return mPackageTypes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderContact viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolderContact();
            convertView = mInflater.inflate(R.layout.newnumber_pricetypeinfo_item, null);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.info = (TextView) convertView.findViewById(R.id.info);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderContact) convertView.getTag();
        }
        viewHolder.price.setText(mPackageTypes[position]);
        viewHolder.info.setText(mPackageTypes[position]);

        return convertView;
    }

    class ViewHolderContact {
        public TextView price;
        public TextView info;
    }
}
