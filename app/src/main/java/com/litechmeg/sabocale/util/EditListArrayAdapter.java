package com.litechmeg.sabocale.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Subject;

public class EditListArrayAdapter extends ArrayAdapter<Subject> {
	LayoutInflater layoutInflater;

	public EditListArrayAdapter(Context context, int resource) {
		super(context, resource);
		// TODO 自動生成されたコンストラクター・スタブ
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	int DayOfWeek;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = layoutInflater.inflate(R.layout.list_item_edit, null);
		}
		convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 150));

		if (position % 2 == 0) {// たがいちがいの色味に
			convertView.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
		} else {
			convertView.setBackgroundColor(Color.rgb(0xF8, 0xF8, 0xF8));
		}
		// 現在位置のSubject取得
		final Subject subject = getItem(position);
		// 部品の関連づけ
		final TextView text = (TextView) convertView.findViewById(R.id.subjectName);
		final TextView periodText = (TextView) convertView.findViewById(R.id.periodText);
		final SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.seekBarka);
		// もともとあるsubjectの当て込み。
		if (subject.name != "free") {
			text.setText(subject.name);
		} else {
			text.setText("空き");
			text.setAlpha(0.3f);
		}
		periodText.setText("" + ((position) + 1));

		return convertView;
	}
}
