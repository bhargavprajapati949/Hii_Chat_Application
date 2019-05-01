package Observer;

import controllers.ChatSceneController;
import javafx.application.Platform;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static source.GlobleVariables.*;

public class DatabaseObserver extends Thread{

    Statement sqlstatement1;
    Statement h2statement1;
    ResultSet rs;

    public DatabaseObserver() throws SQLException {
        sqlstatement1 = sqlcon.createStatement();
        h2statement1 = h2con.createStatement();
        this.start();
    }


    @Override
    public void run(){
        String query = "select * from MSG_" + myusername;

        while(true){
            if(isconnectedtointernet && islogin) {
                try {
                    rs = sqlstatement1.executeQuery(query);
                    if (rs.next()) {
                        loadnewdatatodb(rs);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo handle sql exception
                }
            }
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                // todo handle thread exception
                e.printStackTrace();
            }
        }
    }

    public void loadnewdatatodb(ResultSet rs) throws SQLException {
        //code 1 for msg
        //code 2 for new friend request
        String msg_query;
        String delete_msg_query;
        String addtofriendlistlocal_query;
        String addtofriendlistserver_query;
        String createmsgtable_query;
        String deletefrommsgtable_query;
        do {
            if (rs.getInt("code") == 1) {
                //for msg
                msg_query = "insert into MSG_" + rs.getString("senderusername") + " (stime, sdate, senderusername, receiverusername, msg) values ('" + rs.getString("stime") + "', '" + rs.getString("sdate") + "', '" + rs.getString("senderusername") + "', '" + rs.getString("receiverusername") + "', '" + rs.getString("msg") + "')";
                delete_msg_query = "delete from MSG_" + myusername + " where msg='" + rs.getString("msg") + "'";
                h2statement1.executeUpdate(msg_query);
                sqlstatement.executeUpdate(delete_msg_query);
                if(rs.getString("senderusername").equals(currentfriendusername)){
                    Platform.runLater(() -> {
                        HBox msgbox = null;
                        try {
                            msgbox = chatSceneController.msgbox(rs.getString("msg"), 'r', rs.getString("stime"), rs.getString("sdate"), 2);
                            chatSceneController.msgcontainer.getChildren().add(msgbox);
                        } catch (SQLException e) {
                            //todo handle sqlexception
                            e.printStackTrace();
                        }
                    });
                    Platform.runLater(chatSceneController::loadchatscene);
                }
            }
            else if (rs.getInt("code") == 2) {
                //for add new friend
                addtofriendlistlocal_query = "insert into FRIENDLIST (fname, fusername, gender, mobileno, emailid) values ('" + rs.getString("fname") + "', '" + rs.getString("fusername") + "', '" + rs.getString("gender") + "', '" + rs.getString("mobileno") + "', '" + rs.getString("emailid") + "')";
                addtofriendlistserver_query = "insert into FRIENDLIST_" + myusername + " (fname, fusername, gender, mobileno, emailid) values ('" + rs.getString("fname") + "', '" + rs.getString("fusername") + "', '" + rs.getString("gender") + "', '" + rs.getString("mobileno") + "', '" + rs.getString("emailid") + "')";
                createmsgtable_query = "create table MSG_" + rs.getString("fusername") + " ( msgindex INT(255) UNSIGNED NOT NULL AUTO_INCREMENT , stime varchar (8), sdate varchar (8), senderusername varchar (30), receiverusername varchar (30), msg varchar (21000), sendconform int (2), PRIMARY KEY (msgindex))";
                deletefrommsgtable_query = "delete from MSG_" + myusername + " where fusername='" + rs.getString("fusername") + "'";
                sqlstatement.executeUpdate(addtofriendlistserver_query);
                h2statement1.executeUpdate(addtofriendlistlocal_query);
                h2statement1.executeUpdate(createmsgtable_query);
                sqlstatement.executeUpdate(deletefrommsgtable_query);
                Platform.runLater(friendlistSceneController::reloadfriendlist);
            }
        }while(rs.next());
    }
}
