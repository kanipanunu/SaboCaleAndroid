package com.litechmeg.sabocale.util;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;

public class KamokuListArrayAdapter extends ArrayAdapter<Kamoku> {
	// activityモード
	// 0 = DayAttendanceCalender
	// 1 = KamokuListActivity
	// 2 = SettingActivity
	int mMode = 0;

	LayoutInflater layoutInflater_;

	// 表示したい日時情報
	String date;

	public KamokuListArrayAdapter(Context context, int resource, String date, int mode) {
		super(context, resource);

		mMode = mode;
		if (mMode == 0) {
			this.date = date;
		}
		// layoutInflaterを取得
		layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// layoutInflaterでリソースからViewを生成
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.list_item_kamoku, null);
		}
		convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 150));

		// 奇数偶数で背景色を変える
		if (position % 2 == 0) {
			convertView.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
		} else {
			convertView.setBackgroundColor(Color.rgb(0xF8, 0xF8, 0xF8));
		}

		// 現在位置のkamoku取得
		final Kamoku kamoku = getItem(position);

		// 部品の関連付け
		final TextView absenceTextView = (TextView) convertView.findViewById(R.id.absence);
		final TextView kamokuCountTextView = (TextView) convertView.findViewById(R.id.allClassCount);
		final TextView nameTextView = (TextView) convertView.findViewById(R.id.kamoku_name);
		final TextView mainasuTextView = (TextView) convertView.findViewById(R.id.textView2);
		final TextView kamokuNumTextView = (TextView) convertView.findViewById(R.id.number);
		final Button attendButton = (Button) convertView.findViewById(R.id.attendButton);
		final Button absenceButton = (Button) convertView.findViewById(R.id.absenceButton);
		final Button lateButton = (Button) convertView.findViewById(R.id.lateButton);
		final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.sousa);

		// 名前のセット
		nameTextView.setText(kamoku.name);

		if (kamoku.name.length() >= 4) {
			nameTextView.setTextSize(100 / (kamoku.name).length());
		} else {
			nameTextView.setTextSize(30);
		}
		kamokuNumTextView.setText("" + ((position) + 1));

		// 出席回数とかを取ってきて表示する
		kamokuCountTextView.setText("" + (kamoku.size - kamoku.kyuko));
		absenceTextView.setText(kamoku.absenceCount + kamoku.late / 3 + "");

		// モードによって部品の表示を変える
		if (mMode == 0) {
			kamokuCountTextView.setVisibility(View.GONE);
			absenceTextView.setVisibility(View.GONE);
			mainasuTextView.setVisibility(View.GONE);
		} else if (mMode == 1) {
			mainasuTextView.setVisibility(View.GONE);
			linearLayout.setVisibility(View.GONE);
			kamokuNumTextView.setVisibility(View.GONE);
		} else if (mMode == 2) {
			kamokuCountTextView.setVisibility(View.GONE);
			absenceTextView.setVisibility(View.GONE);
			mainasuTextView.setVisibility(View.GONE);
			attendButton.setVisibility(View.GONE);
			absenceButton.setVisibility(View.GONE);
			lateButton.setVisibility(View.GONE);
			kamokuNumTextView.setVisibility(View.GONE);
		}

		// しーくばーについて。
		if (mMode == 0) {
			// 現在位置の出席を取得(nullはあり得ない)
			final Attendance attendance = Attendance.get(date, position,0);//後で変数に

			// 出席
			attendButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					attendance.status = 0;
					attendance.save();
					attendButton.setAlpha(1f);
					absenceButton.setAlpha(0.3f);
					lateButton.setAlpha(0.3f);
				}
			});
			absenceButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					attendance.status = 1;
					attendance.save();
					attendButton.setAlpha(0.3f);
					absenceButton.setAlpha(1f);
					lateButton.setAlpha(0.3f);

				}
			});
			lateButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					attendance.status = 2;
					attendance.save();
					attendButton.setAlpha(0.3f);
					absenceButton.setAlpha(0.3f);
					lateButton.setAlpha(1f);
				}
			});

			// seekbarに現在のステータスを設定
			if (attendance.status == 3) {
				attendButton.setAlpha(1f);
				absenceButton.setAlpha(0.3f);
				lateButton.setAlpha(0.3f);

				Calendar calendar = Calendar.getInstance();
				String thisdate = String.format("%04d%02d%02d", // yyyyMMdd形式に表示
						calendar.get(Calendar.YEAR), // 年
						calendar.get(Calendar.MONTH) + 1, // 月
						calendar.get(Calendar.DAY_OF_MONTH)); // 日
				if (thisdate.compareTo(attendance.date) >= 0) {
					attendance.status = 0;// 今日よりも前で未選択のがあったら出席扱い。
					attendance.save();
				} else {
					attendButton.setClickable(false);
					absenceButton.setClickable(false);
					lateButton.setClickable(false);
				}

			} else if (attendance.status == 4) {
				convertView.setAlpha(0.3f);
				absenceButton.setClickable(false);
				attendButton.setClickable(false);
				lateButton.setClickable(false);
				// TODO positionのListViewを取ってきてsetalphaして半透明に。
				nameTextView.setText("休講（" + kamoku.name + "）");
				String name = "休講（" + kamoku.name + "）";
				if (name.length() >= 4) {
					nameTextView.setTextSize(100 / (name).length());
				}
			} else {

			}

			if (attendance.status == 0) {
				convertView.setAlpha(1f);
				attendButton.setAlpha(1f);
				absenceButton.setAlpha(0.3f);
				lateButton.setAlpha(0.3f);
			} else if (attendance.status == 1) {
				attendButton.setAlpha(0.3f);
				absenceButton.setAlpha(1f);
				lateButton.setAlpha(0.3f);
			} else if (attendance.status == 2) {
				attendButton.setAlpha(0.3f);
				absenceButton.setAlpha(0.3f);
				lateButton.setAlpha(1f);
			}

		}
		if (kamoku.name.equals("free")) {
			convertView.setVisibility(View.INVISIBLE);
		}

		// ここで作ったViewを返す
		return convertView;

	}
}
