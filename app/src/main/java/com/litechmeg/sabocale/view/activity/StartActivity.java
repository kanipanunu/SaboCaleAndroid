package com.litechmeg.sabocale.view.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

import com.activeandroid.query.Select;
import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.util.Twitter;

/**
 * 起動画面
 */
public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences preference = getSharedPreferences("しぇあぷり", MODE_PRIVATE);
        Editor firstBoot = preference.edit();
        /*
        if (preference.getBoolean("あ", false) == false) {
            Intent intent = new Intent(StartActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
        } else {
        }
        */
        if (Kamoku.getAll().size() != 0) {
            reload();
        }

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
        Intent intent = new Intent(StartActivity.this, DayAttendanceActivity.class);
        intent.putExtra("selection", "a");
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

    /**
     * Twitterに投稿する
     *
     * @param v
     */
    public void twittButton(View v) {
        Twitter.tweet(this, "めぐめぐ");
    }

    public static void reload() {
        class MyAsyncTask extends AsyncTask<String, Integer, Long> implements OnCancelListener {

            final String TAG = "MyAsyncTask";
            Context context;

            public MyAsyncTask(Context context) {
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            protected Long doInBackground(String... params) {

                List<Kamoku> kamokus = Kamoku.getAll();
                for (int i = 0; i < kamokus.size(); i++) {
                    Kamoku kamoku = kamokus.get(i);
                    List<Attendance> getList = Attendance.get(kamoku.getId(),0);//後で変数に
                    List<Attendance> putlist = new ArrayList<Attendance>();
                    Calendar calendar = Calendar.getInstance();
                    String thisdate = String.format("%04d%02d%02d", // yyyyMMdd形式に表示
                            calendar.get(Calendar.YEAR), // 年
                            calendar.get(Calendar.MONTH) + 1, // 月
                            calendar.get(Calendar.DAY_OF_MONTH)); // 日

                    Collections.sort(getList, new Comparator<Attendance>() {

                        @Override
                        public int compare(Attendance lhs, Attendance rhs) {
                            // TODO 自動生成されたメソッド・スタブ
                            return rhs.date.compareTo(lhs.date);
                        }
                    });

                    for (int l = 0; l < getList.size(); l++) {
                        if (Integer.valueOf(getList.get(l).date) < Integer.valueOf(thisdate)) {
                            putlist.add(getList.get(l));
                        }
                    }
                    for (int l = 0; l < putlist.size(); l++) {
                        if (putlist.get(l).status == 3) {
                            putlist.get(l).status = 0;
                            putlist.get(l).save();
                        }
                    }

                }

                return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

}
