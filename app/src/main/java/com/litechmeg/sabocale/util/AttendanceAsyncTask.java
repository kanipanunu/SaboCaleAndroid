package com.litechmeg.sabocale.util;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;

import java.util.Calendar;
import java.util.List;

/**
 * Created by megukanipan on 2015/03/31.
 */
public class AttendanceAsyncTask extends AsyncTask<String, Integer, List<Attendance>> implements SearchManager.OnCancelListener {

    final String TAG = "MyAsyncTask";
    ProgressDialog dialog;
    Context context;
    Term term;

    public AttendanceAsyncTask(Context context, Term term, ProgressDialog dialog) {
        this.dialog = dialog;
        this.context = context;
        this.term = term;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    public void onCancel() {
    }

    @Override
    protected List<Attendance> doInBackground(String... params) {
        //関連づけ
        long dateStart = term.dateStert;
        long dateEnd = term.dateEnd;
        int firstDayOfWeek = term.dayOfWeek;

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(dateStart);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(dateEnd);

        dialog.setMax((int)(dateEnd - dateStart));

        while (startCalendar.after(endCalendar)) {
            // ダイアログを更新
            dialog.setProgress((int)(startCalendar.getTimeInMillis() - dateStart));

            long date = startCalendar.getTimeInMillis();
            List<Subject> subjects = Subject.getAll(startCalendar.get(Calendar.DAY_OF_WEEK), term.getId());

            // その日の出席を全部取得して、あったら
            List<Attendance> attendances = Attendance.getAll(date, term.getId());
            if (attendances != null && attendances.size() > 0) {
                for (int period = 0, size = attendances.size(); period < size; period++) {
                    final Attendance attendance = Attendance.get(date, period, term.getId());
                    attendance.kamokuId = subjects.get(period).kamokuId;//時間割subjectを変更した時の更新
                    attendance.save();
                }
            } else {// 無かったらつくる。※Subject参照
                for (int period = 0; period < subjects.size(); period++) {
                    Attendance attendance = new Attendance();

                    attendance.termId = term.getId();
                    attendance.date = date;
                    attendance.period = period;
                    attendance.kamokuId = subjects.get(period).kamokuId;

                    Calendar now = Calendar.getInstance();

                    if (now.after(startCalendar)) {
                        attendance.status = 0;
                    } else {
                        attendance.status = 3;
                    }

                    attendance.save();
                }
            }

            startCalendar.add(Calendar.DATE, 1);
        }
        return Attendance.getAll();
    }

    @Override
    protected void onPostExecute(List<Attendance> result) {
        super.onPostExecute(result);

        dialog.dismiss();

        for(Attendance a: result){
            System.out.println("a: " + a.toLog());
        }
    }

}
