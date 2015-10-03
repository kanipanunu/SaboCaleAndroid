package com.litechmeg.sabocale.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Attendance")
public class Attendance extends Model {

    @Column(name = "kamokuId")
    public long kamokuId;

    @Column(name = "date")
    public long date;

    @Column(name = "period")
    public int period;

    @Column(name = "status")
    public int status;
    // 0 = 出席, 1 = 欠席, 2 = 遅刻, 3 = まだ, 4 =休講
    public static final int
            STATUS_ATTENDANCE = 0,
            STATUS_ABSENT = 1,
            STATUS_LATE = 2,
            STATUS_MADA = 3,
            STATUS_KYUKO = 4;

    @Column(name = "term")
    public long termId;

    public Attendance(){
        super();
    }

    public static Attendance get(long date, int period, long termId) {
        return query().where("date=? and period=? and term=?", date, period, termId).executeSingle();
    }

    public static List<Attendance> get(long date, long kamokuId, long termId) {
        return query().where("date=? and kamokuId=? and term=?", date, kamokuId, termId).execute();
    }

    public static List<Attendance> getAll() {
        return query().execute();
    }

    public static List<Attendance> getAll(long date) {
        return query().where("date=?", date).execute();
    }

    public static List<Attendance> getAll(long date, long termId) {
        return query().where("date=? and term=?", date, termId).execute();
    }

    public static List<Attendance> getList(long kamokuId, long termId) {
        return query().where("kamokuId=? and term=?", kamokuId, termId).execute();
    }

    public static From query(){
        return new Select().from(Attendance.class);
    }

    public String toLog() {
        return "termId: " + termId + "\n" +
                "kamokuId: " + kamokuId + "\n" +
                "date: " + date + "\n" +
                "period: " + period + "\n" +
                "status: " + status;
    }
}
