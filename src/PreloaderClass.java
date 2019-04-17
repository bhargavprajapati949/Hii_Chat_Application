import controllers.PreloaderSceneController;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class PreloaderClass extends Preloader {
    //deceleration
    private Stage preloaderstage;
    private Parent root;
    private PreloaderSceneController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        preloaderstage = primaryStage;

        //load preloaderScene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/PreloaderScene.fxml"));
        root = loader.load();
        Scene preloaderScene = new Scene(root);

        //Set stage propertie
        // /
        preloaderstage.setResizable(false);
        preloaderstage.initStyle(StageStyle.UNDECORATED);

        //set preloaderScene to stage
        preloaderstage.setScene(preloaderScene);
        preloaderstage.show();

        //get controller class
        controller = loader.getController();

        //pass stage to controller class
        controller.setstage(preloaderstage);
    }

    @FXML
    public void handleStateChangeNotification(Preloader.StateChangeNotification stateChangeNotification) {
        if(stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_LOAD){
            controller.beforeload();
        }
        else if(stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_INIT){
            controller.beforeinit();
        }
        else if (stateChangeNotification.getType() == Preloader.StateChangeNotification.Type.BEFORE_START) {
            preloaderstage.hide();
        }
    }

    public void handleApplicationNotification(PreloaderNotification info){
        source.ProgressNotification p = (source.ProgressNotification)info;
        controller.addtolable(p.getinfo());
    }
}
