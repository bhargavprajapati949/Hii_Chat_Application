package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import source.IsConnectedToInternet;
import source.PatternValidation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static source.GlobleVariables.*;


public class AddFriendSceneController implements Initializable {
    @FXML
    TextField fusernamefield;

    @FXML
    Label errorlabel;

    @FXML
    Button btnaddfriend;

    Boolean vldusername = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fusernamefield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    varifyusername();
                }
            }
        });
    }

    private void varifyusername() {
        if(PatternValidation.isnull(fusernamefield.getText())){
            errorlabel.setText("Username cannot be empty.");
            vldusername = false;
        }
        else if(myusername.equals(fusernamefield.getText().toUpperCase())) {
            errorlabel.setText("You cannot enter your own username.");
            vldusername = false;
        }
        else if(chackalreadyfriends()){
            errorlabel.setText("You are already friends.");
            vldusername = false;
        }
        else {
            errorlabel.setText("");
            vldusername = true;
        }
    }

    private boolean chackalreadyfriends() {
        String query = "select count(fusername) from FRIENDLIST where fusername=";
        query += "'" + fusernamefield.getText().toUpperCase() + "'";
        ResultSet rs;
        try {
            rs = h2statement.executeQuery(query);
            rs.next();
            if(rs.getInt(1) == 0){
                return false;
            }
            else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sql exception
        }
        return false;
    }

    @SuppressWarnings("Duplicates")
    @FXML
    void btnaddfriendclicked(){
        if(!vldusername){
            varifyusername();
        }
        else {
            if (!isconnectedtointernet) {
                notificationtext = "You are offline.";
                errorlabel.setText(notificationtext);
            } else {
                String query = "select * from USERINFO where username=";
                query += "'" + fusernamefield.getText().toUpperCase() + "'";
                try {
                    ResultSet rs = sqlstatement.executeQuery(query);
                    Statement sqlstatement1 = sqlcon.createStatement();
                    if (rs.next()) {
                        query = "insert into ";
                        query += "FRIENDLIST_" + myusername;
                        query += "(fname, fusername, gender, priority, mobileno, emailid) values (";
                        query += "\"" + rs.getString("name") + "\", \"";
                        query += rs.getString("username") + "\", \"";
                        query += rs.getString("gender") + "\", ";
                        query += "0" + ", \"";
                        query += rs.getString("mobileno") + "\", \"";
                        query += rs.getString("emailid") + "\")";
                        sqlstatement1.executeUpdate(query);

                        String query1;
                        query1 = "insert into ";
                        query1 += "FRIENDLIST";
                        query1 += "(fname, fusername, gender, priority, mobileno, emailid) values (";
                        query1 += "'" + rs.getString("name") + "', '";
                        query1 += rs.getString("username") + "', '";
                        query1 += rs.getString("gender") + "', ";
                        query1 += "0" + ", '";
                        query1+= rs.getString("mobileno") + "', '";
                        query1 += rs.getString("emailid") + "')";
                        h2statement.executeUpdate(query1);

                        query = "create table MSG_" + rs.getString("username") + " ( msgindex INT(255) UNSIGNED NOT NULL AUTO_INCREMENT , stime varchar (8), sdate varchar (8), senderusername varchar (30), receiverusername varchar (30), msg varchar (21000), sendconform int (2), PRIMARY KEY (msgindex))";

                        h2statement.executeUpdate(query);


                        friendlistSceneController.reloadfriendlist();
                        btnaddfriend.getScene().getWindow().hide();
                        primaryStage.setScene(friendlistScene);
                    } else {
                        errorlabel.setText("Username is not exist. Please enter correct username.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo handle sql exception
                }
            }
        }
    }
}
