package tom.sros.logIn;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tom.sros.App;

public class userManagmentController {
    
    @FXML
    ListView<String> userList = new ListView<String>();
    ObservableList<String> items = FXCollections.observableArrayList();

    //Log out and home button
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        App.setRoot("/tom/sros/home/homeScreen");
    }
}