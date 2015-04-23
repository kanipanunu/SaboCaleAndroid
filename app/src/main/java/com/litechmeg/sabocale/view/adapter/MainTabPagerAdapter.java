package com.litechmeg.sabocale.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.view.fragment.DayAttendanceView;

/**
 * Created by megukanipan on 2015/04/18.
 */
public class MainTabPagerAdapter extends FragmentPagerAdapter{

    public MainTabPagerAdapter(FragmentManager fm){
        super(fm);
    }
    Fragment dayAttendance1=new DayAttendanceView();
    Fragment dayAttendance2=new DayAttendanceView();
    Fragment dayAttendance3=new DayAttendanceView();
    Fragment dayAttendance4=new DayAttendanceView();

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return dayAttendance1;

            case 1:
                return dayAttendance2;

            case 2:
                return dayAttendance3;

            case 3:
                return dayAttendance4;

            //TODO returnfragmentする。
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
                return "1";
            case 1:
                return"2";
            case 2:
                return "3";
            case 3:
                return "4";
        }
        return null;
    }
}
