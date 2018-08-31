package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import main.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.DBConnection.*;


public class LoginScreenController {
    @FXML
    private TextField loginUsername;
    @FXML
    private TextField loginPassword;
    @FXML
    void loginClicked(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, Exception {
        String userName = loginUsername.getText();
        String password = loginPassword.getText();
        String query = "SELECT password FROM user WHERE userName = '" + userName + "'";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        rs.next();
        String userPass = rs.getString("password");

        if(userPass.equals(password)){
            System.out.println("yay!");
        }




    }
}
