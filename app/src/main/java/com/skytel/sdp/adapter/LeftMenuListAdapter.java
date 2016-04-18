package com.skytel.sdp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skytel.sdp.R;

/**
 * Created by bayarkhuu on 4/16/2016.
 */
public class LeftMenuListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mMenus;
    private String[] mIcons;
    private LayoutInflater mLayoutInflater = null;

    public LeftMenuListAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mMenus = context.getResources().getStringArray(R.array.leftmenu_array);
        mIcons = context.getResources().getStringArray(R.array.leftmenu_ic_array);
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mMenus.length;
    }

    @Override
    public Object getItem(int position) {
        return mMenus[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.left_menu_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

//		viewHolder.mMenu.setTypeface(roboto_light);
        viewHolder.leftMenuName.setText(mMenus[position] + "");
        viewHolder.leftMenuName.setTextColor(mContext.getResources().getColor(R.color.colorMenuText));
        try {
            Drawable d = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(mIcons[position], "drawable", mContext.getPackageName()));
            viewHolder.leftMenuIcon.setBackground(d);
        } catch (Exception e) {
        }

        return v;

    }

    class CompleteListViewHolder {
        public TextView leftMenuName;
        //        public View underline;
        public ImageView leftMenuIcon;
        //      public LinearLayout container;

        public CompleteListViewHolder(View base) {
            leftMenuName = (TextView) base.findViewById(R.id.leftMenuName);
            //     underline = (View) base.findViewById(R.id.underline);
            leftMenuIcon = (ImageView) base.findViewById(R.id.leftMenuIcon);
            //   container = (LinearLayout) base.findViewById(R.id.container);
        }
    }
}
