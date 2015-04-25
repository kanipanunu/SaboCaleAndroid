package com.litechmeg.sabocale.view.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;
import com.litechmeg.sabocale.util.AttendanceAsyncTask;
import com.litechmeg.sabocale.util.TermListArrayAdapter;
import com.litechmeg.sabocale.view.adapter.MainTabPagerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 選択した日の科目を表示する画面
 */
public class DayAttendanceActivity extends ActionBarActivity {
    // TextView(なんのだろう？)
    MainTabPagerAdapter mainTabPagerAdapter;

    ViewPager mViewPager;


    ActionBarDrawerToggle toggle;
    ListView termListView;
    TermListArrayAdapter termAdapter;
    // 日付
    Calendar calendar;
    // リスト(不要？)
    String date;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_attendance);
        pref = getSharedPreferences("TermSellect", MODE_PRIVATE);

        /**
         * テスト
         */


        // Viewを関連付け
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, layout, R.string.terms, R.string.terms);
        toggle.setDrawerIndicatorEnabled(true);
        layout.setDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        } catch (Exception e) {
        }
        getSupportActionBar().getTitle();

        mainTabPagerAdapter = new MainTabPagerAdapter(
                getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mainTabPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                try{
                    getSupportActionBar().setSelectedNavigationItem(position);
                }catch (Exception e){

                }
            }
        });
        // getCountでタブの数を指定。
        for (int i = 0; i < mainTabPagerAdapter.getCount(); i++) {
            // Actionbarにタブを追加。
            // getPageTitleでタブのタイトルを表示
            getSupportActionBar().addTab(getSupportActionBar().newTab()
                    .setText(mainTabPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }


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


        // ListViewにAdapterをセット
        termAdapter = new TermListArrayAdapter(this, R.layout.activity_kamoku_list);
        List<Term> terms = Term.getAll();
        for (int i = 0; i < terms.size(); i++) {
            termAdapter.add(terms.get(i));
        }
        termListView = (ListView) findViewById(R.id.navigationDrawer);
        termListView.setAdapter(termAdapter);
        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term term = termAdapter.getItem(position);
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong("TermId", term.getId());
                editor.apply();
            }
        });
        termListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Term term = termAdapter.getItem(position);
                term.delete();
                termAdapter.remove(term);
                termListView.setAdapter(termAdapter);
                return true;
            }
        });
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

                Term term = Term.get(termName);
                if (term == null) {
                    term = new Term();
                    term.name = termName;
                    term.save();
                }

                long termId = term.getId();

                load(term);
                Intent intent = new Intent(DayAttendanceActivity.this, EditActivity.class);
                intent.putExtra("タームの生成", termId);
                startActivityForResult(intent, 1);
            }
        });
        saveTerm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateStert, dateEnd;
                Term term = Term.get(editTerm.getText().toString());
                String year1 = editYear1.getText().toString();
                long month1 = spinnerMonth1.getSelectedItemId() + 1;
                long date1 = spinnerDate1.getSelectedItemId() + 1;


                String year2 = editYear2.getText().toString();
                long month2 = spinnerMonth2.getSelectedItemId() + 1;
                long date2 = spinnerDate2.getSelectedItemId() + 1;
                dateStert = String.format("%1$s%2$02d%3$02d", year1, month1, date1);
                dateEnd = String.format("%1$s%2$02d%3$02d", year2, month2, date2);
                term.dateStert = dateStert;
                term.dateEnd = dateEnd;
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

    public void load(Term term) {
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
                            kamoku.termId = term.getId();
                            kamoku.save();
                        }
                        Log.d(kamoku.name, jikan[i]);
                    } else {
                        kamoku = Kamoku.get("free");
                        if (kamoku == null) {
                            kamoku = new Kamoku();
                            kamoku.name = "free";
                            kamoku.termId = term.getId();
                            kamoku.save();
                        }
                    }

                    // new Subject(name, dayOfWeek, period);
                    Subject s = Subject.get(i + 1, period, term.getId());

                    if (s != null) {
                    } // TODO
                    else {
                        Subject subject = new Subject(); // 新しいSubjectを作成
                        subject.name = kamoku.name; // ファイルから取得した科目名をセット
                        subject.dayOfWeek = i + 1; // 曜日をセット
                        subject.period = period; // 時限をセット
                        subject.kamokuId = kamoku.getId(); // 科目のIDをセット
                        subject.termId = term.getId();
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

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // ここで表示するフラグメントを決定する
        // setCurrentItem で、下記の SectionPagerAdapter の getItem を呼び出し
        mViewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * タブの選択が外れた場合の処理
     */
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * タブが2度目以降に選択された場合の処理
     */

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
