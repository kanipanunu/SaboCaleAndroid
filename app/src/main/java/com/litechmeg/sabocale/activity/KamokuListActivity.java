package com.litechmeg.sabocale.activity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import com.litechmeg.sabocale.util.KamokuListArrayAdapter;

/**
 * 科目のリストを表示する画面
 */
public class KamokuListActivity extends Activity {

	EditText editText;

	// ListView関連
	ListView listview;
	KamokuListArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kamoku_list);
		editText = (EditText) findViewById(R.id.editText1);
		editText.setVisibility(View.GONE);

		// Adapterの設定
		adapter = new KamokuListArrayAdapter(this, R.layout.activity_kamoku_list, null, 1);
		List<Kamoku> kamokus = Kamoku.getAll();
		for (int i = 0; i < kamokus.size(); i++) {
			kamokus.get(i).calculate();
            if (Attendance.get(kamokus.get(i).getId(),0).size() == 0) {//後で変数にする
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
				// TODO 自動生成されたメソッド・スタブ
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
				// TODO 自動生成されたメソッド・スタブ
				final Kamoku kamoku = adapter.getItem(position);

				Intent intent = new Intent(KamokuListActivity.this, AttendanceListActivity.class);
				intent.putExtra("め", kamoku.getId());
				startActivity(intent);

				return false;
			}

		});

	}

	public static List<Attendance> dateLook(long x) {
		List<Attendance> attendances = new ArrayList<Attendance>();
		String dateStart;
		String dateEnd;
		dateStart = Term.get(0).dateStert;
		int firstDayOfWeek = Term.get(0).dayOfWeek;
		dateEnd = Term.get(1).dateStert;
		System.out.println(dateStart);
		System.out.println(dateEnd);
		int y = Integer.valueOf(dateStart) / 10000;
		int m = (Integer.valueOf(dateStart) % 10000) / 100;
		int d = Integer.valueOf(dateStart) % 100;

		int dayMax = 30;
		while ((y * 10000) + (m * 100) + (d) <= Integer.valueOf(dateEnd)) {
			switch (m) {// 曜日の最大値設定。
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				dayMax = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				dayMax = 30;
				break;
			case 2:
				if (y % 4 == 0) {
					dayMax = 28;
				} else {
					dayMax = 29;
				}
				break;
			default:
				break;
			}
			attendances.addAll(Attendance.get(Integer.toString((y * 10000) + (m * 100) + (d)), x,1));//後で変数に
			d++;// 日付ふえるよ
			firstDayOfWeek++;

			if (d > dayMax) {// 月が変わるよ
				m++;
				d = 1;
				if (m > 12) {// 年が変わるよ
					y++;
					m = 1;
				}
			}
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
