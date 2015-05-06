package com.litechmeg.sabocale.view.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

public class AttendanceListArrayAdapter extends ArrayAdapter<Attendance> {
	LayoutInflater layoutInflater_;

	// 表示したい日時情報
	long mKamokuid;

	public AttendanceListArrayAdapter(Context context, int resource, long kamokuid) {
		super(context, resource);

		// layoutInflaterを取得
		layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mKamokuid = kamokuid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// layoutInflaterでリソースからViewを生成
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.list_item_kamoku, null);
		}

		// layoutparamsでサイズ変更
		convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 150));

		// 奇数偶数で背景色を変える
		if (position % 2 == 0) {
			convertView.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
		} else {
			convertView.setBackgroundColor(Color.rgb(0xF8, 0xF8, 0xF8));
		}

		// 現在位置のkamoku取得
		final Attendance attendance = getItem(position);

		// 部品の関連付け
		final TextView absenceTextView = (TextView) convertView.findViewById(R.id.absence);
		final TextView kamokuCountTextView = (TextView) convertView.findViewById(R.id.allClassCount);
		final TextView nameTextView = (TextView) convertView.findViewById(R.id.kamoku_name);
		final TextView mainasuTextView = (TextView) convertView.findViewById(R.id.textView2);
		final TextView kamokuNumTextView = (TextView) convertView.findViewById(R.id.number);
		final Button attendButton = (Button) convertView.findViewById(R.id.attendButton);
		final Button absenceButton = (Button) convertView.findViewById(R.id.absenceButton);
		final Button lateButton = (Button) convertView.findViewById(R.id.lateButton);
		final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.layoutkazu);

		// 名前のセット

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(attendance.date);
		String dayOfWeek_str = new SimpleDateFormat("E", Locale.JAPAN).format(calendar.getTime());
		String date = calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DATE) + "日" + "（" + dayOfWeek_str + "）";
		nameTextView.setText(date);
		nameTextView.setTextSize(150 / date.length());

		String kamokuNum;
		if (position == 0) {
			kamokuNum = "前回";
		} else if (position == 1) {
			kamokuNum = "前々回";
		} else {
			kamokuNum = "" + (getCount() - position) + "回目";
		}
		kamokuNumTextView.setText(kamokuNum);
		kamokuNumTextView.setTextSize(45 / kamokuNum.length());
		kamokuCountTextView.setVisibility(View.GONE);
		absenceTextView.setVisibility(View.GONE);
		mainasuTextView.setVisibility(View.GONE);
		linearLayout.setVisibility(View.GONE);

		// しーくばーについて。
		// 現在位置の出席を取得(nullはあり得ない)
		// seekbarに現在のステータスを設定
		switch (attendance.status) {
		case 0:
			convertView.setAlpha(1f);
			// まだ
		case 3:
			attendButton.setAlpha(1f);
			absenceButton.setAlpha(0.3f);
			lateButton.setAlpha(0.3f);
			attendance.status = 0;
			attendance.save();
			break;
		// 欠席
		case 1:
			attendButton.setAlpha(0.3f);
			absenceButton.setAlpha(1f);
			lateButton.setAlpha(0.3f);

			break;
		// 遅刻
		case 2:
			attendButton.setAlpha(0.3f);
			absenceButton.setAlpha(0.3f);
			lateButton.setAlpha(1f);
			break;

		// 休講
		case 4:
			convertView.setAlpha(50);
			break;

		default:
			break;
		}

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

		// ここで作ったViewを返す
		return convertView;
	}

}
