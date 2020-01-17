package tom.sros.home;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;

public class HomeScreenController {
    
    //Functions for buttons to use to take the user to different screens.
    @FXML
    private void logOut() throws IOException{
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    
    @FXML
    private void userManagmentBtn() throws IOException{
        App.setRoot("/tom/sros/logIn/userManagment");
    }
    
    @FXML
    private void addToDBBtn() throws IOException{
        App.setRoot("/tom/sros/item/addItemToDataBase");
    }
    
    @FXML
    private void addToStrgRoom() throws IOException{
        App.setRoot("/tom/sros/storageRoom/AddToStorageRoomScreen");
    }
    
    @FXML
    private void strgRoomMgmnt() throws IOException{
        App.setRoot("/tom/sros/storageRoom/storageRoomManagment");
    }
    
    @FXML
    private void locateItemScreen() throws IOException{
        App.setRoot("/tom/sros/item/locateItem");
    }
}