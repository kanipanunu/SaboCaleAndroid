package com.litechmeg.sabocale.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Term;

/**
 * Created by megukanipan on 2015/03/26.
 */
public class TermListArrayAdapter extends ArrayAdapter<Term>{
    SharedPreferences pref;

    LayoutInflater layoutInflater_;
    public TermListArrayAdapter(Context context,int resourse){
        super(context, resourse);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override

        public View getView(final int position, View convertView, ViewGroup parent) {
        // layoutInflaterでリソースからViewを生成
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.list_item_term, null);
        }

        final Term term = getItem(position);
        pref = getContext().getSharedPreferences("TermSelect", Context.MODE_PRIVATE);
        //
        if(term.getId()==pref.getLong("TermId", 1)){
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.primary));
        }else {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.primary_light));
        }

        TextView termName=(TextView)convertView.findViewById(R.id.termname);
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
        termName.setText(term.name);
//        termName.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });
        return convertView;
    }
}
