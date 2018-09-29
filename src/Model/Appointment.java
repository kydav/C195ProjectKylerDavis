package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.java2d.pipe.SpanShapeRenderer;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import java.util.Locale;



public class Appointment {
    private IntegerProperty appointmentId;
    private IntegerProperty customerId;
    private StringProperty customerName;
    private IntegerProperty userId;
    private StringProperty userName;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty contact;
    private StringProperty type;
    private StringProperty url;
    private Timestamp start;
    private Timestamp end;
    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;
    private String startDate;
    private String endDate;
    private String startDayOfWeek;
    private String endDayOfWeek;

    public Appointment(){
        appointmentId = new SimpleIntegerProperty();
        customerId = new SimpleIntegerProperty();
        customerName = new SimpleStringProperty();
        userId = new SimpleIntegerProperty();
        userName = new SimpleStringProperty();
        title = new SimpleStringProperty();
        description = new SimpleStringProperty();
        location = new SimpleStringProperty();
        contact = new SimpleStringProperty();
        type = new SimpleStringProperty();
        url = new SimpleStringProperty();
        this.start = start;
        this.end = end;
        this.startLocalDateTime = startLocalDateTime;
        this.endLocalDateTime = endLocalDateTime;
        startDate = new String();
        endDate = new String();
        startDayOfWeek = new String();
        endDayOfWeek = new String();
    }
    public void setAppointmentId(IntegerProperty appointmentId){
        this.appointmentId = appointmentId;
    }
    public void setCustomerId(IntegerProperty customerId){ this.customerId = customerId; }
    public void setCustomerName(String customerName){ this.customerName.set(customerName); }
    public void setUserId(IntegerProperty userId){ this.userId = userId; }
    public void setUserName(String userName){ this.userName.set(userName); }
    public void setTitle(String title){ this.title.set(title); }
    public void setDescription(String description){ this.description.set(description); }
    public void setLocation(String location){ this.location.set(location); }
    public void setContact(String contact){ this.contact.set(contact); }
    public void setType(String type){ this.type.set(type); }
    public void setUrl(String url){ this.url.set(url); }
    public void setStart(Timestamp start){ this.start = start; }
    public void setEnd(Timestamp end){ this.end = end; }
    public void setStartLocalDateTime(LocalDateTime startLocalDateTime){ this.startLocalDateTime = startLocalDateTime; }
    public void setEndLocalDateTime(LocalDateTime endLocalDateTime){ this.endLocalDateTime = endLocalDateTime; }
    public void setStartDate(String startDate){this.startDate = startDate; }
    public void setEndDate(String endDate){this.endDate = endDate; }
    public void setStartDayOfWeek(String startDayOfWeek){ this.startDayOfWeek = startDayOfWeek; }
    public void setEndDayOfWeek(String endDayOfWeek){ this.endDayOfWeek = endDayOfWeek; }

    public IntegerProperty appointmentIdProperty(){ return appointmentId; }
    public IntegerProperty customerIdProperty(){ return customerId; }
    public StringProperty customerName(){ return customerName; }
    public IntegerProperty userIdProperty(){ return userId; }
    public StringProperty userName(){ return userName; }
    public StringProperty titleProperty(){ return title;    }
    public StringProperty descriptionProperty(){ return description; }
    public StringProperty locationProperty(){ return location; }
    public StringProperty contactProperty(){ return contact; }
    public StringProperty typeProperty(){ return type; }
    public StringProperty urlProperty(){ return url; }
    public Timestamp startProperty(){ return start; }
    public Timestamp endProperty(){ return end; }
    public LocalDateTime startLocalDateTimeProperty(){ return startLocalDateTime; }
    public LocalDateTime endLocalDateTimeProperty(){ return startLocalDateTime; }
    public String startDateProperty(){ return startDate; }
    public String endDateProperty(){ return endDate; }
    public String startDayOfWeekProperty(){ return startDayOfWeek; }
    public String EndDayOfWeekProperty(){ return endDayOfWeek; }

    public int getAppointmentId(){ return appointmentId.get(); }
    public int getCustomerId(){ return customerId.get(); }
    public String getCustomerName(){ return customerName.get(); }
    public int getUserId(){ return userId.get(); }
    public String getUserName(){ return userName.get(); }
    public String getTitle(){ return title.get(); }
    public String getDescription(){ return description.get(); }
    public String getLocation(){ return location.get(); }
    public String getContact(){ return contact.get(); }
    public String getType(){ return type.get(); }
    public String getUrl(){ return url.get(); }
    public Timestamp getStart(){ return start; }
    public Timestamp getEnd(){ return end; }
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime; }
    public LocalDateTime getEndLocalDateTime() { return endLocalDateTime; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStartDayOfWeek() { return startDayOfWeek; }
    public String getEndDayOfWeek() { return endDayOfWeek; }


    public static String validAppointment(String title, String customer, String user, String type, String description, Timestamp start, Timestamp end, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, LocalTime startTime, LocalTime endTime){
        String error = "";
        ResourceBundle resources = ResourceBundle.getBundle("Resources", Locale.getDefault());
        if(title.isEmpty())
            error = resources.getString("appointment.title");
        if(customer.isEmpty())
            error = resources.getString("appointment.customer");
        if(user.isEmpty())
            error = resources.getString("appointment.user");
        if(type.isEmpty())
            error = resources.getString("appointment.type");
        if(description.isEmpty())
            error = resources.getString("appointment.description");
        if(start.toString() == null)
            error = resources.getString("appointment.start");
        if(end.toString() == null)
            error = resources.getString("appointment.end");
        if(startLocalDateTime.getDayOfWeek().toString().equals("SATURDAY") || startLocalDateTime.getDayOfWeek().toString().equals("SUNDAY") ||
                endLocalDateTime.getDayOfWeek().toString().equals("SATURDAY") || endLocalDateTime.getDayOfWeek().toString().equals("SUNDAY"))
            error = resources.getString("appointment.weekend");
        if(startTime.getHour() < 8 && startTime.getHour() > 16  || endTime.getHour() < 8 && endTime.getHour() > 17)
            error = resources.getString("appointment.afterHours");
        return error;

    }
}
