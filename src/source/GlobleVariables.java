package source;

import controllers.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class GlobleVariables {

    public static boolean islogin;

    public static Connection h2con;
    public static Connection sqlcon;

    public static Statement h2statement;
    public static Statement sqlstatement;

    public static String notification = "";
    public static Boolean iserror = false;
    public static String errormsg = "";

    public static Stage errorMsgStage;
    public static Scene errorMsgScene;
    public static ErrorMsgSceneController errorMsgSceneController;

    public static Stage primaryStage;

    public static Scene welcomeScene;
    public static Scene signupScene;
    public static Scene signinScene;
    public static Scene forgetPasswordScene;
    public static Scene friendlistScene;
    public static Scene chatScene;

    public static WelcomeSceneController welcomeSceneController;
    public static SignupSceneController signupSceneController;
    public static SigninSceneController signinSceneController;
    public static ForgetPasswordSceneController forgetPasswordSceneController;
    public static FriendListSceneController friendlistSceneController;
    public static ChatSceneController chatSceneController;

    public static String myusername;

    public static String currentfriendname;
    public static String currentfriendusername;
}