package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class PreloaderSceneController {

    Stage preloaderstage;

    @FXML
    private Label loadingstatus;

    public void setstage(Stage preloaderstage){
        this.preloaderstage = preloaderstage;
    }

    //TODO Drag window
    private double xpoint = 0;
    private double ypoint = 0;

    public void onMousePressed(MouseEvent event){
        xpoint = event.getSceneX();
        ypoint = event.getSceneY();
    }
    public void onMouseDragged(MouseEvent event){
        preloaderstage.setX(event.getSceneX()-xpoint);
        preloaderstage.setY(event.getScreenY()-ypoint);
    }


    public void beforeload() {
        loadingstatus.setText("Starting Application...");
    }

    public void beforeinit() {
        loadingstatus.setText("Fetching data and initializing connections...");
    }

    public void addtolable(String info){
        loadingstatus.setText(info);
    }
}
