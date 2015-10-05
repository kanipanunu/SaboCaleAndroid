package com.litechmeg.sabocale.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;
import com.litechmeg.sabocale.util.PrefUtils;
import com.litechmeg.sabocale.view.adapter.EditListArrayAdapter;

import java.util.List;

public class EditActivity extends ActionBarActivity implements ActionBar.TabListener {

    // あだぷだー
    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        long Id = intent.getLongExtra("タームの生成", 0);
        term = Term.get(Id);

        final ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        } catch (Exception e) {

        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        String[] array;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            array = getResources().getStringArray(R.array.title_sections);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
            args.putLong(DummySectionFragment.ARG_TERM_ID, term.getId());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return array[position];
        }
    }

    public static class DummySectionFragment extends ListFragment {
        ListView listView;

        @Override
        public void onStart() {
            super.onStart();

            listView = getListView();

            Button footerButton = new Button(getActivity());
            footerButton.setText("追加");
            listView.addFooterView(footerButton);

            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {//時間割設定の科目入力画面出す。
                    // dialog用のレイアウトを取得
                    parent = View.inflate(getActivity(), R.layout.dialog_subject_edit, null);
                    parent.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));

                    // Viewの関連付け
                    final EditText EditKamokuName = (EditText) parent.findViewById(R.id.editText1);
                    final TextView absenceText = (TextView) parent.findViewById(R.id.absence);
                    final Button saveButton = (Button) parent.findViewById(R.id.saveButton);
                    final Button arikoma = (Button) parent.findViewById(R.id.arikoma);
                    final Button akikoma = (Button) parent.findViewById(R.id.akikoma);

                    // 現在位置のKamokuを取得して名前を表示
                    final Subject subject = (Subject) getListAdapter().getItem(position);
                    EditKamokuName.setText(subject.name);

                    // あらーとダイアログの生成
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder //
                            .setTitle("") // タイトルをセット
                            .setMessage("")// メッセージをセット
                            .setView(parent) // Viewをセット
                            .show(); // アラートダイアログの表示
                    saveButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subject.name = EditKamokuName.getText().toString();
                            if (Kamoku.get(subject.name, subject.termId) != null) {
                            } else {
                                Kamoku kamoku = new Kamoku();
                                kamoku.name = subject.name;
                                kamoku.termId = subject.termId;
                                kamoku.save();
                            }
                            subject.kamokuId = (long) Kamoku.get(subject.name, subject.termId).getId();
                            subject.save();
                        }
                    });
                    arikoma.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subject.name = EditKamokuName.getText().toString();
                            if (Kamoku.get(subject.name, subject.termId) != null) {
                            } else {
                                Kamoku kamoku = new Kamoku();
                                kamoku.name = subject.name;
                                kamoku.save();
                            }
                            subject.kamokuId = (long) Kamoku.get(subject.name, subject.termId).getId();
                            subject.termId = (long) Kamoku.get(subject.name, subject.termId).termId;
                            subject.save();
                        }

                    });
                    akikoma.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Kamoku.get("free", subject.termId) == null) {
                                Kamoku kamoku = new Kamoku();
                                kamoku.name = "free";
                                kamoku.save();
                            }
                            subject.kamokuId = Kamoku.get("free", subject.termId).getId();
                            subject.name = "free";
                            subject.termId = (long) Kamoku.get(subject.name, subject.termId).termId;
                            subject.save();
                        }
                    });
                }
            });
        }

        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_TERM_ID = "term_Id";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_edit_dummy, container, false);

            Bundle args = getArguments();

            final EditListArrayAdapter adapter = new EditListArrayAdapter(this.getActivity(),
                    R.layout.fragment_edit_dummy);
            System.out.println(((Integer) getArguments().get(ARG_SECTION_NUMBER) + 1) + "");
            // 曜日ごとにじかんわりをとってくる。（int dayOfWeek）
            List<Subject> subjects = Subject.getAll((Integer) getArguments().get(ARG_SECTION_NUMBER) + 1, args.getLong(ARG_TERM_ID));
            if (subjects != null) {
                adapter.addAll(subjects);
            }

            setListAdapter(adapter);

            return rootView;
        }
    }

    public void back(View v) {
        List<Subject> subjects = Subject.getAll(PrefUtils.getTermId(this));
        for (int i = 0; i < subjects.size(); i++) {
            subjects.get(i).save();
        }
        finish();
    }

}
