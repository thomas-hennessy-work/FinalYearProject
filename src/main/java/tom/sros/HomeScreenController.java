package tom.sros;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeScreenController {
    
    @FXML
    private void logOut() throws IOException{
          App.setRoot("logIn");
    }
    
    @FXML
    private void userManagmentBtn() throws IOException{
        App.setRoot("userManagment");
    }
    
    @FXML
    private void addToDBBtn() throws IOException{
        App.setRoot("AddToDataBase");
    }
    
    @FXML
    private void addToStrgRoom() throws IOException{
        App.setRoot("AddToStorageRoom");
    }
    
    @FXML
    private void strgRoomMgmnt() throws IOException{
        App.setRoot("storageRoomManagment");
    }
    
    @FXML
    private void locateItemScreen() throws IOException{
        App.setRoot("locateItem");
    }
}
