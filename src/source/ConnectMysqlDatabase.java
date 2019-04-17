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
                sqlcon = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/k2HY7nUBqq", "k2HY7nUBqq", "3U3nSWhgkr");
                sqlstatement = sqlcon.createStatement();
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
