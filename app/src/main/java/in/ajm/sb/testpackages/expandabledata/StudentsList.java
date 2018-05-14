package in.ajm.sb.testpackages.expandabledata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dsd on 2016/04/02.
 */

public class StudentsList implements Parcelable {

    private String studentName;
    private String sectionName;
    private String rollNum;

    public StudentsList() {

    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

    protected StudentsList(Parcel in) {
        studentName = in.readString();
        sectionName = in.readString();
        rollNum = in.readString();
    }

    public static final Creator<StudentsList> CREATOR = new Creator<StudentsList>() {
        @Override
        public StudentsList createFromParcel(Parcel in) {
            return new StudentsList(in);
        }

        @Override
        public StudentsList[] newArray(int size) {
            return new StudentsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentName);
        dest.writeString(sectionName);
        dest.writeString(rollNum);
    }
}
