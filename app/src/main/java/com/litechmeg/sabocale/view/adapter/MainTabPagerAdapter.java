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
public class MainTabPagerAdapter extends FragmentPagerAdapter{

    public MainTabPagerAdapter(FragmentManager fm){
        super(fm);
    }
    Fragment dayAttendance=new DayAttendanceFragment();
    Fragment calender=new CalendarView();
    Fragment kamokuList=new KamokuListFragment();
    Fragment settings=new SettingsView();

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return dayAttendance;

            case 1:
                return calender;

            case 2:
                return kamokuList;

            case 3:
                return settings;

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
                return"カレンダー";
            case 2:
                return "見る";
            case 3:
                return "設定！";
        }
        return null;
    }
}
