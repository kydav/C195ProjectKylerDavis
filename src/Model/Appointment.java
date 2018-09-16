package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Appointment {

    private static ResourceBundle resources;
    private IntegerProperty appointmentId;
    private IntegerProperty customerId;
    private IntegerProperty userId;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty contact;
    private StringProperty type;
    private StringProperty url;
    private DateFormat start;
    private DateFormat end;

    public Appointment(){
        appointmentId = new SimpleIntegerProperty();
        customerId = new SimpleIntegerProperty();
        userId = new SimpleIntegerProperty();
        title = new SimpleStringProperty();
        description = new SimpleStringProperty();
        location = new SimpleStringProperty();
        contact = new SimpleStringProperty();
        type = new SimpleStringProperty();
        url = new SimpleStringProperty();
        start = new SimpleDateFormat();
        end = new SimpleDateFormat();
    }
    public void setAppointmentId(IntegerProperty appointmentId){
        this.appointmentId = appointmentId;
    }
    public void setCustomerId(IntegerProperty customerId){ this.customerId = customerId; }
    public void setUserId(IntegerProperty userId){ this.userId = userId; }
    public void setTitle(String title){ this.title.set(title); }
    public void setDescription(String description){ this.description.set(description); }
    public void setLocation(String location){ this.location.set(location); }
    public void setContact(String contact){ this.contact.set(contact); }
    public void setType(String type){ this.type.set(type); }
    public void setUrl(String url){ this.url.set(url); }
    public void setStart(DateFormat start){ this.start = start; }
    public void setEnd(DateFormat end){ this.end = end; }

    public IntegerProperty appointmentIdProperty(){ return appointmentId; }
    public IntegerProperty customerIdProperty(){ return customerId; }
    public IntegerProperty userIdProperty(){ return userId; }
    public StringProperty titleProperty(){ return title;    }
    public StringProperty descriptionProperty(){ return description; }
    public StringProperty locationProperty(){ return location; }
    public StringProperty contactProperty(){ return contact; }
    public StringProperty typeProperty(){ return type; }
    public StringProperty urlProperty(){ return url; }
    public DateFormat startProperty(){ return start; }
    public DateFormat endProperty(){ return end; }
    public int getAppointmentId(){ return appointmentId.get(); }
    public int getCustomerId(){ return customerId.get(); }
    public int getUserId(){ return userId.get(); }
    public String getTitle(){ return title.get(); }
    public String getDescription(){ return description.get(); }
    public String getLocation(){ return location.get(); }
    public String getContact(){ return contact.get(); }
    public String getType(){ return type.get(); }
    public String getUrl(){ return url.get(); }
    public DateFormat getStart(){ return start; }
    public DateFormat getEnd(){ return end; }


    public static String validAppointment(String title, String description, String location, String contact,  String start, String end){
        String error = "";
        if(title.length() == 0)
            error = resources.getString("customer.name");
        if(description.length() == 0)
            error = resources.getString("customer.address");
        if(location.length() == 0)
            error = resources.getString("customer.postalCode");
        if(contact.length() == 0)
            error = resources.getString("customer.phone");
        if(start.length() == 0)
            error = resources.getString("customer.city");
        if(end.length() == 0)
            error = resources.getString("customer.country");
        return error;

    }
}
