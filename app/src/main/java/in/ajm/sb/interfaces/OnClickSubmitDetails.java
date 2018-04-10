package in.ajm.sb.interfaces;

/**
 * Created by ajm on 26/03/18.
 */

public interface OnClickSubmitDetails {

    void onClickSubmitStudentParent(String enrollmentNumber, String aadharNumber, int userType);
    void onClickSubmitTeacher(String teacherCode, String aadharNumber);
    void onClickSubmitSchoolAdmin(String schoolPin, String aadharNumber);

}
