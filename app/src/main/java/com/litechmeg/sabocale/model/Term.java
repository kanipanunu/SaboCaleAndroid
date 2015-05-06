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
    @Column(name = "dateStart")
    public long dateStert;
    @Column(name = "dateEnd")
    public long dateEnd;
    @Column(name = "dayOfWeek")
    public int dayOfWeek;


    public Term() {
        super();
    }

    public Term(String termName){
        super();
        name=termName;
    }

    public static Term get(long id) {
        return new Select().from(Term.class).where("id=?", id).executeSingle();
    }

    public static List<Term> getAll() {
        return new Select().from(Term.class).execute();
    }

    public static Term get(String name) {
        return new Select().from(Term.class).where("name=?", name).executeSingle();
    }
}