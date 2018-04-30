package in.ajm.sb.data;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationObject implements Parcelable {

    public static final Creator<LocationObject> CREATOR = new Creator<LocationObject>() {
        @Override
        public LocationObject createFromParcel(Parcel in) {
            return new LocationObject(in);
        }

        @Override
        public LocationObject[] newArray(int size) {
            return new LocationObject[size];
        }
    };
    private String city;
    private String state;
    private String address_type;
    private String country;
    private String zip;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String neighbourhood;
    private String landmark;
    private String isDefaultBillingAdd;
    private String customerId;
    private String locationId;
    private String address;
    private String isPrimary;

    public LocationObject() {
    }

    public LocationObject(Parcel in) {
        address = in.readString();
        city = in.readString();
        state = in.readString();
        address_type = in.readString();
        country = in.readString();
        zip = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phoneNum = in.readString();
        neighbourhood = in.readString();
        landmark = in.readString();
        isDefaultBillingAdd = in.readString();
        customerId = in.readString();
        locationId = in.readString();
        isPrimary = in.readString();
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getIsDefaultBillingAdd() {
        return isDefaultBillingAdd;
    }

    public void setIsDefaultBillingAdd(String isDefaultBillingAdd) {
        this.isDefaultBillingAdd = isDefaultBillingAdd;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(address_type);
        dest.writeString(country);
        dest.writeString(zip);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNum);
        dest.writeString(neighbourhood);
        dest.writeString(landmark);
        dest.writeString(isDefaultBillingAdd);
        dest.writeString(customerId);
        dest.writeString(locationId);
        dest.writeString(isPrimary);
    }
}
