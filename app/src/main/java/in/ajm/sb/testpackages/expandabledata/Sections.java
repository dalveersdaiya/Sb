package in.ajm.sb.testpackages.expandabledata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsd on 2016/04/02.
 */

public class Sections implements Parcelable {

    private String className;
    private String sectionName;
    private int noOfStudents;
    List<StudentsList> studentsLists = new ArrayList<>();

    public Sections() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getNoOfStudents() {
        return noOfStudents;
    }

    public void setNoOfStudents(int noOfStudents) {
        this.noOfStudents = noOfStudents;
    }

    public List<StudentsList> getStudentsLists() {
        return studentsLists;
    }

    public void setStudentsLists(List<StudentsList> studentsLists) {
        this.studentsLists = studentsLists;
    }

    protected Sections(Parcel in) {
        className = in.readString();
        sectionName = in.readString();
        noOfStudents = in.readInt();
        studentsLists = in.createTypedArrayList(StudentsList.CREATOR);
    }

    public static final Creator<Sections> CREATOR = new Creator<Sections>() {
        @Override
        public Sections createFromParcel(Parcel in) {
            return new Sections(in);
        }

        @Override
        public Sections[] newArray(int size) {
            return new Sections[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeString(sectionName);
        dest.writeInt(noOfStudents);
        dest.writeTypedList(studentsLists);
    }
}
