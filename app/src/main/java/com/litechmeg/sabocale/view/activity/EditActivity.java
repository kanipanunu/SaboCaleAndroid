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
import com.litechmeg.sabocale.util.EditListArrayAdapter;

import java.util.List;

public class EditActivity extends ActionBarActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	// あだぷだー
	static ListView listView;
	/**
	 * The {@link android.support.v4.view.ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
    SharedPreferences pref;

    Term term;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
        pref=getSharedPreferences("TermSellect",MODE_PRIVATE);
        Intent intent=getIntent();
        long Id=intent.getLongExtra("タームの生成",0);
        term=Term.get(Id);

        // Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
        try{
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }catch (Exception e){

        };


		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    /**
	 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
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
            args.putLong(DummySectionFragment.ARG_TERM_ID,term.getId());
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

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

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
					parent = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_subject_edit, null);
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
							// TODO 自動生成されたメソッド・スタブ
							subject.name = EditKamokuName.getText().toString();
							if (Kamoku.get(subject.name) != null) {
							} else {
								Kamoku kamoku = new Kamoku();
								kamoku.name = subject.name;
                                kamoku.termId=subject.termId;
								kamoku.save();
							}
							subject.kamokuId = (long) Kamoku.get(subject.name).getId();
							subject.save();
						}
					});
					arikoma.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							subject.name = EditKamokuName.getText().toString();
							if (Kamoku.get(subject.name) != null) {
							} else {
								Kamoku kamoku = new Kamoku();
								kamoku.name = subject.name;
								kamoku.save();
							}
							subject.kamokuId = (long) Kamoku.get(subject.name).getId();
							subject.save();
						}

					});
					akikoma.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (Kamoku.get("free") == null) {
								Kamoku kamoku = new Kamoku();
								kamoku.name = "free";
								kamoku.save();
							}
							subject.kamokuId = Kamoku.get("free").getId();
							subject.name = "free";
							subject.save();
						}
					});
				}
			});
		}

		public static final String ARG_SECTION_NUMBER = "section_number";
        public  static final String ARG_TERM_ID="term_Id";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_edit_dummy, container, false);

            Bundle args=getArguments();

            final EditListArrayAdapter adapter = new EditListArrayAdapter(this.getActivity(),
					R.layout.fragment_edit_dummy);
			System.out.println(((Integer) getArguments().get(ARG_SECTION_NUMBER) + 1) + "");
			// 曜日ごとにじかんわりをとってくる。（int dayOfWeek）
			List<Subject> subjects = Subject.getAll((Integer) getArguments().get(ARG_SECTION_NUMBER) + 1,args.getLong(ARG_TERM_ID));
			if (subjects != null) {
				adapter.addAll(subjects);
			}

			setListAdapter(adapter);

			return rootView;
		}
	}
    public void back(View v){
        List<Subject> subjects=Subject.getAll(pref.getLong("TermId",1));
        for(int i=0;i<subjects.size();i++){
            subjects.get(i).save();
        }
        finish();
    }

}
