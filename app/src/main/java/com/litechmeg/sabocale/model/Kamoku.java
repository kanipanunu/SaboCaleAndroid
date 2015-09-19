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
        return new Select().from(Kamoku.class).execute();
    }

    public static List<Kamoku> getAll(Long termId) {
        return new Select().from(Kamoku.class).where("TermId=?", termId).execute();
    }
    public static Kamoku get(String name) {
        return new Select().from(Kamoku.class).where("name=?", name).executeSingle();
    }
    public static Kamoku get(long Id) {
        return new Select().from(Kamoku.class).where("id=?", Id).executeSingle();
    }
    public void calculate(long termId) {
        List<Attendance> attendances = Attendance.getList(getId(), termId);

        absenceCount = 0;
        attend = 0;
        late = 0;
        kyuko = 0;

        for (int i = 0; i < size; i++) {

            if (attendances.get(i).status == Attendance.STATUS_ATTENDANCE) {
                attend++;
            } else if (attendances.get(i).status == Attendance.STATUS_ABSENT) {
                absenceCount++;
            } else if (attendances.get(i).status == Attendance.STATUS_LATE) {
                late++;
            } else if (attendances.get(i).status == Attendance.STATUS_KYUKO) {
                kyuko++;
            }
        }
    }


}
