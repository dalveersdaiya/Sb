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

    @SerializedName("admission_no")
    @Expose
    String admissionNum;

    @SerializedName("sr_no")
    @Expose
    String serialNum;

    @SerializedName("class_roll_no")
    @Expose
    String classRollNum;

    @SerializedName("admission_date")
    @Expose
    String admissionDate;

    @SerializedName("first_name")
    @Expose
    String firstName;

    @SerializedName("middle_name")
    @Expose
    String middleName;

    @SerializedName("last_name")
    @Expose
    String lastName;

    @SerializedName("batch_id")
    @Expose
    String batchId;

    @SerializedName("date_of_birth")
    @Expose
    String dateOfBirth;

    @SerializedName("gender")
    @Expose
    String gender;

    @SerializedName("blood_group")
    @Expose
    String bloodGroup;

    @SerializedName("birth_place")
    @Expose
    String birthPlace;

    @SerializedName("nationality_id")
    @Expose
    String nationalityId;

    @SerializedName("language")
    @Expose
    String language;

    @SerializedName("religion")
    @Expose
    String religion;

    @SerializedName("student_category_id")
    @Expose
    String studentCategoryId;

    @SerializedName("address_line1")
    @Expose
    String addressLine1;

    @SerializedName("address_line2")
    @Expose
    String addressLine2;

    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("state")
    @Expose
    String state;

    @SerializedName("pin_code")
    @Expose
    String pinCode;

    @SerializedName("country_id")
    @Expose
    String countryId;

    @SerializedName("phone1")
    @Expose
    String phone1;

    @SerializedName("phone2")
    @Expose
    String phone2;

    @SerializedName("immediate_contact_id")
    @Expose
    String immediateContactId;

    @SerializedName("is_sms_enabled")
    @Expose
    String isSmsEnabled;

    @SerializedName("status_description")
    @Expose
    String statusDescription;

    @SerializedName("is_active")
    @Expose
    String isActive;

    @SerializedName("is_deleted")
    @Expose
    String isDeleted;

    @SerializedName("enrollment_date")
    @Expose
    String enrollmentDate;
}
