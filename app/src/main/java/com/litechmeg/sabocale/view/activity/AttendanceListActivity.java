package com.litechmeg.sabocale.view.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.util.AttendanceListArrayAdapter;

public class AttendanceListActivity extends Activity {//
	ListView listView;

    AttendanceListArrayAdapter adapter;

    long kamokuid;
	Calendar calendar;
	TextView kamokuName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_list);

		listView = (ListView) findViewById(R.id.listView1);

		Intent intent = getIntent();
		if (intent != null) {
			kamokuid = intent.getLongExtra("め", (long) -1);
		}

		kamokuName = (TextView) findViewById(R.id.kamokuName);
		Kamoku kamoku = Kamoku.load(Kamoku.class, kamokuid);
		kamokuName.setText(kamoku.name + "のヒストリー");
		if (Kamoku.getAll().size() != 0) {
			StartActivity.reload();
			// addapterの設定。
			List<Attendance> getList = Attendance.get(kamoku.getId(),1);//後で変数に修正
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
			adapter = new AttendanceListArrayAdapter(this, R.layout.activity_attendance_list, kamokuid);
			adapter.addAll(putlist);

			listView.setAdapter(adapter);

		}

	}


}
