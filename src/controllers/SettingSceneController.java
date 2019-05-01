package controllers;

import static source.GlobleVariables.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ResourceBundle;

public class SettingSceneController implements Initializable {

    @FXML
    ImageView btnback;

    @FXML
    AnchorPane myprofiletab;

    @FXML
    CheckBox alwaysontop;

    @FXML
    Label notificationinfo;

    @FXML
    void btnbackclicked(){
        friendlistSceneController.updatenotificationinfo();
        primaryStage.setScene(friendlistScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        myprofiletab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                profileSceneController.fatchpersonaldetail();
                profileSceneController.updatenotificationinfo();
                primaryStage.setScene(profileScene);
            }
        });

        alwaysontop.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    primaryStage.setAlwaysOnTop(true);
                }
                else if(oldValue){
                    primaryStage.setAlwaysOnTop(false);
                }
            }
        });
    }

    public void updatenotificationinfo(){
        notificationinfo.setText(notificationtext);
    }
}

