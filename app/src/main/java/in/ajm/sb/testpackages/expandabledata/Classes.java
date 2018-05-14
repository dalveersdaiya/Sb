package in.ajm.sb.testpackages.expandabledata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Classes implements Parcelable{

    String className;
    String noOfSections;
    String classId;
    List<Sections> sectionsList;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNoOfSections() {
        return noOfSections;
    }

    public void setNoOfSections(String noOfSections) {
        this.noOfSections = noOfSections;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(List<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public Classes(){

    }

    protected Classes(Parcel in) {
        className = in.readString();
        noOfSections = in.readString();
        classId = in.readString();
        sectionsList = in.createTypedArrayList(Sections.CREATOR);
    }

    public static final Creator<Classes> CREATOR = new Creator<Classes>() {
        @Override
        public Classes createFromParcel(Parcel in) {
            return new Classes(in);
        }

        @Override
        public Classes[] newArray(int size) {
            return new Classes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeString(noOfSections);
        dest.writeString(classId);
        dest.writeTypedList(sectionsList);
    }
}
