package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import source.ErrorMsg;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static source.GlobleVariables.*;

public class SigninSceneController implements Initializable {

    @FXML
    Button btnsingup;

    @FXML
    Button btnsignin;

    @FXML
    Hyperlink forgetpasswordlink;

    @FXML
    Label btnminimize;

    @FXML
    TextField usernamefield;

    @FXML
    PasswordField passwordfield;

    @FXML
    Label errorlabel;

    String userfriendlisttablename;
    String usermsgtablename;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnsignupclicked(){
        errorlabel.setText("");
        primaryStage.setScene(signupScene);
    }

    @FXML
    public void forgetpasswordclicked() {
        errorlabel.setText("");
        primaryStage.setScene(forgetPasswordScene);
    }

    @FXML
    public void btnminimizeclicked() {
        primaryStage.setIconified(true);
    }

    @FXML
    public void btnsigninclicked(){
        String query = "select * from USERINFO where username=";
        query += "\"" + usernamefield.getText().toUpperCase() + "\" and password=\"" + passwordfield.getText() + "\"";
        ResultSet rs;
        try {
            rs = sqlstatement.executeQuery(query);
            if(!rs.next()){
                errorlabel.setText("Please enter correct username and password.");
                errorlabel.setStyle("-fx-text-fill: red");
            }
            else {
                errorlabel.setText("Successfully Sign In");
                errorlabel.setStyle("-fx-text-fill: green");
                userfriendlisttablename = "FRIENDLIST_" + usernamefield.getText().toUpperCase();
                usermsgtablename = "MSG_" + usernamefield.getText().toUpperCase();
                fetchpersonaldetail(rs);
                fetchfriendlist();
                createmsgforeachfriend();
                friendlistSceneController.reloadfriendlist();
                islogin = true;
                primaryStage.setScene(friendlistScene);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errormsg = "Local database already in use.\n Please close other application using local database.";
            ErrorMsg.showerror();
        }
    }


    void fetchpersonaldetail(ResultSet rs){
        String query;
        try {
            query = "update USERINFO set ";
            query += "islogin=1, ";
            query += "username=" + "'" + rs.getString("username").toUpperCase() + "', ";
            query += "name=" + "'" + rs.getString("name") + "', ";
            query += "gender=" + "'" + rs.getString("gender") + "', ";
            query += "mobileno=" + "'" + rs.getString("mobileno") + "', ";
            query += "emailid=" + "'" + rs.getString("emailid") + "' ";
            query += "where islogin=0;";
            myusername = rs.getString("username").toUpperCase();
            h2statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
            //todo handle exception
        }
    }

    @SuppressWarnings("Duplicates")
    private void fetchfriendlist() {
        String query = "select * from ";
        query += userfriendlisttablename + ";";
        try {
            ResultSet rs = sqlstatement.executeQuery(query);
            while (rs.next()){
                query = "insert into FRIENDLIST (fname, fusername, gender, priority, mobileno, emailid) ";
                query += "values ('" + rs.getString("fname") + "', '";
                query += rs.getString("fusername") + "', '";
                query += rs.getString("gender") + "', ";
                query += rs.getInt("priority") + ", '";
                query += rs.getString("mobileno") + "', '";
                query += rs.getString("emailid") + "'";
                query += ")";
                h2statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sql exception
        }
    }

    private void createmsgforeachfriend() {
        String query = "select fusername from ";
        query += userfriendlisttablename + ";";
        try {
            ResultSet rs = sqlstatement.executeQuery(query);
            while(rs.next()){
                query = "create table MSG_" + rs.getString("fusername") + " (sdate varchar (8), stime varchar (8), senderusername varchar (30), receiverusername varchar (30), msg varchar (21000), readconform int (2))";
                h2statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sql exception
        }
    }

    public void setlablesignout(){
        errorlabel.setText("Successfully Sign Out");
    }

    public void clearall(){
        errorlabel.setText("");
        usernamefield.setText("");
        passwordfield.setText("");
    }
}
