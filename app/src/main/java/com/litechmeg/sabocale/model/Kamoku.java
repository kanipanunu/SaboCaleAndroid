package com.litechmeg.sabocale.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Kamoku")
public class Kamoku extends Model {
    @Column(name = "name")
    public String name;
    @Column(name = "TermId")
    public long termId;

    public int absenceCount = 0;
    public int attend = 0;
    public int late = 0;
    public int kyuko = 0;
    public int size = 0;

    public Kamoku() {
        super();
    }

    public Kamoku(String name) {
        this.name = name;
    }

    public Kamoku(String name, long termId) {
        this.name = name;
        this.termId = termId;
    }

    public static List<Kamoku> getAll() {
        List<Kamoku> kamokus = new Select().from(Kamoku.class).execute();
        return kamokus;
    }

    public static List<Kamoku> getAll(Long termId) {
        List<Kamoku> kamokus = new Select().from(Kamoku.class).where("termId=?,termId").execute();
        return kamokus;
    }

    public static Kamoku get(String name) {// 以下のnameは引数。
        // TODO 自動生成されたメソッド・スタブ
        return (Kamoku) new Select().from(Kamoku.class).where("name=?", name).executeSingle();
    }

    public void calculate() {
        List<Attendance> attendances = Attendance.get(getId(), 0);//後で変数にする。

        absenceCount = 0;
        attend = 0;
        late = 0;
        kyuko = 0;

        for (int i = 0; i < size; i++) {
            if (attendances.get(i).status == 0) {
                attend++;
            } else if (attendances.get(i).status == 1) {
                absenceCount++;
            } else if (attendances.get(i).status == 2) {
                late++;
            } else if (attendances.get(i).status == 4) {
                kyuko++;
            }
        }
    }


}
