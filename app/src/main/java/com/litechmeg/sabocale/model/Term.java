package com.litechmeg.sabocale.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

/**
 * 授業の期間を管理するためのモデル
 */
@Table(name = "Term")
public class Term extends Model {

    @Column(name = "name")
    public String name;

    @Column(name = "dateStart")
    public long dateStart;
    
    @Column(name = "dateEnd")
    public long dateEnd;

    @Column(name = "dayOfWeek")
    public int dayOfWeek;


    public Term() {
        super();
    }

    public Term(String termName) {
        super();
        name = termName;
    }

    public static Term get(long id) {
        return Model.load(Term.class, id);
    }

    public static List<Term> getAll() {
        return query().execute();
    }

    public static Term get(String name) {
        return query().where("name=?", name).executeSingle();
    }

    public static From query(){
        return new Select().from(Term.class);
    }
}