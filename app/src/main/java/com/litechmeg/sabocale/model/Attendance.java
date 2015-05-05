package com.litechmeg.sabocale.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
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
    // 0 = 出席, 1 = 欠席, 2 = 遅刻, 3 = まだ, 4 =休講
    @Column(name = "status")
    public int status;
    @Column(name = "term")
    public long termId;

    public static final int
            STATUS_ATTENDANCE = 0,
            STATUS_ABSENT = 1,
            STATUS_LATE = 2,
            STATUS_MADA = 3,
            STATUS_KYUKO = 4;


    public static Attendance get(long date, int period, long termId) {

        return new Select().from(Attendance.class).where("date=? and period=? and term=?", date, period, termId).executeSingle();
    }

    public static List<Attendance> getAll(long date, long termId) {
        return new Select().from(Attendance.class).where("date=? and term=?", date, termId).execute();
    }

    public static List<Attendance> getAll(long date) {
        return new Select().from(Attendance.class).where("date=?", date).execute();
    }

    public static List<Attendance> daleteAll() {
        return new Delete().from(Attendance.class).execute();
    }

    public static List<Attendance> getTerm(long dateStart, long dateEnd, long termId) {
        return new Select().from(Attendance.class).where("date>=? and date<=? and term=?", dateStart, dateEnd, termId).execute();
    }

    public static List<Attendance> get(long date, long Id, long termId) {
        return new Select().from(Attendance.class).where("date=? and kamokuId=? and term=?", date, Id, termId).execute();
    }

    public static List<Attendance> getList(long kamokuId, long termId) {
        return new Select().from(Attendance.class).where("kamokuId=? and term=?", kamokuId, termId).execute();
    }

    public static List<Attendance> getTerm(long Id) {
        return new Select().from(Attendance.class).where("term=?", Id).execute();
    }

    public static List<Attendance> getAll() {
        return new Select().from(Attendance.class).execute();
    }

    public String toLog() {
        return "termId: " + termId + "\n" +
                "kamokuId: " + kamokuId + "\n" +
                "date: " + date + "\n" +
                "period: " + period + "\n" +
                "status: " + status;
    }
}
