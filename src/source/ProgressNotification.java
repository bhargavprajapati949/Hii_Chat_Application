package source;

import javafx.application.Preloader;

public class ProgressNotification implements Preloader.PreloaderNotification {
    public String info;

    public ProgressNotification(String info){
        this.info = info;
    }

    public String getinfo(){
        return (info);
    }
}