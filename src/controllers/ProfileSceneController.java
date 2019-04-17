package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

import static source.GlobleVariables.*;

public class ProfileSceneController{
    @FXML
    Button btnback;

    @FXML
    Label labelname;

    @FXML
    Label labelgender;

    @FXML
    Label labelusername;

    @FXML
    Label labelmobileno;

    @FXML
    Label labelemailid;

    @FXML
    Label notificationinfo;

    @FXML
    public void btnbackclicked(){
        if(myusername.equals(labelusername.getText())){
            primaryStage.setScene(settingScene);
        }
        else {
            primaryStage.setScene(chatScene);
        }
    }

    public void fatchpersonaldetail(){
        String query;
        ResultSet rs;
        try {
            query = "select * from USERINFO";
            rs = h2statement.executeQuery(query);
            rs.next();
            labelname.setText(rs.getString("name"));
            labelgender.setText(rs.getString("gender"));
            labelusername.setText(rs.getString("username"));
            labelmobileno.setText(rs.getString("mobileno"));
            labelemailid.setText(rs.getString("emailid"));
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sqlexception
        }
    }

    public void fatchfrienddetail(){
        String query;
        ResultSet rs;

        try {
            query = "select * from FRIENDLIST WHERE fusername=";
            query += "'" + currentfriendusername + "'";
            rs = h2statement.executeQuery(query);
            rs.next();
            labelname.setText(rs.getString("fname"));
            labelgender.setText(rs.getString("gender"));
            labelusername.setText(rs.getString("fusername"));
            labelmobileno.setText(rs.getString("mobileno"));
            labelemailid.setText(rs.getString("emailid"));
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sqlexception
        }
    }
}