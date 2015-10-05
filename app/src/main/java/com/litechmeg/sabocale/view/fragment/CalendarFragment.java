package com.litechmeg.sabocale.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.view.activity.DayAttendanceActivity;

import org.kazzz.view.calendar.CalendarSelectionEvent;
import org.kazzz.view.calendar.CalendarView;
import org.kazzz.view.calendar.DateInfo;
import org.kazzz.view.calendar.OnCalendarSelectionListener;

import java.util.Calendar;


public class CalendarFragment extends Fragment {
    CalendarView calenderView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 第３引数のbooleanは"container"にreturnするViewを追加するかどうか
        //trueにすると最終的なlayoutに再度、同じView groupが表示されてしまうのでfalseでOKらしい
        // CalendarViewを表示するだけ
        View layout = inflater.inflate(R.layout.activity_calendar, container, false);

        calenderView = (CalendarView) layout.findViewById(R.id.Calendar);
        calenderView.addOnCalendarSelectionListener(new OnCalendarSelectionListener() {
            @Override
            public void onCalendarSelection(CalendarSelectionEvent event) {
                DateInfo dateInfo = event.getDateInfo();

                // 念のためnullチェック
                if (dateInfo != null) {
                    // Calendarを取得
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(dateInfo.getYear(), dateInfo.getMonth() - 1, dateInfo.getDay());
                    calendar.setTimeInMillis(calendar.getTimeInMillis() - calendar.getTimeInMillis() % (60 * 60 * 24 * 1000));

                    Intent intent = new Intent(getActivity(), DayAttendanceActivity.class);
                    intent.putExtra("selection", calendar.getTimeInMillis());
                    startActivity(intent);
                }
            }
        });

        return layout;

    }
}




