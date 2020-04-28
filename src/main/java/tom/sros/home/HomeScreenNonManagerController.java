package tom.sros.home;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;

public class HomeScreenNonManagerController {
    
    boolean UserType;
    
    public void userTypeData(boolean userType){
        UserType = userType;
    }
    
    //Functions used in conjunction with FXML buttons to navigate to other pages
    //in the system.
    @FXML
    private void logOut() throws IOException{
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    
    @FXML
    private void addToDBBtn() throws IOException{
        App.setRoot("/tom/sros/item/addItemToDataBaseNonManager");
    }
    
    @FXML
    private void addToStrgRoom() throws IOException{
        App.setRoot("/tom/sros/storageRoom/AddToStorageRoomScreenNonManager");
    }
    
    @FXML
    private void locateItemScreen() throws IOException{
        App.setRoot("/tom/sros/item/locateItemNonManager");
    }
}