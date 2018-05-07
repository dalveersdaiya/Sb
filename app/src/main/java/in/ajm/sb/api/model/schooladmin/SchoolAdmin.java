package in.ajm.sb.api.model.schooladmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SchoolAdmin extends RealmObject {


    @PrimaryKey
    @SerializedName("id")
    @Expose
    String studentId = "";

    @SerializedName("email")
    @Expose
    String studentEmail= "";

    @SerializedName("created_at")
    @Expose
    String createdAt= "";

    @SerializedName("updated_at")
    @Expose
    String updatedAt= "";

    @SerializedName("fname")
    @Expose
    String fName= "";

    @SerializedName("lname")
    @Expose
    String lName= "";

    @SerializedName("gender")
    @Expose
    String gender= "";

    @SerializedName("dob")
    @Expose
    String dob= "";

    @SerializedName("is_married")
    @Expose
    String isMarried= "";

    @SerializedName("main_mobile")
    @Expose
    String mainMobile= "";

    @SerializedName("other_contact")
    @Expose
    String otherContact= "";

    @SerializedName("address")
    @Expose
    String address= "";

    @SerializedName("city")
    @Expose
    String city= "";

    @SerializedName("state")
    @Expose
    String state= "";

    @SerializedName("zip")
    @Expose
    String zip= "";

    @SerializedName("personal_email")
    @Expose
    String personalEmail= "";

    @SerializedName("facebook_link")
    @Expose
    String facebookLink= "";

    @SerializedName("twitter_link")
    @Expose
    String twitterLink= "";

    @SerializedName("linkedin_link")
    @Expose
    String linkedinLink= "";

    @SerializedName("instagram_link")
    @Expose
    String instagramLink= "";

    @SerializedName("pan_no")
    @Expose
    String pan_no= "";

    @SerializedName("addhar_no")
    @Expose
    String aadharNum;

    @SerializedName("inistitute")
    @Expose
    String inistitute= "";

    @SerializedName("academic_status")
    @Expose
    String academic_status= "";

    @SerializedName("addhar_no")
    @Expose
    String addhar_no= "";

    @SerializedName("roles")
    @Expose
    String roles= "";

    @SerializedName("permission")
    @Expose
    String permission= "";

    @SerializedName("last_access_time")
    @Expose
    String last_access_time= "";


    @SerializedName("authentication_token")
    @Expose
    String authentication_token= "";


}
