package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
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

    @FXML
    void btnbackclicked(){
        primaryStage.setScene(friendlistScene);
    }

    @FXML
    void btnsendmsgclicked(){
        if(!PatternValidation.isnull(msgtextfield.getText())){
            if(!IsConnectedToInternet.check()){
                notification = "You are offline.";
                //todo show notification to status bar
            }
            else {
                try {
                    savemsgtoserverdb();
                    savemsgtolocaldb();
                    addmsgtoscene();
                    msgtextfield.setText("");
                } catch (SQLException e) {
                    e.printStackTrace();
                    notification = "Cannot connect to server.";
                    //todo show notification
                    //todo handle msg send on offline condition
                }

            }
        }

    }




    @SuppressWarnings("Duplicates")
    private void savemsgtolocaldb() throws SQLException {
        String msg = msgtextfield.getText();
        String query = "insert into MSG_" + currentfriendusername + " (senderusername,  receiverusername, msg) ";
        query += "values ('" + myusername + "', '" + currentfriendusername + "', '" + msg + "')";
        h2statement.executeUpdate(query);

    }

    private void addmsgtoscene() {
        AnchorPane v = msgbox(msgtextfield.getText(), 's', "time date");
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
        loadmsgtoscene();


    }

    private void loadmsgtoscene() {

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

    private AnchorPane msgbox(String msg, char senderorreciver, String timedate){
        Text msgtext = new Text(msg);
        Text timedatatext = new Text("time date");
        VBox v = new VBox(msgtext, timedatatext);
        v.setMaxWidth(1.79);
        v.setPrefWidth(555);
        AnchorPane ap = new AnchorPane(v);
        return ap;
    }
}
