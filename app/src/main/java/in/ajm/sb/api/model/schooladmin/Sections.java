package in.ajm.sb.api.model.schooladmin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sections extends RealmObject {

    @PrimaryKey
    @SerializedName("sectionName")
    @Expose
    String sectionName;


    public static Sections getBySectionName(String sectionName) {
        return Realm.getDefaultInstance().where(Sections.class).equalTo("sectionName", sectionName).findFirst();
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
