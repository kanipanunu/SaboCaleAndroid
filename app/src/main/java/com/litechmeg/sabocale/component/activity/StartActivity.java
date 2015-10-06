package com.litechmeg.sabocale.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.activeandroid.query.Select;
import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;

import java.util.List;

/**
 * 起動画面
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /**
     * ログを見るためだけのボタン？ TodoActivityが未完のため未実装
     *
     * @param v
     */
    public void todo(View v) {
        List<Attendance> attendanceList = new Select().from(Attendance.class).execute();

        for (int i = 0; i < attendanceList.size(); i++) {
            System.out.println( // ログ出力
                    "日付:" + attendanceList.get(i).date + //
                            "　時間:" + attendanceList.get(i).period + //
                            "　科目:" + attendanceList.get(i).kamokuId + //
                            "　状況:" + attendanceList.get(i).status + //
                            " ID:" + attendanceList.get(i).getId()); //
        }

        // TODO todoActivityに遷移(未完)
        // Intent intent = new Intent(StartActivity.this, TodoActivity.class);
        // startActivity(intent);
    }

    /**
     * 授業を計算するActivityに遷移
     *
     * @param v
     */
    public void zyugyou(View v) {
        Intent intent = new Intent(StartActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * カレンダー表示Activityに遷移
     *
     * @param v
     */
    public void calendar(View v) {
        Intent intent = new Intent(StartActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    public void todayButton(View v) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 科目のリストを表示するActivityに遷移
     *
     * @param v
     */
    public void intentList(View v) {
        Intent intent = new Intent(StartActivity.this, KamokuListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

}
