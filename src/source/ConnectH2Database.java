package source;

import static source.GlobleVariables.*;

import org.h2.jdbc.JdbcSQLException;

import java.sql.*;

public class ConnectH2Database {
    public static Connection connect(){
        Connection h2con = null;

        // Loading driver

        //Connecting to database if exist
        try{
            Class.forName("org.h2.Driver");
            h2con = DriverManager.getConnection("jdbc:h2:~/'Hii - Chat App'/database;IFEXISTS=TRUE");
            h2statement = h2con.createStatement();
            return h2con;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            //TODO add proper exception handaling
            iserror = true;
            errormsg = "H2 database Driver cannot load. Reinstall application.\n";
        }
        catch (JdbcSQLException e){
        //throws exception when database is not exist
            h2con = createNewUserinfoDatabase();
        }
        catch (SQLException e) {
            e.printStackTrace();
            //todo handle exception
            iserror = true;
            errormsg += "Cannot connect to local database.\n";
        }

        return (h2con);
    }


    //creating new database
    private static Connection createNewUserinfoDatabase() {
        Connection h2con = null;

        try {
            h2con = DriverManager.getConnection("jdbc:h2:~/'Hii - Chat App'/database");
            h2statement = h2con.createStatement();

            //creating table
            h2statement.executeUpdate("create table USERINFO");


            //creating fields and initializing it.

            //login conformation
            h2statement.executeUpdate("alter table USERINFO ADD islogin INT");
            h2statement.executeUpdate("insert into USERINFO(islogin) values ( 0 )");

            //Personal Information
            h2statement.executeUpdate("alter table USERINFO ADD name varchar(30)");
            h2statement.executeUpdate("alter table USERINFO ADD username varchar(20)");
            h2statement.executeUpdate("alter table USERINFO ADD gender varchar(12)");
            h2statement.executeUpdate("alter table USERINFO ADD mobileno varchar(10)");
            h2statement.executeUpdate("alter table USERINFO ADD emailid varchar(30)");

            //friend list table
            h2statement.executeUpdate("create table FRIENDLIST");
            h2statement.executeUpdate("alter table FRIENDLIST add fname varchar (30)");
            h2statement.executeUpdate("alter table FRIENDLIST add fusername varchar (30)");
            h2statement.executeUpdate("alter table FRIENDLIST add gender varchar (12)");
            h2statement.executeUpdate("alter table FRIENDLIST add priority int (1000)");
            h2statement.executeUpdate("alter table FRIENDLIST add mobileno varchar (10)");
            h2statement.executeUpdate("alter table FRIENDLIST add emailid varchar (30)");

        } catch (SQLException e) {
            e.printStackTrace();
            //todo handle exception
            iserror = true;
            errormsg += "Local database already in use with other application.\nPlease close other application using local database.\nCannot use local database.";
        }

        return h2con;
    }
}
