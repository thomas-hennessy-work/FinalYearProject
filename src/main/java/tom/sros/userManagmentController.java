package tom.sros;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class userManagmentController {
    
    @FXML
    ListView<String> userList = new ListView<String>();
    ObservableList<String> items = FXCollections.observableArrayList();

    @FXML
    private void logOut() throws IOException {
        App.setRoot("logIn");
    }
    
    @FXML
    private void home() throws IOException{
        App.setRoot("homeScreen");
    }
}