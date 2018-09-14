package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.DBConnection.conn;
import Model.Customer;

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
        String customerToDeleteQuery = "UPDATE U04EE1.customer \n" +
                "SET U04EE1.customer.active = 0 \n" +
                "WHERE U04EE1.customer.customerId = " + customerToDelete + ";";
        Statement stmt = conn.createStatement();
        int rows = stmt.executeUpdate(customerToDeleteQuery);
        return rows;
    }
    public static int insertCity(String city){
        return 0;
    }
    public static int updateCity(int addressId, String city, int countryId){
        int newCityId = 0;
        int rows =0;
        String checkCity =
                "SELECT U04EE1.city.cityId \n" +
                "FROM U04EE1.city \n" +
                "WHERE U04EE1.city.city = '" + city + "';";
        String insertNewCity =
                "INSERT INTO U04EE1.city (city,countryId,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" + city + "'," + countryId + ",CURDATE(),'" + loggedUser + "',CURDATE(),'" + loggedUser + "');";
        try{
            //Check if city exists
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkCity);
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
                            ResultSet selectRs = selectStmt.executeQuery(checkCity);
                            selectRs.next();
                            newCityId = selectRs.getInt("cityId");
                            String addressUpdate =
                                            "UPDATE U04EE1.address \n" +
                                            "SET U04EE1.address.cityId = " + newCityId + "U04EE1.address.lastUpdate = CURDATE(), U04EE1.address.lastUpdateBy = '" + loggedUser + "' \n" +
                                            "WHERE U04EE1.address.addressId = " + addressId + ";";

                            try {
                                Statement addressUpdateStmt = conn.createStatement();
                                rows = addressUpdateStmt.executeUpdate(addressUpdate);
                                return rows;
                            }catch(SQLException e){
                                System.out.println("Error updating address table: "+ e);
                                return -1;
                            }
                        }catch(SQLException e){
                            System.out.println("Error getting new city Id: " + e);
                            return -1;
                        }
                    }
                }catch(SQLException e){
                    System.out.println("Failed to insert city into city table: " + e);
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
        }catch(SQLException e){
            System.out.println("Failed to run select statement to search for existing city: " + e);
            return -1;
        }
    }
    public static int insertCountry(String country){
        int newCountryId = 0;
        String checkCountry =
                "SELECT U04EE1.country.countryId \n" +
                        "FROM U04EE1.country \n" +
                        "WHERE U04EE1.country.country = '" + country + "';";
        String insertNewCountry =
                "INSERT INTO U04EE1.country (country,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" + country + "',CURDATE(),'" + loggedUser + "',CURDATE(),'" + loggedUser + "');";
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
                        System.out.println("Insert was unsuccessful:" +e);
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


    public static int updateCountry(String country, int addressId, String city){
        int newCountryId = 0;
        String checkCountry =
                        "SELECT U04EE1.country.countryId \n" +
                        "FROM U04EE1.country \n" +
                        "WHERE U04EE1.country.country = '" + country + "';";
        String insertNewCountry =
                        "INSERT INTO U04EE1.country (country,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                        "VALUES ('" + country + "',CURDATE(),'" + loggedUser + "',CURDATE(),'" + loggedUser + "');";
        try{
            //Check if country exists
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkCountry);
            if(!rs.next()){
                //rs is empty, Country does not exist, create it
                Statement insertStmt = conn.createStatement();
                int insertRs = insertStmt.executeUpdate(insertNewCountry);
                if(insertRs == 0){
                    //Catching if the insert didn't go through
                    System.out.println("No rows were inserted");
                    return -1;
                }else{
                    try{
                        //Get the new country Id
                        Statement selectStmt = conn.createStatement();
                        ResultSet selectRs = selectStmt.executeQuery(checkCountry);
                        selectRs.next();
                        newCountryId = selectRs.getInt("countryId");
                        //execute updateCity function to update the city table
                        return updateCity(addressId,city,newCountryId);
                    }catch(SQLException e){
                        System.out.println("failed to get new country Id: " + e);
                        return -1;
                    }
                }
            }else{
                //rs is not empty country does exist, update city to reflect new country
                newCountryId = rs.getInt("countryId");
                return updateCity(addressId,city, newCountryId);
            }
        }catch(SQLException e){
            System.out.println("Failed to check if country exists: " + e);
            return -1;
        }
    }
    public static int updateCustomerTable(String name, int customerId){
        int rows = 0;
        String updateCustomerName =
                "UPDATE U04EE1.customer \n" +
                "SET U04EE1.customer.customerName = '" + name + "',U04EE1.customer.lastUpdate = CURDATE(), U04EE1.customer.lastUpdateBy =  '" + loggedUser + "'\n" +
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
    public static int updateAddressTable(int addressId, String phone, String address, String address2, String postalCode){
        int rows = 0;
        String updateAddress =
                "UPDATE U04EE1.address \n" +
                "SET U04EE1.address.phone = '" + phone + "',U04EE1.address.address = '" + address + "',U04EE1.address.address2 = '" + address2 + "',U04EE1.address.postalCode = '" + postalCode + "',U04EE1.address.lastUpdate = CURDATE(), U04EE1.address.lastUpdateBy =  '" + loggedUser + "'\n" +
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
    public static int insertNewCustomer(String customerName, String phone, String address, String address2, String city, String postalCode, String country){
        int countryId = 0;
        int cityId = 0;
        int addressId = 0;
        String checkExistCountry =
                "SELECT U04EE1.country.countryId" +
                "FROM U04EE1.country" +
                "WHERE U04EE1.country.country ='" + country +"';";
        String checkExistCity =
                "SELECT U04EE1.city.cityId" +
                "FROM U04EE1.city" +
                "WHERE U04EE1.city.city ='" + city +"';";
        String insertCountry =
                "INSERT INTO U04EE1.country (country,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" + country + "',CURDATE(),'" + loggedUser + "',CURDATE(),'" + loggedUser + "');";
        String insertCity =
                "INSERT INTO U04EE1.city (city,countryId,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" + city + "'," + countryId + ",CURDATE(),'" + loggedUser + "',CURDATE(),'" + loggedUser + "');";
        String insertAddress =
                "INSERT INTO U04EE1.address(address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" +address+ "','" +address2+ "',"+cityId+",'" +postalCode+"','"+phone+"',CURDATE(),'"+loggedUser+"',CURDATE(),'" +loggedUser+"');";

        String insertCustomer =
                "INSERT INTO U04EE1.customer(customerName,addressId,active,createdDate,createdBy,lastUpdate,lastUpdateBy) \n" +
                "VALUES ('" +customerName+"',"+addressId+",1,CURDATE(),'"+loggedUser+"',CURDATE(),'"+loggedUser+"');";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkExistCountry);
            if(!rs.next()){
                //Country doesn't exist, create
                Statement insertStmt = conn.createStatement();
                int insertRs = insertStmt.executeUpdate(insertCountry);
                if(insertRs == 0){
                    //no rows inserted
                    System.out.println("No Country was inserted");
                }
                else{
                    try{
                        Statement selectStmt = conn.createStatement();
                        ResultSet selectRs = selectStmt.executeQuery(checkExistCountry);
                        if(!rs.next()){
                            //Couldn't find newly inserted row in DB
                        }else{
                            countryId = rs.getInt("countryId");
                        }
                    }catch(SQLException e){

                    }
                }

            }else{
                //Country exists set countryId to id from select statement
                countryId = rs.getInt("countryId");
            }
        }catch(SQLException e){

        }
        return 0;
    }


}
