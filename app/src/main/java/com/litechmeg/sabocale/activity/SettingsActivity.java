package com.litechmeg.sabocale.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;
import com.litechmeg.sabocale.util.KamokuListArrayAdapter;

import org.w3c.dom.Text;

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
       termEdit=(EditText)findViewById(R.id.termname);
        termEdit.setText("ターム名を入力してください");

        // if (preference.getBoolean("あ", false) == false) {
        // load();
        // Term sterm = new Term();
        // sterm.date = "20140401";
        // sterm.dayOfWeek = 2;
        // sterm.se = 0;
        // sterm.termName = termName;
        // sterm.save();
        // Term eterm = new Term();
        // eterm.date = "20150331";
        // eterm.dayOfWeek = 2;
        // eterm.se = 1;
        // eterm.termName = termName;
        // eterm.save();
        // load();
        // Intent intent = new Intent(SettingsActivity.this,
        // FirstActivity.class);
        // startActivity(intent);
        //
        // } else {
        //
        // }
        // 関連づけ
        // adapterを生成
        adapter = new KamokuListArrayAdapter(this, R.layout.activity_kamoku_list, null, 2);

        // ListViewにadapterをセット
        dateStartText = (TextView) findViewById(R.id.dateStartText);
        dateEndText = (TextView) findViewById(R.id.dateEndText);
        if (Term.getAll().size() != 0) {
            dateStartText.setText(Term.get(0).date);
            dateEndText.setText(Term.get(1).date);
        }
        Subject.deleteAll(); // もとある時間割を消去

        List<Kamoku> kamokus = Kamoku.getAll(); // 科目全体を取得
        adapter.addAll(kamokus); // adapterに科目情報をセット

		/*
         * Subjectを全取得してソート(不要) List<Subject> subjectList = new
		 * Select().from(Subject.class).execute(); Collections.sort(subjectList,
		 * new Comparator<Subject>() {
		 * 
		 * @Override public int compare(Subject lhs, Subject rhs) { //
		 * 自動生成されたメソッド・スタブ if(lhs.period < rhs.period){ if(lhs.dayOfWeek <
		 * rhs.dayOfWeek){ return 1; }else{ return -1; } }else{ if(lhs.dayOfWeek
		 * < rhs.dayOfWeek){ return 1; }else{ return -1; } } // return
		 * lhs.dayOfWeek > rhs.dayOfWeek ? 1 : -1; } });
		 */
    }

    /**
     * 時間割ファイルをロードする(もしかしたら不要になるかも)
     */
    public void load() {
        try {
            // ファイルの読み込み
            InputStream is = getResources().openRawResource(R.raw.zikanwari);
            BufferedReader brf = new BufferedReader(new InputStreamReader(is));

            int period = 0; // 時限の数
            String line; // 読み込んだファイルの行

            while ((line = brf.readLine()) != null) {

                // 一行ごとに時間割の読み込み
                String[] jikan = line.split(",");

                System.out.println("len: " + jikan.length);
                for (int i = 0; i < jikan.length; i++) {
                    Kamoku kamoku;

                    // もし空白でなければ(空白はスルー)
                    if (!jikan[i].equals("")) {
                        kamoku = Kamoku.get(jikan[i]);
                        // 名前が jikan[j] のKamokuをロード
                        // なければ新しく作る
                        if (kamoku == null) {
                            kamoku = new Kamoku();
                            kamoku.name = jikan[i];
                            kamoku.save();
                        }
                        Log.d(kamoku.name, jikan[i]);
                    } else {
                        kamoku = Kamoku.get("free");
                        if (kamoku == null) {
                            kamoku = new Kamoku();
                            kamoku.name = "free";
                            kamoku.save();
                        }
                    }

                    // TODO この辺り、コンストラクタを作ったほうがいいかも？
                    // new Subject(name, dayOfWeek, period);
                    Subject subject = new Subject(); // 新しいSubjectを作成
                    subject.name = kamoku.name; // ファイルから取得した科目名をセット
                    subject.dayOfWeek = i + 1; // 曜日をセット
                    subject.period = period; // 時限をセット
                    subject.kamokuId = kamoku.getId(); // 科目のIDをセット
                    subject.save(); // Subjectの保存
                }

                // 次の時限に進む
                period++;
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

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

    public void keisan(View v) {
        List<Subject> subjects = Subject.getAll();
        if (subjects.size() != 0) {

        } else {
            load(); // 時間割をロードする
        }
        Intent intent = new Intent(SettingsActivity.this, EditActivity.class);
        startActivity(intent);

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
                termName=""+EditTermName;


                // TODO 自動生成されたメソッド・スタブ
                new MyAsyncTask(getApplicationContext()).execute("");
            }
        });

    }

    class MyAsyncTask extends AsyncTask<String, Integer, Long> implements OnCancelListener {

        final String TAG = "MyAsyncTask";
        ProgressDialog dialog;
        Context context;

        public MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(SettingsActivity.this);//context渡すと落ちる？？
            dialog.setTitle("時間割のよみこみをしています。");
            dialog.setMessage("保存中…");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        public void onCancel() {
        }

        @Override
        protected Long doInBackground(String... params) {

            // その科目の数を取ってくる
            String dateStart;
            String dateEnd;
            dateStart = Term.get(0).date;
            int firstDayOfWeek = Term.get(0).dayOfWeek;
            dateEnd = Term.get(1).date;
            System.out.println(dateStart);
            System.out.println(dateEnd);
            int y = Integer.valueOf(dateStart) / 10000;
            int m = (Integer.valueOf(dateStart) % 10000) / 100;
            int d = Integer.valueOf(dateStart) % 100;

            int dayMax = 30;

            dialog.setMax(Integer.valueOf(dateEnd) - Integer.valueOf(dateStart));

            while ((y * 10000) + (m * 100) + (d) <= Integer.valueOf(dateEnd)) {
                System.out.println(y + ":" + m + ":" + d);

                dialog.setProgress((y * 10000) + (m * 100) + (d) - Integer.valueOf(dateStart));

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

                List<Subject> subjects = Subject.getAll(firstDayOfWeek % 7);
                //同じtermnameのものがあったら
                termName=""+termEdit;
                List<Term> terms=Term.get(termName);
                if(terms.size()>=0){

                }else {
                    Term term=new Term();


                    // その日の出席を全部取得して、あったら
                    if (Attendance.getAll(Integer.toString((y * 10000) + (m * 100) + (d))).size() != 0) {
                        // デバッグ出力
                        List<Attendance> attendances = Attendance.getAll(Integer.toString((y * 10000) + (m * 100)
                                + (d)));
                        for (int i = 0; i < attendances.size(); i++) {
                            final Attendance attendance = Attendance.get(
                                    (Integer.toString((y * 10000) + (m * 100) + (d))), i,0);//後で変数に
                            attendance.kamokuId = subjects.get(i).kamokuId;
                            attendance.save();
                            System.out.println("更新！" + attendance.kamokuId+"ターム"+termName+" "+term.getId());
                        }
                    } else {// 無かったらつくる。※Subject参照
                        for (int i = 0; i < subjects.size(); i++) {
                            Attendance attendance = new Attendance();
                            attendance.termId = term.getId();
                            attendance.date = Integer.toString((y * 10000) + (m * 100) + (d));
                            attendance.period = i;
                            attendance.kamokuId = subjects.get(i).kamokuId;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DATE));
                            int date = ((calendar.get(Calendar.YEAR) * 10000)
                                    + ((calendar.get(Calendar.MONTH) + 1) * 100) + // 月
                                    calendar.get(Calendar.DAY_OF_MONTH)); // 日
                            if (((y * 10000) + (m * 100) + (d)) <= date) {
                                attendance.status = 0;
                            } else {
                                attendance.status = 3;
                            }
                            attendance.save();
                            System.out.println("作った！" + attendance.kamokuId+"ターム"+termName+" "+term.getId());
                        }
                    }
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
            }
            return null;

        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO 処理が終了したらすること
            super.onPostExecute(result);

            dialog.dismiss();
        }
    }


}


