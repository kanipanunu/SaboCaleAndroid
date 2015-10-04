package com.litechmeg.sabocale.view.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Term;
import com.litechmeg.sabocale.view.adapter.KamokuListArrayAdapter;

/**
 * 科目のリストを表示する画面
 */
public class KamokuListActivity extends Activity {

	EditText editText;

	// ListView関連
	ListView listview;
	KamokuListArrayAdapter adapter;

    SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kamoku_list);
        pref = getSharedPreferences("TermSellect", this.MODE_PRIVATE);

        long termId=pref.getLong("TermId",0);

		// Adapterの設定
		adapter = new KamokuListArrayAdapter(this, R.layout.activity_kamoku_list,0,termId, 1);
		List<Kamoku> kamokus = Kamoku.getAll();
		for (int i = 0; i < kamokus.size(); i++) {
			kamokus.get(i).calculate(termId);
            if (Attendance.getList(kamokus.get(i).getId(), termId).size() == 0) {//後で変数にする
                Kamoku.delete(Kamoku.class, kamokus.get(i).getId());//ここがうまく働いていないかも
                System.out.println(kamokus.get(i).name);
                if (!kamokus.get(i).name.equals("free")) {
                    adapter.add(kamokus.get(i));
                }
		    }
		}

		adapter.sort(new Comparator<Kamoku>() {

			@Override
			public int compare(Kamoku lhs, Kamoku rhs) {
				float l = ((float) lhs.absenceCount / (float) lhs.size);
				float r = ((float) rhs.absenceCount / (float) rhs.size);

				return -Float.valueOf(l).compareTo(r);
			}
		});

		// ListViewの設定
		listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				// dialog用のレイアウトを取得
				parent = getLayoutInflater().inflate(R.layout.dialog_kamoku_detail,
						(ViewGroup) findViewById(R.id.layout_Dialog));
				parent.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));

				// Viewの関連付け
				final TextView kamokuName = (TextView) parent.findViewById(R.id.kamoku_name);
				final TextView absenceCountText = (TextView) parent.findViewById(R.id.absenceText);
				final TextView kamokuCountText = (TextView) parent.findViewById(R.id.all_classCountText);
				final TextView sabori = (TextView) parent.findViewById(R.id.saboriCount);
				final TextView attendCountText = (TextView) parent.findViewById(R.id.attendText);
				final Button attendButton = (Button) parent.findViewById(R.id.attendButton);
				final Button absenceButton = (Button) parent.findViewById(R.id.absenceButton);
				final Button lateButton = (Button) parent.findViewById(R.id.lateButton);
				final Button intentButton = (Button) parent.findViewById(R.id.intentButton);

				// 現在位置のKamokuを取得して名前を表示
				final Kamoku kamoku = adapter.getItem(position);
				kamokuName.setText(kamoku.name);
				// attendButton.setVisibility(View.GONE);
				// absenceButton.setVisibility(View.GONE);
				// lateButton.setVisibility(View.GONE);

				// その科目の数を取ってくる

				List<Attendance> attendances = dateLook(kamoku.getId());

				kamokuCountText.setText("授業合計" + attendances.size() + "");// attendanceがnull？？
				int absenceCount = 0;
				int attend = 0;
				int late = 0;
				for (int i = 0; i < attendances.size(); i++) {
					if (attendances.get(i).status == 0) {
						attend++;
					} else if (attendances.get(i).status == 1) {
						absenceCount++;
					} else if (attendances.get(i).status == 2) {
						late++;
					} else {

					}

				}
				absenceCountText.setText("休んだ数" + (absenceCount + (late / 3)) + "");
				attendCountText.setText("出席したかず" + attend + "");
				sabori.setText(((attendances.size() / 3) - (absenceCount + (late / 3))) + "");

				// TODO これはあとで使う
				ProgressBar progressBar = (ProgressBar) parent.findViewById(R.id.progressBar1);
				progressBar.setMax(attendances.size());
				progressBar.setProgress(absenceCount + late / 3);
				progressBar.setSecondaryProgress(absenceCount + late / 3 + attend);

				// あらーとダイアログの生成
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KamokuListActivity.this);
				final AlertDialog dialog = alertDialogBuilder //
						.setTitle("") // タイトルをセット
						.setMessage(kamoku.name)// メッセージをセット
						.setView(parent) // Viewをセット
						.show(); // アラートダイアログの表示
				intentButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(KamokuListActivity.this, AttendanceListActivity.class);
						intent.putExtra("め", kamoku.getId());
						startActivity(intent);
						dialog.dismiss();
					}
				});
			}

		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				final Kamoku kamoku = adapter.getItem(position);

				Intent intent = new Intent(KamokuListActivity.this, AttendanceListActivity.class);
				intent.putExtra("め", kamoku.getId());
				startActivity(intent);

				return false;
			}

		});

	}

	public List<Attendance> dateLook(long x) {
        SharedPreferences pref;
        // FIXME SPConfig内の変数に置き換える
        pref = getSharedPreferences("TermSellect", this.MODE_PRIVATE);

        long termId=pref.getLong("TermId",0);

        List<Attendance> attendances = new ArrayList<Attendance>();
		long dateStart =(Term.get(termId).dateStert);
		long dateEnd =(Term.get(termId).dateEnd);
        int firstDayOfWeek = Term.get(termId).dayOfWeek;
        Calendar calendarStart=Calendar.getInstance();
        Calendar calendarEnd=Calendar.getInstance();

        calendarStart.setTimeInMillis(dateStart);
        calendarEnd.setTimeInMillis(dateEnd);




		System.out.println(dateStart);
		System.out.println(dateEnd);


		int y = calendarStart.get(calendarStart.YEAR);
		int m =calendarStart.get(calendarStart.MONTH);
		int d = calendarStart.get(calendarStart.DATE);

		while (calendarStart.getTimeInMillis() <= dateEnd) {
            calendarStart.getTimeInMillis();
			attendances.addAll(Attendance.get(calendarStart.getTimeInMillis(), x,termId));
            calendarStart.add(Calendar.DATE,1);
			firstDayOfWeek++;

		}
		return attendances;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kamoku_list, menu);
		return true;
	}

}
