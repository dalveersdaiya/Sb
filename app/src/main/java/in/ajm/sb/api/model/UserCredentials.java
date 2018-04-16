package in.ajm.sb.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserCredentials extends RealmObject {

    @SerializedName("userName")
    @Expose
    String userName;

    @PrimaryKey
    @SerializedName("userId")
    @Expose
    String userId;

    @SerializedName("userType")
    @Expose
    String userType;

    @SerializedName("isSelected")
    @Expose
    boolean isSelected;

    @SerializedName("userImage")
    @Expose
    String userImage;

    public static UserCredentials getByUserId(String userId) {
        return Realm.getDefaultInstance().where(UserCredentials.class).equalTo("userId", userId).findFirst();
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
