package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
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
    private PasswordField loginPassword;
    @FXML
    void loginClicked(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, Exception {
        String userName = loginUsername.getText();
        //System.out.println(loginPassword.getText());
        String password = loginPassword.getText();


        if(password == "" || userName == ""){

        };

        String query = "SELECT password FROM user WHERE userName = '" + userName + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //For when username doesn't exist and query result set is empty
        if(!rs.next()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("%login.userNotFound");
            alert.setHeaderText("User " + userName + " not found");
            alert.showAndWait();
        }
        //For when username exists and password is incorrect
        else if(){

        } else{
            //rs.next();
            String userPass = rs.getString("password");
            if (userPass.equals(password)) {
                System.out.println("yay!");
            }

        }


    }
}
