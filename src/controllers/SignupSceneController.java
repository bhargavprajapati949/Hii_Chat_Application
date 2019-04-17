package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import source.IsConnectedToInternet;
import source.PatternValidation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static source.GlobleVariables.*;

public class SignupSceneController implements Initializable {

    Boolean allVarified = false;

    @FXML
    Button btnsignup;

    @FXML
    Hyperlink signinlink;

    @FXML
    TextField namefield;
    @FXML
    Label namelabel;

    @FXML
    TextField usernamefield;
    @FXML
    Label usernamelabel;

    @FXML
    ComboBox genderfield;
    @FXML
    Label genderlabel;

    @FXML
    TextField mobilenofield;
    @FXML
    Label mobilenolabel;

    @FXML
    TextField emailidfield;
    @FXML
    Label emailidlabel;

    @FXML
    PasswordField passwordfield;
    @FXML
    Label passwordlabel;

    @FXML
    PasswordField conpasswordfield;
    @FXML
    Label conpasswordlabel;

    @FXML
    ComboBox secquefield;
    @FXML
    Label secquelabel;

    @FXML
    TextField secqueansfield;
    @FXML
    Label secqueanslabel;

    Boolean vldname = false;
    Boolean vldusername = false;
    Boolean vldgender = false;
    Boolean vldmobileno = false;
    Boolean vldemailid = false;
    Boolean vldconpassword = false;
    Boolean vldsecque = false;
    Boolean vldsecqueans = false;

    String usermsgtablename;
    String userfriendlisttablename;

    //new listeners
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        genderfield.setItems(FXCollections.observableArrayList("Male", "Female", "Others"));

        secquefield.setItems(FXCollections.observableArrayList("Your Birthday", "Last School", "Grandfather's name", "Your first mobile's company", "Your Favourite book"));

