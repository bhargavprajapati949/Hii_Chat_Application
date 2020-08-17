package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import static source.GlobleVariables.*;

public class WelcomeSceneController {

    @FXML
    Button btngetstarted;

    @FXML
    Label btnclose;

    @FXML
    private void clickedgetstarted() {
        primaryStage.setScene(signinScene);
    }

    @FXML
    void btncloseclicked(){
        primaryStage.close();
        System.exit(0);
    }

}
