package com.litechmeg.sabocale.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.util.PrefUtils;
import com.litechmeg.sabocale.view.adapter.AttendanceListArrayAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AttendanceListActivity extends AppCompatActivity {

    long kamokuId;
    long termId;

    TextView kamokuName;
    ListView listView;

    AttendanceListArrayAdapter adapter;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        pref = getSharedPreferences(PrefUtils.PREF_NAME, MODE_PRIVATE);

        termId = pref.getLong(PrefUtils.PREF_KEY_TERM_ID, 0);

        listView = (ListView) findViewById(R.id.listView1);
        kamokuName = (TextView) findViewById(R.id.kamokuName);

        Intent intent = getIntent();
        if (intent != null) {
            kamokuId = intent.getLongExtra("め", -1);
        }

        Kamoku kamoku = Kamoku.load(Kamoku.class, kamokuId);
        kamokuName.setText(kamoku.name + "のヒストリー");

        if (Kamoku.getAll().size() > 0) {
            // adapterの設定。
            List<Attendance> attendances = Attendance.getList(kamoku.getId(), termId);

            long nowTimeInMills = System.currentTimeMillis();

            for (Attendance attendance : attendances) {
                if (attendance.date > nowTimeInMills) {
                    attendances.remove(attendance);
                }
            }

            Collections.sort(attendances, new Comparator<Attendance>() {

                @Override
                public int compare(Attendance lhs, Attendance rhs) {
                    return Long.valueOf(rhs.date).compareTo(lhs.date);
                }
            });

            adapter = new AttendanceListArrayAdapter(this, R.layout.activity_attendance_list, kamokuId);
            adapter.addAll(attendances);

            listView.setAdapter(adapter);

        }
    }
}
