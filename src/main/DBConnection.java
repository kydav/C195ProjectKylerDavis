package main;
import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static final String databaseName = "U04EE1";
    private static final String DBUrl = "jdbc:mysql://52.206.157.109/" + databaseName;
    private static final String username = "U04EE1";
    private static final String password = "53688214657";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection conn;

    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DBUrl, username, password);
        System.out.println("Connection Successful");
    }

    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        conn.close();
        System.out.println("Connection Close Successful");
    }


}
