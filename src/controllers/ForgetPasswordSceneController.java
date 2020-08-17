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

public class ForgetPasswordSceneController implements Initializable {
    @FXML
    TextField usernamefield;
    @FXML
    Label usernamelabel;

    @FXML
    ComboBox secquefield;
    @FXML
    Label secquelabel;

    @FXML
    TextField secqueansfield;
    @FXML
    Label secqueanslabel;

    @FXML
    PasswordField passwordfield;
    @FXML
    Label passwordlabel;

    @FXML
    PasswordField conpasswordfield;
    @FXML
    Label conpasswordlabel;

    @FXML
    Label errornotification;

    @FXML
    Hyperlink signinlink;

    @FXML
    Label btnclose;

    Boolean vldusername = false;
    Boolean vldsecque = false;
    Boolean vldsecqueans = false;
    Boolean vldconpassword = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        secquefield.setItems(FXCollections.observableArrayList("Your Birthday", "Last School", "Grandfather's name", "Your first mobile's company", "Your Favourite book"));

        usernamefield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifyusernamefield();
                }
            }
        });

        secquefield.focusedProperty().addListener(new ChangeListener<Boolean>() {
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

        conpasswordfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                varifyconpasswordfield();
            }
        });
    }

    @FXML
    void btnsubmitclicked(){
        if(!(vldsecque && vldsecqueans && vldconpassword)){
            varifyusernamefield();
            varifyconpasswordfield();
            varifysecqueansfield();
            varifysecquefield();
        }
        else {
            if(!IsConnectedToInternet.check()){
                notificationtext = "You are offline";
                errornotification.setText(notificationtext);
            }
            else {
                try {
                    String query = "SELECT secque, secqueans FROM USERINFO WHERE username=";
                    query += "'" + usernamefield.getText().toUpperCase() + "'";
                    ResultSet rs = sqlstatement.executeQuery(query);
                    if(!rs.next()){
                        errornotification.setText("Security detail is wrong. Please reenter correct detail.");
                    }
                    else {
                        if(!((rs.getString("secque").equals(secquefield.getValue().toString())) && (rs.getString("secqueans").equals(secqueansfield.getText())))){
                            errornotification.setText("Security detail is wrong. Please reenter correct detail.");
                        }else {
                            query = "update USERINFO set ";
                            query += "password=" + "\'" + passwordfield.getText() + "\'";
                            query += "where username=" + "\'" + usernamefield.getText().toUpperCase() + "\'";
                            sqlstatement.execute(query);
                            errornotification.setStyle("");
                            clearall();
                            primaryStage.setScene(signinScene);
                        }
                    }
                } catch (SQLException e) {
                    //todo handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void signinlinkclicked(){
        clearall();
        primaryStage.setScene(signinScene);
    }

    void varifyusernamefield(){
        if(PatternValidation.isnull(usernamefield.getText())){
            usernamelabel.setText("Username cannot be empty.");
            vldusername = false;
        }
        else {
            usernamelabel.setText("");
            vldusername = true;
        }
    }

    void varifysecquefield(){
        if(PatternValidation.isnull(secquefield)){
            secquelabel.setText("Please select security question.");
            vldsecque = false;
        }
        else {
            secquelabel.setText("");
            vldsecque = true;
        }
    }

    void varifysecqueansfield(){
        if(PatternValidation.isnull(secqueansfield.getText())){
            secqueanslabel.setText("Security answer cannot be empty.");
            vldsecqueans = false;
        }
        else {
            secqueanslabel.setText("");
            vldsecqueans = true;
        }
    }

    void varifyconpasswordfield(){
        if(PatternValidation.isnull(conpasswordfield.getText())){
            passwordlabel.setText("Password cannot be empty.");
            conpasswordlabel.setText("");
            vldconpassword = false;
        }
        else if(!passwordfield.getText().equals(conpasswordfield.getText())){
            conpasswordlabel.setText("Password does not match");
            passwordlabel.setText("");
            vldconpassword = false;
        }
        else {
            conpasswordlabel.setText("");
            passwordlabel.setText("");
            vldconpassword = true;
        }
    }

    @SuppressWarnings("Duplicates")
    void clearall(){
        usernamelabel.setText("");
        usernamefield.setText("");
        secquefield.setValue(null);
        secquelabel.setText("");
        secqueanslabel.setText("");
        secqueansfield.setText("");
        passwordlabel.setText("");
        passwordfield.setText("");
        conpasswordlabel.setText("");
        conpasswordfield.setText("");
    }

    @FXML
    void btncloseclicked(){
        primaryStage.close();
        System.exit(0);
    }
}