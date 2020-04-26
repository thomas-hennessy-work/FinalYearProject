package tom.sros.home;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;
import tom.sros.item.AddItemToDataBaseController;
import tom.sros.item.LocateItemController;
import tom.sros.storageRoom.AddToStorageRoomController;

public class HomeScreenController {
    
    boolean UserType;
    
    public void userTypeData(boolean userType){
        UserType = userType;
    }
    
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
        AddItemToDataBaseController AITDBC = new AddItemToDataBaseController();
        AITDBC.userTypeData(UserType);
        App.setRoot("/tom/sros/item/addItemToDataBase");
    }
    
    @FXML
    private void addToStrgRoom() throws IOException{
        AddToStorageRoomController ATSRC = new AddToStorageRoomController();
        ATSRC.userTypeData(UserType);
        App.setRoot("/tom/sros/storageRoom/AddToStorageRoomScreen");
    }
    
    @FXML
    private void strgRoomMgmnt() throws IOException{
        App.setRoot("/tom/sros/storageRoom/storageRoomManagment");
    }
    
    @FXML
    private void locateItemScreen() throws IOException{
        LocateItemController LIC = new LocateItemController();
        LIC.userTypeData(UserType);
        App.setRoot("/tom/sros/item/locateItem");
    }
}