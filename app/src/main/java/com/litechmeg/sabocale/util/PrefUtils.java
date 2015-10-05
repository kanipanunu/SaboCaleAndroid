package com.litechmeg.sabocale.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fukuo on 2015/10/06.
 */
public class PrefUtils {

    public static final String PREF_NAME = "TERM_SELECT";
    public static final String PREF_KEY_TERM_ID = "TERM_ID";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void setTermId(Context context, long termId){
        getEditor(context)
                .putLong(PREF_KEY_TERM_ID, termId)
                .commit();
    }

    public static int getTermId(Context context){
        return getSharedPreferences(context)
                .getInt(PREF_KEY_TERM_ID, -1);
    }
}
