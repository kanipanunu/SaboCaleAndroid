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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public static List<Subject> getAll() {
		return new Select().from(Subject.class).execute();
	}

	public static List<Subject> getAll(int dayOfWeek) {
		return new Select().from(Subject.class).where("dayOfWeek = ?", dayOfWeek).execute();
	}

	public static void deleteAll() {
		List<Subject> subjects = new Select().from(Subject.class).execute();
		for (int i = 0; i < subjects.size(); i++) {
			subjects.get(i).delete();
		}
	}
}
