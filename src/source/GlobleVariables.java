package source;

import Observer.DatabaseObserver;
import Observer.InternetObserver;
import controllers.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class GlobleVariables {

    public static boolean islogin;
    public static boolean isconnectedtointernet = false;

    public static Connection h2con;
    public static Connection sqlcon;

    public static Statement h2statement;
    public static Statement sqlstatement;

    public static String notificationtext = "";
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
    public static Scene settingScene;
    public static Scene profileScene;

    public static WelcomeSceneController welcomeSceneController;
    public static SignupSceneController signupSceneController;
    public static SigninSceneController signinSceneController;
    public static ForgetPasswordSceneController forgetPasswordSceneController;
    public static FriendListSceneController friendlistSceneController;
    public static ChatSceneController chatSceneController;
    public static SettingSceneController settingSceneController;
    public static ProfileSceneController profileSceneController;

    public static String myusername;

    public static String currentfriendname;
    public static String currentfriendusername;

    public static InternetObserver io;
    public static DatabaseObserver dbobserver;
}
