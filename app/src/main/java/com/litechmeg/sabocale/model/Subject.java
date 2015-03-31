package com.litechmeg.sabocale.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Subject")
public class Subject extends Model {
	@Column(name = "kamokuId")
	public long kamokuId;
	@Column(name = "name")
	public String name;
	@Column(name = "dayOfWeek")
	public int dayOfWeek;
	@Column(name = "period")
	public int period;
	@Column(name = "status")
	public int status;
    @Column(name = "TermId")
    public long termId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Subject> getAll(long termId) {
		return new Select().from(Subject.class).where("termId=?",termId).execute();
	}

	public static List<Subject> getAll(int dayOfWeek,long termId) {
		return new Select().from(Subject.class).where("dayOfWeek = ? and termId=?", dayOfWeek,termId).execute();
	}
    public static Subject get(int dayOfWeek,int period, long termId){
        return new Select().from(Subject.class).where("dayOfWeek = ? and period = ? and termId = ?",dayOfWeek,period,termId).executeSingle();
    }

	public static void deleteAll() {
		List<Subject> subjects = new Select().from(Subject.class).execute();
		for (int i = 0; i < subjects.size(); i++) {
			subjects.get(i).delete();
		}
	}
}
