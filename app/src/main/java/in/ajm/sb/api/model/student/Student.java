package in.ajm.sb.api.model.student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Student extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    String studentId;

    @SerializedName("email")
    @Expose
    String studentEmail;

    @SerializedName("created_at")
    @Expose
    String createdAt;

    @SerializedName("updated_at")
    @Expose
    String updatedAt;

    @SerializedName("admin_id")
    @Expose
    String adminId;

    @SerializedName("authentication_token")
    @Expose
    String authenticationToken;

    @SerializedName("last_access_time")
    @Expose
    String lastAccessTime;
}
