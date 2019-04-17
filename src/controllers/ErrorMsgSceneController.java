package controllers;

import static source.GlobleVariables.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ErrorMsgSceneController {

    @FXML
    Button btnexit;

    @FXML
    Label errormsglabel;

    @FXML
    public void btnexitclicked(){
        System.exit(0);
    }

    public void seterrormsg(){
        errormsglabel.setText(errormsg);
    }

}
