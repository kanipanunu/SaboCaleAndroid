package com.litechmeg.sabocale.component.fragment;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.component.adapter.KamokuListArrayAdapter;
import com.litechmeg.sabocale.util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by megukanipan on 2015/04/19.
 */
public class DayAttendanceFragment extends Fragment {

    TextView dateTextView;
    TextView dayOfWeekTextView;

    // ListView関連
    ListView kamokuListView;
    KamokuListArrayAdapter adapter;

    long date;
    Calendar calendar;
    long termId;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 第３引数のbooleanは"container"にreturnするViewを追加するかどうか
        // trueにすると最終的なlayoutに再度、同じView groupが表示されてしまうのでfalseでOKらしい

        termId = PrefUtils.getTermId(getActivity());

        View v = inflater.inflate(R.layout.fragment_day_attendance, container, false);

        // Viewを関連付け
        dateTextView = (TextView) v.findViewById(R.id.date);
        dayOfWeekTextView = (TextView) v.findViewById(R.id.DayOfWeek);
        kamokuListView = (ListView) v.findViewById(R.id.listView1);

        calendar = Calendar.getInstance();
        // 日付を文字に変換
        date = (calendar.getTimeInMillis()) - (calendar.getTimeInMillis() % (1000 * 3600 * 24));
        Log.d("今日", "" + date);
        setDateText(); // dateTextViewに文字をセット

        // ListView関連
        adapter = new KamokuListArrayAdapter(getActivity(), R.layout.activity_kamoku_list, date, termId, 0);
        setDataToAdapter();
        kamokuListView.setAdapter(adapter);

        kamokuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View parent, final int position, long arg3) {
                // dialog用のレイアウトを取得
                View dialogView = inflater.inflate(R.layout.dialog_subject_edit, null);
                dialogView.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));

                // Viewの関連付け

                // 現在位置のKamokuを取得して名前を表示
                final Kamoku kamoku = adapter.getItem(position);
                final Attendance attendance = Attendance.get(date, position, termId);

                final EditText editKamokuName = (EditText) dialogView.findViewById(R.id.editText1);
                final Button ariButton = (Button) dialogView.findViewById(R.id.arikoma);
                final Button nashiButton = (Button) dialogView.findViewById(R.id.akikoma);
                final Button saveButton = (Button) dialogView.findViewById(R.id.saveButton);

                editKamokuName.setText(kamoku.name + "");

                if (attendance.status == 4) {
                    nashiButton.setAlpha(1f);
                    ariButton.setAlpha(0.3f);
                } else {
                    nashiButton.setAlpha(0.3f);
                    ariButton.setAlpha(1f);
                }

                // あらーとダイアログの生成
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                        .setTitle("変更") // タイトルをセット
                        .setMessage(kamoku.name)// メッセージをセット
                        .setView(dialogView); // Viewをセット

                final AlertDialog dialog = alertDialogBuilder.show(); // アラートダイアログの表示
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = editKamokuName.getText().toString();
                        Log.d("かもく", newName);

                        Kamoku newKamoku = Kamoku.get(newName,termId);
                        // kamokuがなかったら、新しく作る
                        if (newKamoku == null) {
                            newKamoku = new Kamoku();
                            newKamoku.name = newName;
                            newKamoku.save();
                        }
                        // attendanceの科目を更新
                        attendance.kamokuId = newKamoku.getId();
                        attendance.save();

                        // ダイアログを消す
                        dialog.dismiss();

                        // 表示の更新
                        adapter.notifyDataSetChanged();
                    }
                });
                ariButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 科目の入力が新しい科目だったら
                        String newKamokuName = editKamokuName.getText().toString();
                        Kamoku newKamoku = Kamoku.get(newKamokuName,termId);
                        if (newKamoku == null) {
                            newKamoku = new Kamoku();
                            newKamoku.name = newKamokuName;
                            newKamoku.save();
                        }

                        // attendanceの科目を更新
                        attendance.kamokuId = newKamoku.getId();
                        attendance.status = Attendance.STATUS_ATTENDANCE;
                        attendance.save();

                        // 見た目を更新
                        ariButton.setAlpha(1f);
                        nashiButton.setAlpha(0.3f);
                    }

                });

                nashiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 科目状態を休講にする
                        attendance.status = Attendance.STATUS_KYUKO;
                        attendance.save();
                        // 見た目を更新
                        ariButton.setAlpha(0.3f);
                        nashiButton.setAlpha(1f);
                    }
                });

            }
        });
        return v;
    }


    public void setDataToAdapter() {

        List<Attendance> attendances = Attendance.getAll(date, termId);

        ArrayList<Kamoku> kamokus = new ArrayList<Kamoku>();

        for (Attendance attendance : attendances) {
            Kamoku kamoku = Kamoku.get(attendance.kamokuId);
            if (kamoku != null) {
                kamokus.add(kamoku);
            }
        }

        adapter.addAll(kamokus);
    }

    public void setDateText() {
        dateTextView.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        // 曜日を表示
        dayOfWeekTextView.setText(new SimpleDateFormat("E", Locale.JAPAN).format(calendar.getTime()));
    }
}
