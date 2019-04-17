package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static source.GlobleVariables.*;

public class WelcomeSceneController {

    @FXML
    Button btngetstarted;

    @FXML
    private void clickedgetstarted() {
        primaryStage.setScene(signinScene);
    }

}
