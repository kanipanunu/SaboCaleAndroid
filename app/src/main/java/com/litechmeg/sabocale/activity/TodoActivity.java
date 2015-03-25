package com.litechmeg.sabocale.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.litechmeg.sabocale.R;

/**
 * TODO
 * todo画面(未完)
 */
public class TodoActivity extends Activity {

    public static final String ToDo_KEY = "ToDo_KEY";
    private static final String SHAREPRE_KEY = "MEGU";
    private static final String LENGH = null;

    SharedPreferences pref;

    private ListView listView;
    private EditText editText;
    private Button button;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        pref = getSharedPreferences(TodoActivity.SHAREPRE_KEY, Activity.MODE_PRIVATE);
        Editor editor = pref.edit();

        // ToDoArray = pref.getInt(MainActivity."TODO" + i, adapter.getItem(i);

        listView = (ListView) findViewById(R.id.listView1);
        editText = (EditText) findViewById(R.id.editText1);
        button = (Button) findViewById(R.id.todo);

        // ListViewにAdapterをセット
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                adapter.remove(adapter.getItem(arg2));
            }

        });

        for (int i = 0; i < pref.getInt("LENGH", 0); i++) {
            // getString("TODO" + i, adapter.getItem(i));
            String text = pref.getString("TODO" + i, "なかった");
            adapter.add(text);
        }

    }

    public void addItem(View v) {
        String text = editText.getText().toString();

        adapter.add(text);
        Editor editor = pref.edit();
        /*
		 * editor.putString(MainActivity, 5); editor.commit(); Editor editor =
		 * sp.edit();
		 */
        for (int i = 0; i < adapter.getCount(); i++) {
            editor.putString("TODO" + i, adapter.getItem(i));
        }

        editor.putInt("LENGH", adapter.getCount());

        editor.commit();
        editText.setText("");
    }
}
