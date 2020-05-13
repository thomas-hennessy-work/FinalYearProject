package tom.sros.home;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;

public class HomeScreenController {
    
    //Functions used in conjunction with FXML buttons to navigate to other pages
    //in the system.
    @FXML
    private void logOut() throws IOException{
        App.setRoot("/tom/sros/login/logInScreen");
    }
    
    @FXML
    private void userManagmentBtn() throws IOException{
        App.setRoot("/tom/sros/login/userManagment");
    }
    
    @FXML
    private void addToDBBtn() throws IOException{
        App.setRoot("/tom/sros/item/addItemToDataBase");
    }
    
    @FXML
    private void addToStrgRoom() throws IOException{
        App.setRoot("/tom/sros/storageRoom/addToStorageRoomScreen");
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