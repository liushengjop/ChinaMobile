package com.my898tel.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liusheng on 14-9-9.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> mList;

    public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }
}
