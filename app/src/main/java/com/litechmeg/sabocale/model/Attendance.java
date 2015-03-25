package com.litechmeg.sabocale.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Attendance")
public class Attendance extends Model {
	@Column(name = "kamokuId")
	public long kamokuId;
	@Column(name = "date")
	public String date;
	@Column(name = "period")
	public int period;
	// 0 = 出席, 1 = 欠席, 2 = 遅刻, 3 = まだ, 4 =休講
	@Column(name = "status")
	public int status;
    @Column(name = "term")
    public long termId;



    public static Attendance get(String date, int period,long termId) {

		// TODO 自動生成されたメソッド・スタブ
		return new Select().from(Attendance.class).where("date=? and period=? and termId=?", date, period,termId).executeSingle();
	}

	public static List<Attendance> getAll(String date) {
		// TODO 自動生成されたメソッド・スタブ
		return new Select().from(Attendance.class).where("date=?", date).execute();
	}

	public static List<Attendance> daleteAll() {
		return new Delete().from(Attendance.class).execute();
	}

	public static List<Attendance> get(String dateStart, String dateEnd,long termId) {
		return new Select().from(Attendance.class).where("date>=? and date<=? and termId=?", dateStart, dateEnd,termId).execute();
	}

	public static List<Attendance> get(String date, long Id,long termId) {
		return new Select().from(Attendance.class).where("date=? and kamokuId=? and termId=?", date, Id,termId).execute();
	}

	public static List<Attendance> get(long Id,long termId) {
		return new Select().from(Attendance.class).where("kamokuId=? and termId=?", Id,termId).execute();
	}
    public static List<Attendance> getTerm(long Id) {
        return  new Select().from(Attendance.class).where("termId=?", Id).execute();
    }
}
