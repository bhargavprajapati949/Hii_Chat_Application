package source;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class IsConnectedToInternet {
    public static boolean check(){

        try {
            URL url;
            URLConnection con;

            //checking for google
            //url = new URL("https://www.google.com/");
            //con = url.openConnection();
            //con.getContent();

            //checking for mysql server
            url = new URL("https://remotemysql.com/");
            con = url.openConnection();
            con.getContent();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            //todo handle exception
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            //todo handle exception
            return false;
        }

        //using ping command in cmd
        //taking long time
        /*
        int x = 0;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ping www.google.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            x = p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(x == 1){
            return true;
        }
        else{
            return false;
        }
        */
    }
}
