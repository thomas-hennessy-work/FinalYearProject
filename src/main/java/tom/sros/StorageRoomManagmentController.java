package tom.sros;

import java.io.IOException;
import javafx.fxml.FXML;

public class StorageRoomManagmentController {
    
    @FXML
    private void logOut() throws IOException {
        App.setRoot("logIn");
    }
    
    @FXML
    private void home() throws IOException{
        App.setRoot("homeScreen");
    }
    
}
