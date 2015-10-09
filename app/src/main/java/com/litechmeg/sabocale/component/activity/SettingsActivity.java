package com.litechmeg.sabocale.component.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Term;
import com.litechmeg.sabocale.component.adapter.KamokuListArrayAdapter;

/**
 * 時間割を読み込む画面
 */
public class SettingsActivity extends Activity {
    // よくわからないもの
    // TODOdatePickerをYYMMDDのstring形式に
    String dateEnd;
    String dateStart;
    TextView dateStartText, dateEndText;
    // ListView関連
    ListView kamokuListView;
    KamokuListArrayAdapter adapter;
    Term term;
    String termName;
    EditText termEdit;

    //    SharedPreferences prefTerm = getSharedPreferences("しぇあぷり", MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_cal);
    }

    /**
     * 時間割ファイルをロードする(もしかしたら不要になるかも)
     */
    public void startsellect(View v) {
        Intent intent = new Intent(SettingsActivity.this, CalendarActivity.class);
        intent.putExtra("め", "setting");
        startActivityForResult(intent, 1);
    }

    public void endsellect(View v) {
        Intent intent = new Intent(SettingsActivity.this, CalendarActivity.class);
        intent.putExtra("め", "setting");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // intentから返ってきた情報が RESULT_OK だったら
        if (resultCode == RESULT_OK) {
            // 選択した日時を取得
            Calendar cal = (Calendar) data.getExtras().get("selection");
            String date = String.format("%04d%02d%02d", // yyyyMMdd形式に表示
                    cal.get(Calendar.YEAR), // 年
                    cal.get(Calendar.MONTH) + 1, // 月
                    cal.get(Calendar.DAY_OF_MONTH)); // 日
            int dow = cal.get(Calendar.DAY_OF_WEEK);

            // requestCode == 1 =>
            if (requestCode == 1) {
                // スタートのpickerにセット
                // datePickerStart.init(cal.get(Calendar.YEAR),
                // cal.get(Calendar.MONTH) - 1,
                // cal.get(Calendar.DAY_OF_MONTH), null);
                dateStart = date;

                dateStartText.setText(dateStart);
            } else // requestCode == 2 =>
                if (requestCode == 2) {
                    // エンドのpickerにセット
                    // datePickerEnd.init(cal.get(Calendar.YEAR),
                    // cal.get(Calendar.MONTH) - 1,
                    // cal.get(Calendar.DAY_OF_MONTH), null);
                    dateEnd = date;
                    dateEndText.setText(dateEnd);
                }
        }

    }


    public void saveAttendance(View v) {

        View parent = LayoutInflater.from(this).inflate(R.layout.dialog_term_edit, null);
        parent.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
        // Viewの関連付け
        final EditText EditTermName = (EditText) parent.findViewById(R.id.editText1);
        final TextView saveButton = (Button) parent.findViewById(R.id.saveButton);


        // その科目の数を取ってくる

        // あらーとダイアログの生成
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder //
                .setTitle("") // タイトルをセット
                .setMessage("")// メッセージをセット
                .setView(parent) // Viewをセット
                .show(); // アラートダイアログの表示
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termName = "" + EditTermName;

            }
        });


    }


}


