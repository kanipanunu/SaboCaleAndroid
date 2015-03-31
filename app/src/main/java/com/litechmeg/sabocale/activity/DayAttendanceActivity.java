package com.litechmeg.sabocale.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.kazzz.util.HolidayUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;
import com.litechmeg.sabocale.util.AttendanceAsyncTask;
import com.litechmeg.sabocale.util.KamokuListArrayAdapter;
import com.litechmeg.sabocale.util.TermListArrayAdapter;
import com.litechmeg.sabocale.util.Twitter;

/**
 * 選択した日の科目を表示する画面
 */
public class DayAttendanceActivity extends ActionBarActivity {
    // TextView(なんのだろう？)
    ActionBarDrawerToggle toggle;

    TextView dateText;
    TextView dayOfWeekTextView;
    // ListView関連
    ListView kamokuListView;
    ListView termListView;
    KamokuListArrayAdapter adapter;
    TermListArrayAdapter termAdapter;
    // 日付
    Calendar calendar;
    // リスト(不要？)
    List<Kamoku> kamokus;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_attendance);

        /**
         * テスト
         */
        Term t = new Term();
        t.save();
        System.out.println(t.getId());

        // Viewを関連付け
        dateText = (TextView) findViewById(R.id.date);
        kamokuListView = (ListView) findViewById(R.id.listView1);
        dayOfWeekTextView = (TextView) findViewById(R.id.DayOfWeek);

        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, layout, R.string.terms, R.string.terms);
        toggle.setDrawerIndicatorEnabled(true);
        layout.setDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // 選択した日付を取得
        if (!getIntent().getExtras().get("selection").equals("a")) {
            calendar = (Calendar) getIntent().getExtras().get("selection");
        } else {
            calendar = Calendar.getInstance();
            // DateInfo dateInfo = event.getDateInfo();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE));
        }
        // 日付を文字に変換
        date = String.format("%04d%02d%02d", // yyyyMMdd形式に表示
                calendar.get(Calendar.YEAR), // 年
                calendar.get(Calendar.MONTH) + 1, // 月
                calendar.get(Calendar.DAY_OF_MONTH)); // 日

        // 日にちを表示
        dateText.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日");// "日で曜日" +
        // (calendar.get(Calendar.DAY_OF_WEEK)));
        // 曜日を表示
        dayOfWeekTextView.setText(new SimpleDateFormat("E", Locale.JAPAN).format(calendar.getTime()));

        // ListViewにAdapterをセット
        adapter = new KamokuListArrayAdapter(this, R.layout.activity_kamoku_list, date, 0);
        termAdapter = new TermListArrayAdapter(this, R.layout.activity_kamoku_list);
        List<Term> terms = Term.getAll();
        for (int i = 0; i < terms.size(); i++) {
            termAdapter.add(terms.get(i));
        }
        termListView = (ListView) findViewById(R.id.navigationDrawer);
        termListView.setAdapter(termAdapter);
        termListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Term term = termAdapter.getItem(position);
                term.delete();


                return true;
            }
        });

        kamokuListView.setAdapter(adapter);
        kamokuListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View parent, final int position, long arg3) {
                // TODO 自動生成されたメソッド・スタブ
                // dialog用のレイアウトを取得
                parent = getLayoutInflater().inflate(R.layout.dialog_subject_edit,
                        (ViewGroup) findViewById(R.id.layout_Dialog));
                parent.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));

                // Viewの関連付け

                // 現在位置のKamokuを取得して名前を表示
                final Kamoku kamoku = adapter.getItem(position);
                // attendButton.setVisibility(View.GONE);
                // absenceButton.setVisibility(View.GONE);
                // lateButton.setVisibility(View.GONE);
                final Attendance attendance = Attendance.get(date, position, 0);//後で変数に

                final EditText EditKamokuName = (EditText) parent.findViewById(R.id.editText1);
                final TextView absenceText = (TextView) parent.findViewById(R.id.absence);
                final Button ariButton = (Button) parent.findViewById(R.id.arikoma);
                final Button nashiButton = (Button) parent.findViewById(R.id.akikoma);
                final Button saveButton = (Button) parent.findViewById(R.id.saveButton);
                EditKamokuName.setText(kamoku.name + "");
                if (attendance.status == 4) {
                    nashiButton.setAlpha(1f);
                    ariButton.setAlpha(0.3f);
                } else {
                    nashiButton.setAlpha(0.3f);
                    ariButton.setAlpha(1f);
                }

                // あらーとダイアログの生成
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DayAttendanceActivity.this);
                final AlertDialog dialog = alertDialogBuilder //
                        .setTitle("変更") // タイトルをセット
                        .setMessage(kamoku.name)// メッセージをセット
                        .setView(parent) // Viewをセット
                        .show(); // アラートダイアログの表示
                saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 自動生成されたメソッド・スタブ
                        kamoku.name = EditKamokuName.getText().toString();
                        Log.d("かもく", kamoku.name);
                        if (Kamoku.get(kamoku.name) != null) {
                            attendance.kamokuId = kamoku.getId();
                        } else {
                            Kamoku newkamoku = new Kamoku();
                            newkamoku.name = kamoku.name;
                            newkamoku.save();
                            attendance.kamokuId = newkamoku.getId();
                        }
                        attendance.save();
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
                ariButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 科目の入力が新しい科目だったら
                        if (Kamoku.get(EditKamokuName.getText().toString()) == null) {
                            Kamoku kamoku = new Kamoku();
                            kamoku.name = EditKamokuName.getText().toString();
                            kamoku.save();
                        }

                        attendance.kamokuId = Kamoku.get(EditKamokuName.getText().toString()).getId();
                        attendance.status = 0;
                        attendance.save();
                        ariButton.setAlpha(1f);
                        nashiButton.setAlpha(0.3f);
                    }

                });

                nashiButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // もしフリーがなかったら(ありえない？)
                        // if (Kamoku.get("free") == null) {
                        // Kamoku kamoku = new Kamoku();
                        // kamoku.name = "free";
                        // kamoku.save();
                        // }
                        // 科目をfreeに変更
                        // attendance.kamokuId = Kamoku.get("free").getId();
                        // 科目状態を休講にする
                        attendance.status = 4;
                        attendance.save();
                        ariButton.setAlpha(0.3f);//
                        nashiButton.setAlpha(1f);
                    }
                });
                return true;
            }

        });

        // もし休日でなければ

        if (HolidayUtil.getHolidayName(calendar).equals("")) {

            // その日の全科目を取得
            kamokus = new ArrayList<Kamoku>();
            List<Attendance> attendances = Attendance.getAll(date);
            for (int i = 0; i < attendances.size(); i++) {
                // その日のSubjectで、i時限目の出席がすでにあれば取得
                Attendance attendance = Attendance.get(date, i, 1);//後で変数に

                Log.d("date", "" + attendance.date);
                Log.d("period", "" + attendance.period);
                Log.d("status", "" + attendance.status);

                // その日の科目を習得してadapterに追加
                Kamoku kamoku = Kamoku.load(Kamoku.class, attendance.kamokuId);
                kamokus.add(kamoku);
                Log.d("name", kamoku.name);
            }

            adapter.addAll(kamokus);
        }
    }

    public void addTerm(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View parent = getLayoutInflater().inflate(R.layout.dialog_term_edit, (ViewGroup) findViewById(R.id.layout_Dialog));
        parent.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));

        final EditText editTerm = (EditText) parent.findViewById(R.id.editTermName);
        final EditText editYear1 = (EditText) parent.findViewById(R.id.year1);
        final EditText editYear2 = (EditText) parent.findViewById(R.id.year2);
        Button saveTerm = (Button) parent.findViewById(R.id.saveButton);
        Button editSubject = (Button) parent.findViewById(R.id.editButton);

        final Spinner spinnerMonth1 = (Spinner) parent.findViewById(R.id.spinner2);
        final Spinner spinnerDate1 = (Spinner) parent.findViewById(R.id.spinner3);

        final Spinner spinnerMonth2 = (Spinner) parent.findViewById(R.id.spinner5);
        final Spinner spinnerDate2 = (Spinner) parent.findViewById(R.id.spinner6);
        builder.setView(parent);
        final AlertDialog dialog = builder.show();


        List<String> months = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            months.add((i + 1) + "");
        }
        List<String> dates = new ArrayList<String>();
        for (int i = 0; i < 31; i++) {
            dates.add((i + 1) + "");
        }
        ArrayAdapter<String> spAdapterM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        spAdapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth1.setAdapter(spAdapterM);
        spinnerMonth2.setAdapter(spAdapterM);
        ArrayAdapter<String> spAdapterD = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dates);
        spAdapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate1.setAdapter(spAdapterD);
        spinnerDate2.setAdapter(spAdapterD);
        editSubject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String termName = editTerm.getText().toString();
                Term term = new Term();
                term.name=termName;
                term.save();
                long termId = term.getId();

                load(termName);
                Intent intent = new Intent(DayAttendanceActivity.this, EditActivity.class);
                intent.putExtra("タームの生成", termId);
                startActivityForResult(intent, 1);
            }
        });
        saveTerm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateStert,dateEnd;
                Term term = Term.get(editTerm.getText().toString());
                String year1 = editYear1.getText().toString();
                long month1 = spinnerMonth1.getSelectedItemId() + 1;
                long date1 = spinnerDate1.getSelectedItemId() + 1;


                String year2 = editYear2.getText().toString();
                long month2 = spinnerMonth2.getSelectedItemId() + 1;
                long date2 = spinnerDate2.getSelectedItemId() + 1;
                dateStert=String.format("%1$s%2$02d%3$02d",year1,month1,date1);
                dateEnd=String.format("%1$s%2$02d%3$02d",year2,month2,date2);
                term.dateStert=dateStert;
                term.dateEnd=dateEnd;
                term.save();
                ProgressDialog asyncTskDialog = new ProgressDialog(getApplicationContext());
                asyncTskDialog.setTitle("時間割のよみこみをしています。");
                asyncTskDialog.setMessage("保存中…");
                asyncTskDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //asyncTskDialog.setMax(100);
                //asyncTskDialog.setProgress(0);
                asyncTskDialog.show();
                new AttendanceAsyncTask(getApplicationContext(), term).execute("");

            }
        });

    }

    /**
     * Twitterに投稿
     */
    public void Tweet(View v) {

        // 投稿する文章を作成
        String tweetText = calendar.get(Calendar.YEAR) + "年" + ((calendar.get(Calendar.MONTH) + 1)) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                + new SimpleDateFormat("EEEE", Locale.JAPAN).format(calendar.getTime());
        System.out.println(kamokus.size() + ": size");
        for (int i = 0; i < kamokus.size(); i++) {
            // i番目のsubjectを取得
            Kamoku kamoku = kamokus.get(i);

            tweetText = tweetText + "\n" + (i + 1) + ":" + kamoku.name;
        }
        Twitter.tweet(this, tweetText + "\nさぼらないでね！！");
    }

    public void load(String termName) {
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

                    // new Subject(name, dayOfWeek, period);
                    if (Term.get(termName)!=null && Subject.get((i + 1), period, Term.get(termName).getId()) != null) {

                    } else {
                        Subject subject = new Subject(); // 新しいSubjectを作成
                        subject.name = kamoku.name; // ファイルから取得した科目名をセット
                        subject.dayOfWeek = i + 1; // 曜日をセット
                        subject.period = period; // 時限をセット
                        subject.kamokuId = kamoku.getId(); // 科目のIDをセット
                        subject.termId = Term.get(termName).getId();
                        subject.save(); // Subjectの保存
                    }

                }

                // 次の時限に進む
                period++;
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

    }


    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }
}


