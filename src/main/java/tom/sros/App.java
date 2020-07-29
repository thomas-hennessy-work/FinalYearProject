package tom.sros;

import tom.sros.item.ItemDatabase;
import tom.sros.login.UserDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import tom.sros.storageRoom.BinDataBase;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    String dataBaseName = ("SROSData.db");
    static Boolean isManager;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/tom/sros/login/logInScreen"), 640, 480);
        
        stage.setScene(scene);
        stage.show();
        
        UserDatabase.main(dataBaseName);
        ItemDatabase.main(dataBaseName);
        BinDataBase.main(dataBaseName);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        InputStream IS = App.class.getResourceAsStream(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        return fxmlLoader.load(IS);
    }

    public static void main(String[] args) {
        launch();
    }
    
    //Manager status setters and getters
    public static void setManager(Boolean managerStatus){
        isManager = managerStatus;
    }
    public static Boolean getManager(){
        return isManager;
    }
    public static void clearManagerStatus(){
        isManager = null;
    }
}