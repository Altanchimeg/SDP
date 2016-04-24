package com.skytel.sdp.ui;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skytel.sdp.R;
import com.skytel.sdp.ui.newnumber.NumberChoiceFragment;
import com.skytel.sdp.ui.newnumber.NumberOrderReportFragment;

public class TabNewNumberFragment extends Fragment {

<<<<<<< HEAD:app/src/main/java/com/skytel/sdp/ui/NewNumberFragment.java
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
=======
    FragmentPagerAdapter fragmentPagerAdapter;
>>>>>>> origin/master:app/src/main/java/com/skytel/sdp/ui/TabNewNumberFragment.java
    ViewPager mViewPager;

    public TabNewNumberFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_pager, container, false);

<<<<<<< HEAD:app/src/main/java/com/skytel/sdp/ui/NewNumberFragment.java
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager_new_number);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
=======
        fragmentPagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager_view);
        mViewPager.setAdapter(fragmentPagerAdapter);
>>>>>>> origin/master:app/src/main/java/com/skytel/sdp/ui/TabNewNumberFragment.java

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_view);
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.tab_newnumber_order)));
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.tab_order_report)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

<<<<<<< HEAD:app/src/main/java/com/skytel/sdp/ui/NewNumberFragment.java
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
=======
    public static class FragmentPagerAdapter extends FragmentStatePagerAdapter {

        public FragmentPagerAdapter(FragmentManager fm) {
>>>>>>> origin/master:app/src/main/java/com/skytel/sdp/ui/TabNewNumberFragment.java
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            Fragment fragment;
            if (i == 0) {
                fragment = new NumberChoiceFragment();
            } else {
                fragment = new NumberOrderReportFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + (position + 1);
        }
    }

}
