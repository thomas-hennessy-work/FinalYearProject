package tom.sros.storageRoom;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;

public class StorageRoomManagmentController {
    
    //Logout and home buttons
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        App.setRoot("/tom/sros/home/homeScreen");
    }
    
}
