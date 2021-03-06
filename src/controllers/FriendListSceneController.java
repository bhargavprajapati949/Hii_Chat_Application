package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static source.GlobleVariables.*;

public class FriendListSceneController implements Initializable {

    @FXML
    HBox title;

    @FXML
    Label titlelabel;

    @FXML
    MenuButton dropdownmenu;

    MenuItem addfriendmenuitem;
    MenuItem minimizemenuitem;
    MenuItem closemenuitem;
    MenuItem settingmenuitem;
    MenuItem signoutmenuitem;


    @FXML
    VBox friendnamevbox;

    @FXML
    Label notificationinfo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addfriendmenuitem = new MenuItem("Add Friend");
        minimizemenuitem = new MenuItem("Minimize app");
        closemenuitem = new MenuItem("Close app");
        settingmenuitem  = new MenuItem("Setting");
        signoutmenuitem = new MenuItem("Sign Out");


        dropdownmenu.getItems().addAll(addfriendmenuitem, minimizemenuitem, closemenuitem, settingmenuitem, signoutmenuitem);
        title.setHgrow(titlelabel, Priority.ALWAYS);

        reloadfriendlist();

        addfriendmenuitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader addfriendloader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddFriendScene.fxml"));
                    Scene addfriendscene = new Scene(addfriendloader.load());
                    Stage addfriendstage = new Stage();
                    addfriendstage.setScene(addfriendscene);
                    addfriendstage.initOwner(primaryStage);
                    addfriendstage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        minimizemenuitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setIconified(true);
            }
        });

        closemenuitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
                System.exit(0);
            }
        });

        settingmenuitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                settingSceneController.updatenotificationinfo();
                primaryStage.setScene(settingScene);
            }
        });

        signoutmenuitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String query;

                try {
                    query = "delete from USERINFO";
                    h2statement.executeUpdate(query);
                    query = "insert into USERINFO (islogin) values (0)";
                    h2statement.executeUpdate(query);

                    query = "select fusername from FRIENDLIST";
                    ResultSet rs = h2statement.executeQuery(query);
                    Statement h2statement2 = h2con.createStatement();

                    while(rs.next()){
                        query = "drop table MSG_" + rs.getString("fusername");
                        h2statement2.executeUpdate(query);
                    }

                    query = "drop table pandingmsg";
                    h2statement.executeUpdate(query);

                    query = "delete from FRIENDLIST";
                    h2statement.executeUpdate(query);
                    signinSceneController.clearall();
                    signinSceneController.setlablesignout();
                    dbobserver.stop();
                    primaryStage.setWidth(1000);
                    primaryStage.setHeight(700);
                    primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth())/2);
                    primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight())/2);
                    primaryStage.setScene(signinScene);
                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo handle sql exception
                }

            }
        });
    }


    public void reloadfriendlist(){
        friendnamevbox.getChildren().clear();
        try {
            ResultSet friendlist = h2statement.executeQuery("select * from FRIENDLIST");

            while(friendlist.next()){
                ImageView iv = new ImageView(new Image("images/userdefaultpic.png"));
                iv.setFitHeight(35);
                iv.setFitWidth(35);
                iv.setLayoutX(10);
                iv.setLayoutY(2.5);
                iv.setPickOnBounds(true);
                iv.setPreserveRatio(true);
                String s = friendlist.getString("fname");
                Label fname = new Label(s);
                fname.setId(friendlist.getString("fusername"));
                fname.setLayoutX(65);
                fname.setLayoutY(5.5);
                fname.setFont(Font.font(20.0));
                AnchorPane ap = new AnchorPane();
                ap.setPrefHeight(40.0);
                ap.setPrefWidth(398);
                ap.setStyle("-fx-background-color: #999999");
                ap.getChildren().addAll(iv, fname);
                friendnamevbox.getChildren().add(ap);

                ap.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        currentfriendname = fname.getText();
                        currentfriendusername = fname.getId();
                        chatSceneController.loadchatscene();
                        chatSceneController.updatenotificationinfo();
                        primaryStage.setScene(chatScene);
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle exception sql query problem
        }
    }

    public void updatenotificationinfo(){
        notificationinfo.setText(notificationtext);
    }

}
