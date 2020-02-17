package tom.sros;

import tom.sros.item.ItemDatabase;
import tom.sros.logIn.UserDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import tom.sros.storageRoom.BinDataBase;
import tom.sros.storageRoom.StorageRoomDatabase;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    String dataBaseName = ("SROSData.db");

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/tom/sros/logIn/logInScreen"), 640, 480);
        
        stage.setScene(scene);
        stage.show();
        
        UserDatabase.main(dataBaseName);
        ItemDatabase.main(dataBaseName);
        BinDataBase.createDatabase(dataBaseName);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}