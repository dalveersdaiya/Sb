package in.ajm.sb.data;

/**
 * Created by DSD on 26/03/18.
 */

public class User {
    String userName;
    String userId;
    boolean isSelected;
    String firstName;
    String lastName;
    String mobileumber;
    int userType;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileumber() {
        return mobileumber;
    }

    public void setMobileumber(String mobileumber) {
        this.mobileumber = mobileumber;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
}
