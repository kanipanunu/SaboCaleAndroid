package com.litechmeg.sabocale.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Term")
public class Term extends Model {
	@Column(name = "name")
	public String name;
	@Column(name = "date")
	// 0=start,1=end
	public String date;
	@Column(name = "dayOfWeek")
	public int dayOfWeek;
	@Column(name = "SE")
	// 0=start,1=end
	public int se;

	public static Term get(int se) {
		return new Select().from(Term.class).where("SE=?", se).executeSingle();
	}

	public static List<Term> getAll() {
		return new Select().from(Term.class).execute();
	}

    public static List<Term> get(String name) {
        return  new Select().from(Term.class).where("name=?", name).execute();
    }
}