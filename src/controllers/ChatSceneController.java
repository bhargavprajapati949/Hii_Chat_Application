package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import source.PatternValidation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static source.GlobleVariables.*;

public class ChatSceneController implements Initializable {

    @FXML
    HBox title;

    @FXML
    Button btnback;

    @FXML
    Label friendname;

    @FXML
    MenuButton dropdownmenu;

    @FXML
    public VBox msgcontainer;

    @FXML
    TextField msgtextfield;

    @FXML
    Button btnsendmsg;

    @FXML
    Label notificationinfo;

    MenuItem clearchat;

    @FXML
    void btnbackclicked(){
        friendlistSceneController.updatenotificationinfo();
        primaryStage.setScene(friendlistScene);
    }

    @FXML
    void btnsendmsgclicked(){
        if(!PatternValidation.isnull(msgtextfield.getText())){
            if(!isconnectedtointernet){
                try {
                    savemsgtolocaldb(0);
                    addmsgtosceneandsavetopanding();
                    msgtextfield.setText("");
                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo sql exception
                }
            }
            else {
                try {
                    savemsgtoserverdb();
                    savemsgtolocaldb(1);
                    addmsgtoscene();
                    msgtextfield.setText("");
                } catch (SQLException e) {
                    e.printStackTrace();
                    notificationtext = "Cannot connect to server.";
                    notificationinfo.setText(notificationtext);
                    //todo handle msg send on offline condition
                }

            }
        }

    }

    @FXML
    void friendnamelabelclicked(){
        profileSceneController.fatchfrienddetail();
        profileSceneController.updatenotificationinfo();
        primaryStage.setScene(profileScene);
    }

    @SuppressWarnings("Duplicates")
    private void savemsgtolocaldb(int flag_sendconform) throws SQLException {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm a"));
        String datestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String msg = msgtextfield.getText();
        String query = "insert into MSG_" + currentfriendusername + " (stime, sdate, senderusername,  receiverusername, msg, sendconform) ";
        query += "values ('" + timestamp + "', '" + datestamp + "', '" + myusername + "', '" + currentfriendusername + "', '" + msg + "', " + flag_sendconform + ")";
        h2statement.executeUpdate(query);

    }

    private void addmsgtoscene() {
        //todo addmsgtoscene not  complite
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm a"));
        String datestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        HBox v = msgbox(msgtextfield.getText(), 's', timestamp, datestamp, 1);
        msgcontainer.getChildren().add(v);
    }

    private void addmsgtosceneandsavetopanding(){
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm a"));
        String datestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        HBox v = msgbox(msgtextfield.getText(), 's', timestamp, datestamp, 0);
        msgcontainer.getChildren().add(v);

        String query = "insert into pandingmsg (stime, sdate, senderusername, receiverusername, msg) values (" + timestamp + ", " + datestamp + ", " + myusername + ", " + currentfriendusername + ", " + msgtextfield.getText() + ")";
        try {
            h2statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sqlexception
        }

    }

