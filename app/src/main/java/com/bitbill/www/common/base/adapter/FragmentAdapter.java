package com.bitbill.www.common.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/11/25.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<String> titles = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment fragment) {
        fragments.add(fragment);
    }

    public void addItem(String title, Fragment fragment) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && position < titles.size()) {
            return titles.get(position);
        } else {
            return null;
        }
    }
}
