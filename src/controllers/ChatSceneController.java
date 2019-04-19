package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import source.IsConnectedToInternet;
import source.PatternValidation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    VBox msgcontainer;

    @FXML
    TextField msgtextfield;

    @FXML
    Button btnsendmsg;

    @FXML
    Label notificationinfo;

    MenuItem clearchat;

    @FXML
    void btnbackclicked(){
        primaryStage.setScene(friendlistScene);
    }

    @FXML
    void btnsendmsgclicked(){
        if(!PatternValidation.isnull(msgtextfield.getText())){
            if(!IsConnectedToInternet.check()){
                notificationtext = "You are offline.";
                notificationinfo.setText(notificationtext);
            }
            else {
                try {
                    savemsgtoserverdb();
                    savemsgtolocaldb();
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
        primaryStage.setScene(profileScene);
    }

    @SuppressWarnings("Duplicates")
    private void savemsgtolocaldb() throws SQLException {
        String msg = msgtextfield.getText();
        String query = "insert into MSG_" + currentfriendusername + " (senderusername,  receiverusername, msg) ";
        query += "values ('" + myusername + "', '" + currentfriendusername + "', '" + msg + "')";
        h2statement.executeUpdate(query);

    }

    private void addmsgtoscene() {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm aa"));
        String datestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/mm/yyyy"));
        AnchorPane v = msgbox(msgtextfield.getText(), 's', timestamp, datestamp);
        msgcontainer.getChildren().add(v);
    }

    @SuppressWarnings("Duplicates")
    private void savemsgtoserverdb() throws SQLException {
        String msg = msgtextfield.getText();
        String query = "insert into MSG_" + currentfriendusername + " (senderusername,  receiverusername, msg) ";
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
            while(rs.next()){
                //todo code for load msg from local database to scene by msgcontainer
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sql exception
        }
    }

    private AnchorPane msgbox(String msg, char senderorreciver, String timestamp, String datestamp){
        Text msgtext = new Text(msg);
        Label timedatatext = new Label("time date");
        VBox v = new VBox(msgtext, timedatatext);
        v.setMaxWidth(1.79);
        v.setPrefWidth(555);
        AnchorPane ap = new AnchorPane(v);
        return ap;
    }
}
