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
    public String address;
    public String city;
    public String state;
    public String address_type;
    public String country;
    public String zip;
    public String first_name;
    public String last_name;
    public String phone_num;
    public String neighbourhood;
    public String landmark;
    public String is_default_billing_add;
    public String customer_id;
    public String location_id;

    public LocationObject() {
    }

    public LocationObject(Parcel in) {
        address = in.readString();
        city = in.readString();
        state = in.readString();
        address_type = in.readString();
        country = in.readString();
        zip = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        phone_num = in.readString();
        neighbourhood = in.readString();
        landmark = in.readString();
        is_default_billing_add = in.readString();
        customer_id = in.readString();
        location_id = in.readString();
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
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

    public String getIs_default_billing_add() {
        return is_default_billing_add;
    }

    public void setIs_default_billing_add(String is_default_billing_add) {
        this.is_default_billing_add = is_default_billing_add;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
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
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(phone_num);
        dest.writeString(neighbourhood);
        dest.writeString(landmark);
        dest.writeString(is_default_billing_add);
        dest.writeString(customer_id);
        dest.writeString(location_id);
    }
}
