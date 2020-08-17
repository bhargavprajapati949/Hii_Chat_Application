package source;

import static source.GlobleVariables.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectMysqlDatabase {

    public static Connection connect(){

        Connection sqlcon = null;

        if(IsConnectedToInternet.check()) {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                sqlcon = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/hiichatapp", "hiichatappuser", "q1w2e3r4t5");
                sqlstatement = sqlcon.createStatement();
                if(sqlcon.getMetaData().getTables(null, null, "USERINFO", null).next() == false){
                    sqlstatement.executeUpdate("create table USERINFO ( id int(100) PRIMARY KEY AUTO_INCREMENT , username varchar(30), name varchar(30), gender varchar(12), mobileno varchar(10), emailid varchar(50), password varchar(30), secque varchar(50), secqueans varchar(30));");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                //TODO driver not fount error
                iserror = true;
                errormsg += "Online database driver not found.\n";
            } catch (SQLException e) {
                e.printStackTrace();
                //TODO print error in scene
                iserror = true;
                errormsg += "Cannot connect to Server.\n";
            }
        }
        return sqlcon;
    }

}
