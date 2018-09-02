package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ResourceBundle;

public class Customer {

    private ResourceBundle resources;
    private IntegerProperty customerId;
    private StringProperty customerName;
    private IntegerProperty addressId;
    private StringProperty address;
    private StringProperty address2;
    private IntegerProperty cityId;
    private StringProperty postalCode;
    private StringProperty phone;
    private StringProperty city;
    private IntegerProperty countryId;
    private StringProperty country;
    private IntegerProperty active;

    public Customer(){
        customerId = new SimpleIntegerProperty();
        customerName = new SimpleStringProperty();
        addressId = new SimpleIntegerProperty();
        address = new SimpleStringProperty();
        address2 = new SimpleStringProperty();
        cityId = new SimpleIntegerProperty();
        postalCode = new SimpleStringProperty();
        phone = new SimpleStringProperty();
        city = new SimpleStringProperty();
        countryId = new SimpleIntegerProperty();
        country = new SimpleStringProperty();
        active = new SimpleIntegerProperty();
    }
    public void setCustomerId(IntegerProperty customerId){
        this.customerId = customerId;
    }
    public void setCustomerName(StringProperty customerName){
        this.customerName = customerName;
    }
    public void setAddressId(IntegerProperty addressId){
        this.addressId = addressId;
    }
    public void setAddress(StringProperty address){
        this.address = address;
    }
    public void setAddress2(StringProperty address2){
        this.address2 = address2;
    }
    public void setCityId(IntegerProperty cityId){
        this.cityId = cityId;
    }
    public void setPostalCode(StringProperty postalCode){
        this.postalCode = postalCode;
    }
    public void setPhone(StringProperty phone){
        this.phone = phone;
    }
    public void setCity(StringProperty city){
        this.city = city;
    }
    public void setCountryId(IntegerProperty countryId){
        this.countryId = countryId;
    }
    public void setCountry(StringProperty country){
        this.country = country;
    }
    public void setActive(IntegerProperty active){
        this.active = active;
    }

    public IntegerProperty customerIdProperty(){
        return customerId;
    }
    public StringProperty customerNameProperty(){
        return customerName;
    }
    public IntegerProperty addressIdProperty(){
        return addressId;
    }
    public StringProperty addressProperty(){
        return address;
    }
    public StringProperty address2Property(){
        return address2;
    }
    public IntegerProperty cityIdProperty(){
        return cityId;
    }
    public StringProperty postalCodeProperty(){
        return postalCode;
    }
    public StringProperty phoneProperty(){
        return phone;
    }
    public StringProperty cityProperty(){
        return city;
    }
    public IntegerProperty countryIdProperty(){
        return countryId;
    }
    public StringProperty countryProperty(){
        return country;
    }
    public IntegerProperty activeProperty(){
        return active;
    }
    public int getCustomerId(){
        return customerId.get();
    }
    public String getCustomerName(){
        return customerName.get();
    }
    public int getAddressId(){
        return addressId.get();
    }
    public String getAddress(){
        return address.get();
    }
    public String getAddress2(){
        return address2.get();
    }
    public int getCityId(){
        return cityId.get();
    }
    public String getPostalCode(){
        return postalCode.get();
    }
    public String getPhone(){
        return phone.get();
    }
    public String getCity(){
        return city.get();
    }
    public int getCountryId(){
        return countryId.get();
    }
    public String getCountry(){
        return country.get();
    }
    public int getActive(){
        return active.get();
    }

    public String validCustomer(String customerName, String address, String postalCode, String phone,  String city, String country){
        String error = "";
        if(customerName.length() == 0)
            error = resources.getString("customer.name");
        if(address.length() == 0)
            error = resources.getString("customer.address");
        if(postalCode.length() == 0)
            error = resources.getString("customer.postalCode");
        if(phone.length() == 0)
            error = resources.getString("customer.phone");
        if(city.length() == 0)
            error = resources.getString("customer.city");
        if(country.length() == 0)
            error = resources.getString("customer.country");

        return error;

    }
}