    @SuppressWarnings("Duplicates")
    private void savemsgtoserverdb() throws SQLException {
        String msg = msgtextfield.getText();
        String query = "insert into MSG_" + currentfriendusername + " (stime, sdate, senderusername,  receiverusername, msg) ";
        query += "values ('" + myusername + "', '" + currentfriendusername + "', '" + msg + "')";
        sqlstatement.executeUpdate(query);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //dropdownmenu.getItems().addAll();
        title.setHgrow(friendname, Priority.ALWAYS);
        clearchat = new MenuItem("Clear Chat");
        dropdownmenu.getItems().addAll(clearchat);

        clearchat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String query = "delete from MSG_" + currentfriendusername;
                try {
                    h2statement.executeUpdate(query);
                    loadchatscene();
                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo handle sql exception
                }
            }
        });
    }


    public void loadchatscene(){
        friendname.setText(currentfriendname);
        String friendmsgtablename = "MSG_" + currentfriendusername;
        try {
            ResultSet rs = h2statement.executeQuery("select * from " + friendmsgtablename + ";");
            char sendorrecive = 's';
            while(rs.next()){
                //todo code for load msg from local database to scene by msgcontainer
                if(rs.getString("senderusername").equals(myusername)){
                    sendorrecive = 's';
                }
                else{
                    sendorrecive = 'r';
                }
                HBox msgbox = msgbox(rs.getString("msg"), sendorrecive, rs.getString("stime"), rs.getString("sdate"), rs.getInt("sendconform"));
                msgcontainer.getChildren().add(msgbox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sql exception
        }
    }

    public void addnewmsg(){

    }

    public HBox msgbox(String msg, char sendorrecive, String timestamp, String datestamp, int flag_sendconform){
        Text msgtext = new Text(msg + "\n");
        msgtext.setFill(Color.WHITE);
        msgtext.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        msgtext.setStrokeType(StrokeType.OUTSIDE);
        //msgtext.setWrappingWidth(300);
        msgtext.setStyle("-fx-background-color: blue");

        TextFlow msgtextflow = new TextFlow();
        msgtextflow.getChildren().add(msgtext);
        msgtextflow.setMaxWidth(300);


        Text timedate = new Text(datestamp + " " + timestamp);
        timedate.setFill(Color.WHITE);
        timedate.setStyle("-fx-font-size: 10px; -fx-font-weight: 300;");
        timedate.setStrokeType(StrokeType.OUTSIDE);

        ImageView sendcon = null;
        if(sendorrecive == 's') {
            if (flag_sendconform == 0) {
                //for save to panding msg
                sendcon = new ImageView("images/tick.png");
            } else if (flag_sendconform == 1) {
                //for msg sucssessfully send
                sendcon = new ImageView("images/doubletick.png");
            }
        }
        sendcon.setFitHeight(17);
        sendcon.setFitWidth(17);
        sendcon.setPickOnBounds(true);
        sendcon.setPreserveRatio(true);

        HBox statusline = new HBox(5);
        statusline.setAlignment(Pos.CENTER_RIGHT);
        statusline.setPrefHeight(17);
        statusline.getChildren().addAll(timedate, sendcon);
        statusline.setStyle("-fx-background-color: red");

        VBox msg_container = new VBox(msgtextflow, statusline);
        msg_container.setStyle("-fx-background-color: green");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(msg_container);
        hbox.prefWidth(398);
        hbox.setMaxWidth(398);
        hbox.setMinWidth(398);
        hbox.setStyle("-fx-background-color: transparent");

        if(sendorrecive == 's'){
            msg_container.setPadding(new Insets(0,20,0,0));
            msg_container.setStyle("-fx-background-color: linear-gradient(to left, #ff512f, #dd2476);" +
                    "-fx-background-insets: -5 -10 -5 -5;" +
                    "-fx-effect: dropshadow(three-pass-box,rgba(0,0,0,0.08),2,1.0,0.5,0.5);" +
                    "-fx-shape: \"M 94.658379,129.18587 H 46.277427 c -3.545458,0.23354 -5.32763,-1.59167 -5.14193,-4.67449 v -19.39913 c 0.405797,-3.73565 2.470637,-4.56641 5.14193,-4.90821 h 43.706464 c 2.572701,0.2361 4.604321, 1.68288 4.674488,4.90821 v 19.39913 c 0.436089,3.14572 2.890695,3.57304 4.908212,4.67449 z\";");
            hbox.setAlignment(Pos.CENTER_RIGHT);
        }
        else if(sendorrecive == 'r'){
            msg_container.setPadding(new Insets(0,0,0,20));
            msg_container.setStyle("-fx-background-color: linear-gradient(to left, #4776e6, #8e54e9);" +
                    "-fx-background-insets: -5 -5 -5 -10;" +
                    "-fx-effect: dropshadow(three-pass-box,rgba(0,0,0,0.08),2,1.0,-0.5,-0.5);\n" +
                    "-fx-shape: \"m 46.030545,129.18592 h 48.380952 c 3.54546,0.23355 5.32763,-1.59167 5.14193,-4.67449 V 105.1123 c -0.4058,-3.73565 -2.47064,-4.56641 -5.14193,-4.90821 H 50.705033 c -2.572701,0.2361 -4.604321,1.68288 -4.674488,4.90821 v 19.39913 c -0.436089,3.14572 -2.890695,3.57304 -4.908212,4.67449 z\";");
            hbox.setAlignment(Pos.CENTER_LEFT);
        }

        return hbox;
    }

    public void updatenotificationinfo(){
        notificationinfo.setText(notificationtext);
    }
}
