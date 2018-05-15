package in.ajm.sb.api.model.schooladmin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Classes extends RealmObject {

    @PrimaryKey
    @SerializedName("className")
    @Expose
    String className;

    @SerializedName("classImage")
    @Expose
    String classImage;

    @SerializedName("school_id")
    @Expose
    String schoolId;



    private RealmList<Sections> sections;

    public RealmList<Sections> getSections() {
        return sections;
    }

    public void setSections(RealmList<Sections> sections) {
        this.sections = sections;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassImage() {
        return classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }

    public static Classes getByClassName(String className) {
        return Realm.getDefaultInstance().where(Classes.class).equalTo("className", className).findFirst();
    }



    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
