package controllers;

import Observer.DatabaseObserver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Screen;
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

    @FXML
    Label btnclose;

    String userfriendlisttablename;
    String usermsgtablename;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnsignupclicked(){
        errorlabel.setText("");
        clearall();
        primaryStage.setScene(signupScene);
    }

    @FXML
    public void forgetpasswordclicked() {
        errorlabel.setText("");
        clearall();
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
                createpandingmsgtable();
                friendlistSceneController.reloadfriendlist();
                islogin = true;
                dbobserver = new DatabaseObserver();
                clearall();
                friendlistSceneController.updatenotificationinfo();
                primaryStage.setMinWidth(400);
                primaryStage.setMinHeight(500);
                primaryStage.setWidth(400);
                primaryStage.setHeight(625);
                primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth());
                primaryStage.setY(Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight());
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
                query = "create table MSG_" + rs.getString("fusername") + " ( msgindex INT(255) UNSIGNED NOT NULL AUTO_INCREMENT , stime varchar (8), sdate varchar (8), senderusername varchar (30), receiverusername varchar (30), msg varchar (21000), sendconform int (2), PRIMARY KEY (msgindex))";
                h2statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sql exception
        }
    }

    public void setlablesignout(){
        errorlabel.setText("Successfully Sign Out");
        errorlabel.setStyle("-fx-text-fill: green");
    }

    public void clearall(){
        errorlabel.setText("");
        usernamefield.setText("");
        passwordfield.setText("");
    }

    void createpandingmsgtable(){
        String query = "create table pandingmsg (stime varchar (8), sdate varchar (8), senderusername varchar (30), receiverusername varchar (30), msg varchar (21000), priority INT (255))";
        try {
            h2statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sqlexception
        }
    }

    @FXML
    void btncloseclicked(){
        primaryStage.close();
        System.exit(0);
    }
}
