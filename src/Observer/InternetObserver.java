package Observer;

import source.IsConnectedToInternet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static source.GlobleVariables.*;

public class InternetObserver extends Thread{

    public InternetObserver(){
        this.start();

    }

    @Override
    public void run(){
        while(true){
            if(IsConnectedToInternet.check()){
                isconnectedtointernet = true;
                if(notificationtext.equals("You are offline.")){
                    notificationtext = "";
                    updatetoallscene();
                    sendpandigmsg();
                }
            }
            else{
                isconnectedtointernet = false;
                notificationtext = "You are offline.";
                updatetoallscene();
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //todo thread exception
            }
        }
    }

    void updatetoallscene(){
        chatSceneController.updatenotificationinfo();
        friendlistSceneController.updatenotificationinfo();
        profileSceneController.updatenotificationinfo();
        settingSceneController.updatenotificationinfo();
    }

    void sendpandigmsg(){
        String query = "select * from pandingmsg";
        ResultSet rs = null;
        Statement h2statement2;
        try {
            h2statement2 = h2con.createStatement();
            String savemsgtoserver_query = "insert into MSG_" + rs.getString("receiverusername").toUpperCase() + " (stime, sdate, senderusername, receiverusername, msg) values (" + rs.getString("stime") + ", " + rs.getString("sdate") + ", " + rs.getString("senderusername") + ", " + rs.getString("receiverusername") + ", " + rs.getString("msg") + ")";
            String setsendconform_query = "update MSG_" + rs.getString("receiverusername") + " ";
            rs = h2statement.executeQuery(query);
            while(rs.next()){
                sqlstatement.executeUpdate(savemsgtoserver_query);
                h2statement2.executeUpdate(setsendconform_query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle sqlexception
        }
    }
}
