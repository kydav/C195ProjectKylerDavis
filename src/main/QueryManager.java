package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.DBConnection.conn;
import Model.Customer;
import Model.Appointment;

public class QueryManager{
    private static String loggedUser;

    public static String userValidation(String userName, String password){
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT password FROM user WHERE userName = '" + userName + "'");
            if (!rs.next()) {
                return "notFound";
            } else {
                String userPass = rs.getString("password");
                if (userPass.equals(password)) {
                    loggedUser = userName;
                    return "authenticated";
                } else {
                    return "incorrectPassword";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error querying for user information: " + e);
            return "error";
        }
    }
    public static ObservableList<Customer> getCustomerTableView(){
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try{
            String allCustomer =
                    "SELECT U04EE1.customer.customerid, U04EE1.customer.customerName, " +
                    "U04EE1.address.addressId, U04EE1.address.address, U04EE1.address.address2,  " +
                    "U04EE1.city.cityId, U04EE1.address.postalCode, U04EE1.address.phone, " +
                    "U04EE1.city.city, U04EE1.city.countryId, U04EE1.country.country, U04EE1.customer.active \n" +
                    "FROM U04EE1.customer \n" +
                    "JOIN U04EE1.address ON U04EE1.customer.addressid = U04EE1.address.addressid \n" +
                    "JOIN U04EE1.city ON U04EE1.address.cityid = U04EE1.city.cityid \n" +
                    "JOIN U04EE1.country ON U04EE1.city.countryid = U04EE1.country.countryid \n" +
                    "WHERE U04EE1.customer.active = 1;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allCustomer);
            while (rs.next()) {
                Customer current = new Customer();
                current.setCustomerId(new SimpleIntegerProperty(Integer.parseInt(rs.getString("customerId"))));
                current.setCustomerName(rs.getString("customerName"));
                current.setAddressId(Integer.parseInt((rs.getString("addressId"))));
                current.setAddress(rs.getString("address"));
                current.setAddress2(rs.getString("address2"));
                current.setCityId(Integer.parseInt((rs.getString("cityId"))));
                current.setPostalCode(rs.getString("postalCode"));
                current.setPhone(rs.getString("phone"));
                current.setCity(rs.getString("city"));
                current.setCountryId(Integer.parseInt((rs.getString("countryId"))));
                current.setCountry(rs.getString("country"));
                current.setActive(Integer.parseInt((rs.getString("active"))));
                customerList.add(current);
            }
        }catch(SQLException e){
            System.out.println("Error on Building Data: " + e);
        }
        return customerList;
    }
    public static int deleteTheCustomer(int customerToDelete) throws SQLException{
        int rows;
        String customerToDeleteQuery = "UPDATE U04EE1.customer \n" +
                "SET U04EE1.customer.active = 0, U04EE1.customer.lastUpdate = NOW(), U04EE1.customer.lastUpdateBy = '" + loggedUser +"' \n" +
                "WHERE U04EE1.customer.customerId = " + customerToDelete + ";";
        try {
            Statement stmt = conn.createStatement();
            rows = stmt.executeUpdate(customerToDeleteQuery);
        }catch(SQLException e){
            System.out.println("Error deleting customer:" + e);
            rows = 0;
        }
        return rows;
    }
    public static int addressUpdate(int newCityId, int addressId){
        int rows=0;
        String addressUpdate =
                "UPDATE U04EE1.address \n" +
                "SET U04EE1.address.cityId = " + newCityId + ",U04EE1.address.lastUpdate = NOW(), U04EE1.address.lastUpdateBy = '" + loggedUser + "' \n" +
                "WHERE U04EE1.address.addressId = " + addressId + ";";
        try {
            Statement addressUpdateStmt = conn.createStatement();
            rows = addressUpdateStmt.executeUpdate(addressUpdate);

        }catch(SQLException e){
            System.out.println("Error updating address table: "+ e);
            rows =  -1;
        }
        return rows;
    }

    public static int insertCity(String city, int countryId){
        int newCityId = 0;
        String checkCity =
                "SELECT U04EE1.city.cityId \n" +
                        "FROM U04EE1.city \n" +
                        "WHERE U04EE1.city.city = '" + city + "';";
        String insertNewCity =
                "INSERT INTO U04EE1.city (city,countryId,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                        "VALUES ('" + city + "'," + countryId + ",NOW(),'" + loggedUser + "',NOW(),'" + loggedUser + "');";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkCity);
            if(!rs.next()){
                try{
                    Statement insertStmt = conn.createStatement();
                    int insertRs = insertStmt.executeUpdate(insertNewCity);
                    if (insertRs == 0) {
                        //insert didn't work
                        System.out.println("Failed to insert data, no rows were updated");
                        newCityId = -1;
                    } else {
                        try {
                            Statement selectStmt = conn.createStatement();
                            ResultSet selectRs = selectStmt.executeQuery(checkCity);
                            selectRs.next();
                            newCityId = selectRs.getInt("cityId");
                        }catch(SQLException e){
                            System.out.println("Failed to get new city id: " +e);
                            newCityId = -1;
                        }
                    }
                }catch(SQLException e){
                    System.out.println("City insert was unsuccessful:" +e);
                    newCityId = -1;
                }
            }else{
                //found, need to return id
                newCityId = rs.getInt("cityId");
            }
        }catch(SQLException e){
            System.out.println("Failed to check for existing city: " +e);
            newCityId = -1;
        }
        return newCityId;
    }
    public static int insertCountry(String country){
        int newCountryId = 0;
        String checkCountry =
                "SELECT U04EE1.country.countryId \n" +
                "FROM U04EE1.country \n" +
                "WHERE U04EE1.country.country = '" + country + "';";
        String insertNewCountry =
                "INSERT INTO U04EE1.country (country,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" + country + "',NOW(),'" + loggedUser + "',NOW(),'" + loggedUser + "');";
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(checkCountry);
                if(!rs.next()){
                    try {
                        //not found insert
                        Statement insertStmt = conn.createStatement();
                        int insertRs = insertStmt.executeUpdate(insertNewCountry);
                        if (insertRs == 0) {
                            //insert didn't work
                            System.out.println("Failed to insert data, no rows were updated");
                            newCountryId = -1;
                        } else {
                            try {
                                Statement selectStmt = conn.createStatement();
                                ResultSet selectRs = selectStmt.executeQuery(checkCountry);
                                selectRs.next();
                                newCountryId = selectRs.getInt("countryId");
                            }catch(SQLException e){
                                System.out.println("Failed to get new country id: " +e);
                                newCountryId = -1;
                            }
                        }
                    }catch(SQLException e){
                        System.out.println("Country insert was unsuccessful:" +e);
                        newCountryId = -1;
                    }
                }else{
                    //found, need to return id
                    newCountryId = rs.getInt("countryId");
                }
            }catch(SQLException e){
                System.out.println("Failed to check for existing country: " +e);
                newCountryId = -1;
            }
            return newCountryId;
    }
    public static int updateAddressTable(int addressId, String phone, String address, String address2, String postalCode){
        int rows = 0;
        String updateAddress =
                "UPDATE U04EE1.address \n" +
                "SET U04EE1.address.phone = '" + phone + "',U04EE1.address.address = '" + address + "',U04EE1.address.address2 = '" + address2 + "',U04EE1.address.postalCode = '" + postalCode + "',U04EE1.address.lastUpdate = NOW(), U04EE1.address.lastUpdateBy =  '" + loggedUser + "'\n" +
                "WHERE U04EE1.address.addressId = " + addressId + ";";
        try {
            Statement existCityAddressUpdate = conn.createStatement();
            rows = existCityAddressUpdate.executeUpdate(updateAddress);
            return rows;
        }catch(SQLException e){
            System.out.println("Failed to update address table: " + e);
            return -1;
        }
    }
    public static int updateCustomerTable(String name, int customerId){
        int rows = 0;
        String updateCustomerName =
                "UPDATE U04EE1.customer \n" +
                "SET U04EE1.customer.customerName = '" + name + "',U04EE1.customer.lastUpdate = NOW(), U04EE1.customer.lastUpdateBy =  '" + loggedUser + "'\n" +
                "WHERE U04EE1.customer.customerId = " + customerId + ";";
        try {
            Statement existCityAddressUpdate = conn.createStatement();
            rows = existCityAddressUpdate.executeUpdate(updateCustomerName);
            return rows;
        }catch(SQLException e){
            System.out.println("Failed to update customer table: " + e);
            return -1;
        }
    }
    public static int insertAddress(String address,String address2,int cityId,String postalCode,String phone){
        int newAddressId = 0;
        String insertAddress =
                "INSERT INTO U04EE1.address(address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" +address+ "','" +address2+ "',"+cityId+",'" +postalCode+"','"+phone+"',NOW(),'"+loggedUser+"',NOW(),'" +loggedUser+"');";
        String checkNewAddress =
                "SELECT U04EE1.address.addressId \n" +
                "FROM U04EE1.address \n" +
                "WHERE U04EE1.address.address ='" + address +"';";
        try{
            Statement insertStmt = conn.createStatement();
            int insertRs = insertStmt.executeUpdate(insertAddress);
            if (insertRs == 0) {
                //insert didn't work
                System.out.println("Failed to insert data, no rows were updated");
                newAddressId = -1;
            } else {
                try {
                    Statement selectStmt = conn.createStatement();
                    ResultSet selectRs = selectStmt.executeQuery(checkNewAddress);
                    selectRs.next();
                    newAddressId = selectRs.getInt("addressId");
                }catch(SQLException e){
                    System.out.println("Failed to get new country id: " +e);
                    newAddressId = -1;
                }
            }
        }catch(SQLException e){
            System.out.println("Address insert was unsuccessful:" +e);
            newAddressId = -1;
        }
        return newAddressId;
    }
    public static int insertCustomer(String customerName,int addressId){
        int rows = 0;
        String insertAddress =
                "INSERT INTO U04EE1.customer(customerName,addressId,active,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" +customerName+ "','" +addressId+ "',1,NOW(),'"+loggedUser+"',NOW(),'" +loggedUser+"');";
        try{
            Statement insertStmt = conn.createStatement();
            int insertRs = insertStmt.executeUpdate(insertAddress);
            if (insertRs == 0) {
                //insert didn't work
                System.out.println("Failed to insert data, no rows were updated");
                rows = -1;
            } else {
                return insertRs;
            }
        }catch(SQLException e){
            System.out.println("Customer Insert was unsuccessful:" +e);
            rows = -1;
        }
        return rows;
    }
    public static ObservableList<Appointment> getAppointmentTableView(){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try{
            String allAppointment =
                    "SELECT U04EE1.appointment.appointmentid, U04EE1.appointment.customerId, U04EE1.appointment.userid, " +
                    "U04EE1.appointment.title, U04EE1.customer.customerName, U04EE1.appointment.contact, " +
                    "U04EE1.appointment.description,  U04EE1.appointment.location, U04EE1.appointment.url, " +
                    "U04EE1.appointment.start, U04EE1.appointment.end \n" +
                    "FROM U04EE1.appointment \n" +
                    "JOIN U04EE1.customer ON U04EE1.appointment.customerId = U04EE1.customer.customerId;";
            System.out.println(allAppointment);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allAppointment);
            while (rs.next()) {
                Appointment current = new Appointment();
                current.setAppointmentId(new SimpleIntegerProperty(Integer.parseInt(rs.getString("appointmentId"))));
                current.setCustomerId(new SimpleIntegerProperty(Integer.parseInt(rs.getString("customerId"))));
                current.setUserId(new SimpleIntegerProperty(Integer.parseInt(rs.getString("userId"))));
                current.setTitle(rs.getString("title"));
                current.setCustomerName(rs.getString("customerName"));
                current.setContact(rs.getString("contact"));
                current.setDescription(rs.getString("description"));
                current.setLocation(rs.getString("location"));
                current.setUrl(rs.getString("url"));
                current.setStart(rs.getTimestamp("start"));
                current.setEnd(rs.getTimestamp("end"));
                appointmentList.add(current);
            }
        }catch(SQLException e){
            System.out.println("Error on Building Data: " + e);
        }
        return appointmentList;
    }

    public static ObservableList<String> getCustomerNames(){
        ObservableList<String> allCustomersList  = FXCollections.observableArrayList();
        try{
            String allCustomers =
                            "SELECT U04EE1.customer.customerName \n" +
                            "FROM U04EE1.customer \n" +
                            "WHERE U04EE1.customer.active = 1;";
            System.out.println(allCustomers);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allCustomers);
            while(rs.next()){
                allCustomersList.add(rs.getString("customerName"));
            }
            return allCustomersList;
        }catch(SQLException e){
            System.out.println("Error getting user name data: " + e);
            return null;
        }
    }

}
