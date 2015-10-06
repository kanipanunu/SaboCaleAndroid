package com.litechmeg.sabocale.component.activity;

import java.util.Calendar;

import org.kazzz.view.calendar.CalendarSelectionEvent;
import org.kazzz.view.calendar.CalendarView;
import org.kazzz.view.calendar.DateInfo;
import org.kazzz.view.calendar.OnCalendarSelectionListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.litechmeg.sabocale.R;

/**
 * カレンダーを表示する画面(メイン画面)
 */
public class CalendarActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // CalendarViewを表示するだけ
        CalendarView calenderView = (CalendarView) findViewById(R.id.Calendar);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        LayoutParams params = new LinearLayout.LayoutParams(size.x, (int) ((size.x) * 1.4));
        calenderView.setLayoutParams(params);

        calenderView.addOnCalendarSelectionListener(new OnCalendarSelectionListener() {

            @Override
            public void onCalendarSelection(CalendarSelectionEvent event) {
                DateInfo dateInfo = event.getDateInfo();

                // XXX なんだろう？
                String m = getIntent().getStringExtra("め");

                // 念のためnullチェック
                if (dateInfo != null) {
                    // Calendarを取得
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(dateInfo.getYear(), dateInfo.getMonth() - 1, dateInfo.getDay());

                    if (m == null) {
                        Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                        intent.putExtra("selection", calendar);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("selection", calendar);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }

        });
    }
}