package com.litechmeg.sabocale.util;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;

import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;

import java.util.Calendar;
import java.util.List;

/**
 * Created by megukanipan on 2015/03/31.
 */
public class AttendanceAsyncTask extends AsyncTask<String, Integer, Long> implements SearchManager.OnCancelListener {

    final String TAG = "MyAsyncTask";
    ProgressDialog dialog;
    Context context;
    Term term;

    public AttendanceAsyncTask(Context context, Term term,ProgressDialog dialog) {
        this.dialog=dialog;
        this.context = context;
        this.term=term;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    public void onCancel() {
    }

    @Override
    protected Long doInBackground(String... params) {

        // その科目の数を取ってくる
        String dateStart;
        String dateEnd = ""; // TODO
        dateStart = term.dateStert;
        dateEnd=term.dateEnd;
        int firstDayOfWeek = term.dayOfWeek;
//        System.out.println(dateStart);
//        System.out.println(dateEnd);
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


            // その日の出席を全部取得して、あったら
            if (Attendance.getAll(Integer.toString((y * 10000) + (m * 100) + (d)),term.getId()).size() != 0) {
                // デバッグ出力
                List<Attendance> attendances = Attendance.getAll(Integer.toString((y * 10000) + (m * 100)
                        + (d)),term.getId());
                for (int i = 0; i < attendances.size(); i++) {
                    final Attendance attendance = Attendance.get(
                            (Integer.toString((y * 10000) + (m * 100) + (d))), i, 0);//後で変数に
                    System.out.println("Attendance #" + i + ", Subject " + subjects.get(i).getName());
                    attendance.kamokuId = subjects.get(i).kamokuId;//時間割subjectを変更した時の更新
                    attendance.save();
                    System.out.println("更新！" + attendance.kamokuId + "ターム" + term.name + " " + term.getId());
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
                    System.out.println("作った！" + attendance.kamokuId + "ターム" + term.name + " " + term.getId());
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
        return null;

    }

    @Override
    protected void onPostExecute(Long result) {
        // TODO 処理が終了したらすること
        super.onPostExecute(result);

        dialog.dismiss();
    }
}
