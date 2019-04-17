import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import source.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static source.GlobleVariables.*;


public class SceneController extends Application {


    @Override
    public void init() throws IOException {

        //connect to database

        //embedded database
        h2con = ConnectH2Database.connect();
        if(iserror == true){
            return;
        }

        //checking for login
        ResultSet rs;
        try {
            Statement h2db = h2con.createStatement();
            rs = h2db.executeQuery("select islogin,username from USERINFO");
            rs.next();
            if(rs.getInt("islogin") == 0){
                islogin = false;
            }
            else{
                islogin = true;
                myusername = rs.getString("username");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle exception
            iserror = true;
            errormsg += "Cannot excess local database.\n";
            return;
        }

        //notify to preloader
        notifyPreloader(new ProgressNotification("Connecting to Server...")) ;

        //connect to server
        sqlcon = ConnectMysqlDatabase.connect();
        if(!islogin){
            if(sqlcon == null){
                iserror = true;
                errormsg = "Cannot connect to server.\n";
                return;
            }
        }
        else{
            if(sqlcon == null){
                notification += "Internet is off.\n";
                iserror = false;
                errormsg = "";
            }
        }

        //Fetching graphics data
        notifyPreloader(new ProgressNotification("Fetching graphics data..."));
        if(!islogin){
            if(!(IsConnectedToInternet.check())){
                iserror = true;
                errormsg += "Please turn on your internet connection.\n";
            }
            else {
                try{
                    FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("fxml/WelcomeScene.fxml"));
                    FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("fxml/SignupScene.fxml"));
                    FXMLLoader signinLoader = new FXMLLoader(getClass().getResource("fxml/SigninScene.fxml"));
                    FXMLLoader forgetpasswordLoader = new FXMLLoader(getClass().getResource("fxml/ForgetPasswordScene.fxml"));

                    welcomeScene = new Scene(welcomeLoader.load());
                    signupScene = new Scene(signupLoader.load());
                    signinScene = new Scene(signinLoader.load());
                    forgetPasswordScene = new Scene(forgetpasswordLoader.load());

                    welcomeSceneController = welcomeLoader.getController();
                    signupSceneController = signupLoader.getController();
                    signinSceneController = signinLoader.getController();
                    forgetPasswordSceneController = forgetpasswordLoader.getController();

                }
                catch (IOException e){
                    e.printStackTrace();
                    //todo handle exception
                    iserror = true;
                    errormsg = "Application source corrupted. Please reinstall application.\n";
                    return;
                }

            }
        }


        //load scene

        try {
            FXMLLoader friendlistLoader = new FXMLLoader(getClass().getResource("fxml/FriendlistScene.fxml"));
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("fxml/ChatScene.fxml"));

            friendlistScene = new Scene(friendlistLoader.load());
            chatScene = new Scene(chatLoader.load());


            friendlistSceneController = friendlistLoader.getController();
            chatSceneController = chatLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO fxml file error
            iserror = true;
            errormsg = "Application source corrupted. Please reinstall application.\n";
            return;
        }

        notifyPreloader(new ProgressNotification("Done."));
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        errorMsgStage = new Stage();
        FXMLLoader errorMsgLoader = new FXMLLoader(getClass().getResource("fxml/ErrorMsgScene.fxml"));
        errorMsgScene = new Scene(errorMsgLoader.load());
        errorMsgSceneController = errorMsgLoader.getController();
        errorMsgStage.setScene(errorMsgScene);

        if(iserror){
            ErrorMsg.showerror();
        }
        else {
            if(!islogin){

                primaryStage.setScene(welcomeScene);
            }
            else {
                primaryStage.setMinWidth(400);
                primaryStage.setMinHeight(500);
                primaryStage.setScene(friendlistScene);
            }
            primaryStage.show();
        }
    }
}
