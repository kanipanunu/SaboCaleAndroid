package com.litechmeg.sabocale.util;

import android.content.Context;
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
