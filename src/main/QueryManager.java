package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.DBConnection.conn;
import Model.Customer;

public class QueryManager{
    private static String loggedUser;

    public static String userValidation(String userName, String password) throws SQLException {
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
    }
    public static ObservableList<Customer> getCustomerTableView(){
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try{
            String allCustomer =    "SELECT U04EE1.customer.customerid, U04EE1.customer.customerName, " +
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
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return customerList;
    }
    public static int deleteTheCustomer(int customerToDelete) throws SQLException{
        String customerToDeleteQuery = "UPDATE U04EE1.customer \n" +
                "SET U04EE1.customer.active = 0 \n" +
                "WHERE U04EE1.customer.customerId = " + customerToDelete + ";";
        Statement stmt = conn.createStatement();
        int rows = stmt.executeUpdate(customerToDeleteQuery);
        return rows;
    }
    public static int addressCityOnly(int addressId, String city, int countryId){
        int newCityId = 0;
        int rows =0;
        String checkNewCity =
                "SELECT U04EE1.city.cityId \n" +
                "FROM U04EE1.city \n" +
                "WHERE U04EE1.city.city = '" + city + "';";
        String insertNewCity =
                "INSERT INTO U04EE1.city (city,countryId,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" + city + "'," + countryId + ",CURDATE(),'" + loggedUser + "',CURDATE(),'" + loggedUser + "');";
        String selectCityId =
                "SELECT U04EE1.city.cityId \n" +
                "FROM U04EE1.city \n" +
                "WHERE U04EE1.city.city = '" + city + "';";
        try{
            //Check if city exists
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkNewCity);
            if(!rs.next()){
                //City doesn't already exist, so try insert
                try{
                    //insert statement to insert city
                    Statement insertStmt = conn.createStatement();
                    int insertRs = insertStmt.executeUpdate(insertNewCity);
                    if(insertRs==0){
                        System.out.println("No rows were inserted");
                        return -1;
                    }else{
                        try {
                            //trying to get the city id of the new city row and update address table
                            Statement selectStmt = conn.createStatement();
                            ResultSet selectRs = selectStmt.executeQuery(selectCityId);
                            selectRs.next();
                            newCityId = selectRs.getInt("cityId");
                            String addressUpdate =
                                            "UPDATE U04EE1.address \n" +
                                            "SET U04EE1.address.cityId = " + newCityId + " \n" +
                                            "WHERE U04EE1.address.addressId = " + addressId + ";";

                            try {
                                Statement addressUpdateStmt = conn.createStatement();
                                rows = addressUpdateStmt.executeUpdate(addressUpdate);
                                return rows;
                            }catch(Exception e){
                                System.out.println("Error updating address table");
                                System.out.println(addressUpdate);
                                return -1;
                            }
                        }catch(Exception e){
                            System.out.println("Error getting new city Id");
                            System.out.println(selectCityId);
                            return -1;
                        }
                    }
                }catch(Exception e){
                    System.out.println("Failed to insert city into city table");
                    System.out.println(insertNewCity);
                    return -1;
                }
            }else{
                newCityId = rs.getInt("cityId");
                String addressUpdate =
                                "UPDATE U04EE1.address \n" +
                                "SET U04EE1.address.cityId = " + newCityId + " \n" +
                                "WHERE U04EE1.address.addressId = " + addressId + ";";
                Statement existCityAddressUpdate = conn.createStatement();
                rows = existCityAddressUpdate.executeUpdate(addressUpdate);
                return rows;
            }
        }catch(Exception e){
            System.out.println("Failed to run select statement to search for existing city");
            return -1;
        }
    }
    public static int addressCityCountry(String country,){
        String checkNewCountry =
                "SELECT U04EE1.country.countryId \n" +
                        "FROM U04EE1.country \n" +
                        "WHERE U04EE1.country.country = '" + country + "';";
        

        return 0;
    }
}
