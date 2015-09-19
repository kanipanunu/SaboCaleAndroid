package com.litechmeg.sabocale.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.litechmeg.sabocale.view.fragment.CalendarView;
import com.litechmeg.sabocale.view.fragment.DayAttendanceFragment;
import com.litechmeg.sabocale.view.fragment.KamokuListFragment;
import com.litechmeg.sabocale.view.fragment.SettingsView;

/**
 * Created by megukanipan on 2015/04/18.
 */
public class MainTabPagerAdapter extends FragmentPagerAdapter {

    public MainTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new DayAttendanceFragment();

            case 1:
                return new CalendarView();

            case 2:
                return new KamokuListFragment();

            case 3:
                return new SettingsView();

        }
        return null;
    }


    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "今日の";
            case 1:
                return "カレンダー";
            case 2:
                return "見る";
            case 3:
                return "設定！";
        }
        return null;
    }
}