        namefield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifynamefield();
                }
            }
        });

        usernamefield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifyusernamefield();
                }
            }
        });

        genderfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifygenderfield();
                }
            }
        });

        mobilenofield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifymobilenofield();
                }
            }
        });

        emailidfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifyemailidfield();
                }
            }
        });


        conpasswordfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifyconpasswordfield();
                }
            }
        });

        secquefield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @SuppressWarnings("Duplicates")
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifysecquefield();
                }
            }
        });

        secqueansfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifysecqueansfield();
                }
            }
        });
    }

    void varifynamefield(){
        if(PatternValidation.isnull((String) namefield.getText())){
            namelabel.setText("Name cannot be empty.");
            vldname = false;
        }
        else {
            namelabel.setText("");
            vldname = true;
        }
    }

    void varifyusernamefield(){
        try {
            if(PatternValidation.isnull(usernamefield.getText())){
                usernamelabel.setText("Username cannot be empty.");
                usernamelabel.setStyle("-fx-text-fill: red");
                vldusername = false;
            }
            else {
                ResultSet rs;
                String query = "select count(username) from USERINFO WHERE username=";
                query += "\"" + usernamefield.getText() + "\"";
                rs = sqlstatement.executeQuery(query);
                rs.next();
                if (rs.getInt(1) != 0) {
                    usernamelabel.setText("Username is already taken select other one.");
                    usernamelabel.setStyle("-fx-text-fill: red");
                    vldusername = false;
                } else if (rs.getInt(1) == 0) {
                    usernamelabel.setText("Username is valid.");
                    usernamelabel.setStyle("-fx-text-fill: green");
                    vldusername = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle exception
        }
    }

    void varifygenderfield(){
        if(PatternValidation.isnull(genderfield)){
            genderlabel.setText("Please select gender");
            vldgender = false;
        }
        else {
            genderlabel.setText("");
            vldgender = true;
        }
    }

    void varifymobilenofield(){
        if(!PatternValidation.isNum(mobilenofield.getText())){
            mobilenolabel.setText("Mobile number is not valid.");
            vldmobileno = false;
        }
        else {
            mobilenolabel.setText("");
            vldmobileno = true;
        }
    }

    void varifyemailidfield(){
        if(!PatternValidation.email(emailidfield.getText())){
            emailidlabel.setText("Email Id is not valid.");
            vldemailid = false;
        }
        else {
            emailidlabel.setText("");
            vldemailid = true;
        }
    }

    void varifyconpasswordfield(){
        if(!passwordfield.getText().equals(conpasswordfield.getText())){
            conpasswordlabel.setText("Password does not match.");
            passwordlabel.setText("");
            vldconpassword = false;
        }
        else if(PatternValidation.isnull(conpasswordfield.getText())){
            passwordlabel.setText("Password cannot be empty.");
            conpasswordlabel.setText("");
            vldconpassword = false;
        }
        else{
            conpasswordlabel.setText("");
            passwordlabel.setText("");
            vldconpassword = true;
        }
    }

    void varifysecquefield(){
        if(PatternValidation.isnull(secquefield)){
            secquelabel.setText("Please select security question");
            vldsecque = false;
        }
        else {
            secquelabel.setText("");
            vldsecque = true;
        }
    }

    void varifysecqueansfield(){
        if(PatternValidation.isnull(secqueansfield.getText())){
            secqueanslabel.setText("Answer cannot be empty.");
            vldsecqueans = false;
        }
        else {
            secqueanslabel.setText("");
            vldsecqueans = true;
        }
    }

    @SuppressWarnings("Duplicates")
    void clearall(){
        namefield.setText("");
        namelabel.setText("");
        usernamefield.setText("");
        usernamelabel.setText("");
        genderfield.setValue(null);
        genderlabel.setText("");
        mobilenofield.setText("");
        mobilenolabel.setText("");
        emailidfield.setText("");
        emailidlabel.setText("");
        passwordfield.setText("");
        passwordlabel.setText("");
        conpasswordfield.setText("");
        conpasswordlabel.setText("");
        secquefield.setValue(null);
        secqueansfield.setText("");
    }

    //signin link
    @FXML
    public void signinlinkclicked() {
        clearall();
        signinSceneController.clearall();
        primaryStage.setScene(signinScene);
    }

    //signup btn
    @FXML
    public void btnsignupclicked(){
        userfriendlisttablename = "FRIENDLIST_" + usernamefield.getText().toUpperCase();
        usermsgtablename = "MSG_" + usernamefield.getText().toUpperCase();
        //checking for all value for validity
        if(!(vldusername && vldname &&vldgender && vldmobileno && vldemailid && vldconpassword && vldsecque && vldsecqueans)) {
            varifynamefield();
            varifyusernamefield();
            varifygenderfield();
            varifymobilenofield();
            varifyemailidfield();
            varifyconpasswordfield();
            varifysecquefield();
            varifysecqueansfield();
        }
        else{
            if(!IsConnectedToInternet.check()){
                notification = "You are offline";
                //todo show notification
            }
            else {
                try {
                    uploadpersonaldata();

                    createusersfriendlisttableonserver();

                    createusermsgtableonserver();

                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo handle sql exception
                    notification = "Cannot reach to server. Please try after sometime.";
                }
                clearall();
                signinSceneController.clearall();
                primaryStage.setScene(signinScene);
            }
        }
    }


    void uploadpersonaldata() throws SQLException {
        String query = "insert into USERINFO(username, name, gender, mobileno, emailid, password, secque, secqueans) values (";
        query += "\"" + usernamefield.getText().toUpperCase() + "\", \"";
        query += namefield.getText() + "\", \"";
        query += genderfield.getValue().toString() + "\", \"";
        query += mobilenofield.getText() + "\",\"";
        query += emailidfield.getText() + "\",\"";
        query += passwordfield.getText() + "\",\"";
        query += secquefield.getValue().toString() + "\",\"";
        query += secqueansfield.getText() + "\" )";
        sqlstatement.executeUpdate(query);
    }

    private void createusersfriendlisttableonserver() throws SQLException {
        String query = "create table " + userfriendlisttablename + "( fname varchar (30), fusername varchar (30), gender varchar (12), priority int (255), mobileno varchar (10), emailid varchar (30))";
        sqlstatement.executeUpdate(query);
    }

    private void createusermsgtableonserver() throws SQLException {
        String query = "create table " + usermsgtablename + "(sdate varchar (8), stime varchar (8), senderusername varchar (30), receiverusername varchar (30), msg varchar (21000), readconform int (2))";
        sqlstatement.executeUpdate(query);
    }
}
