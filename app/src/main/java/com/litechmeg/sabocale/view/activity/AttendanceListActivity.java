package com.litechmeg.sabocale.view.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.view.adapter.AttendanceListArrayAdapter;

public class AttendanceListActivity extends Activity {//
    ListView listView;

    AttendanceListArrayAdapter adapter;

    long kamokuid;
    Calendar calendar;
    TextView kamokuName;
    SharedPreferences pref;
    long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        // TODO SPConfig内の変数を使う
        pref = getSharedPreferences("TermSellect", MODE_PRIVATE);

        // TODO SPConfig内の変数を使う
        termId = pref.getLong("TermId", 0);

        listView = (ListView) findViewById(R.id.listView1);
        kamokuName = (TextView) findViewById(R.id.kamokuName);

        Intent intent = getIntent();
        if (intent != null) {
            kamokuid = intent.getLongExtra("め", (long) -1);
        }

        Kamoku kamoku = Kamoku.load(Kamoku.class, kamokuid);
        kamokuName.setText(kamoku.name + "のヒストリー");
        if (Kamoku.getAll().size() > 0) {
            StartActivity.reload(this, termId);
            // addapterの設定。
            List<Attendance> getList = Attendance.getList(kamoku.getId(), termId);
            List<Attendance> putlist = new ArrayList<Attendance>();

            Calendar calendar = Calendar.getInstance();
            calendar.getTimeInMillis();

            Collections.sort(getList, new Comparator<Attendance>() {

                @Override
                public int compare(Attendance lhs, Attendance rhs) {
                    return Long.valueOf(rhs.date).compareTo(lhs.date);
                }
            });

            for (int l = 0; l < getList.size(); l++) {
                if (getList.get(l).date < calendar.getTimeInMillis()) {
                    putlist.add(getList.get(l));
                }
            }
            adapter = new AttendanceListArrayAdapter(this, R.layout.activity_attendance_list, kamokuid);
            adapter.addAll(putlist);

            listView.setAdapter(adapter);

        }

    }


}
