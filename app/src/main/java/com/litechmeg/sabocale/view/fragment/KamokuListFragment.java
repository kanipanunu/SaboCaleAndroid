package com.litechmeg.sabocale.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.view.adapter.KamokuListArrayAdapter;
import com.litechmeg.sabocale.view.activity.AttendanceListActivity;

import java.util.Comparator;
import java.util.List;

/**
 * Created by megukanipan on 2015/04/23.
 */
public class KamokuListFragment extends Fragment{

    EditText editText;

    // ListView関連
    ListView listview;
    KamokuListArrayAdapter adapter;


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 第３引数のbooleanは"container"にreturnするViewを追加するかどうか
        //trueにすると最終的なlayoutに再度、同じView groupが表示されてしまうのでfalseでOKらしい
        View v=inflater.inflate(R.layout.activity_kamoku_list, null, false);

        SharedPreferences pref = getActivity().getSharedPreferences("TermSellect", getActivity().MODE_PRIVATE);

        listview = (ListView) v.findViewById(R.id.listView1);

        final long termId=pref.getLong("TermId",0);

        // Adapterの設定
        adapter = new KamokuListArrayAdapter(getActivity(), R.layout.activity_kamoku_list, 0,termId, 1);
        List<Kamoku> kamokus = Kamoku.getAll(termId);//
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
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
                // dialog用のレイアウトを取得
                inflater.inflate(R.layout.dialog_kamoku_detail, (ViewGroup) parent.findViewById(R.id.layout_Dialog));
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

                List<Attendance> attendances = Attendance.getList(kamoku.getId(), termId);

                kamokuCountText.setText("授業合計" + attendances.size() + "");// attendanceがnull？？
                int absenceCount = 0;
                int attend = 0;
                int late = 0;
                for (int i = 0; i < attendances.size(); i++) {
                    if (attendances.get(i).status == Attendance.STATUS_ATTENDANCE) {
                        attend++;
                    } else if (attendances.get(i).status == Attendance.STATUS_ABSENT) {
                        absenceCount++;
                    } else if (attendances.get(i).status == Attendance.STATUS_LATE) {
                        late++;
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog = alertDialogBuilder //
                        .setTitle("") // タイトルをセット
                        .setMessage(kamoku.name)// メッセージをセット
                        .setView(parent) // Viewをセット
                        .show(); // アラートダイアログの表示
                intentButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AttendanceListActivity.class);
                        intent.putExtra("め", kamoku.getId());
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }

        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View parent, int position, long arg3) {
                final Kamoku kamoku = adapter.getItem(position);

                Intent intent = new Intent(getActivity(), AttendanceListActivity.class);
                intent.putExtra("め", kamoku.getId());
                startActivity(intent);

                return false;
            }

        });

        return v;

    }
}
