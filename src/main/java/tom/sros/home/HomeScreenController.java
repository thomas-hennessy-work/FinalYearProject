package tom.sros.home;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;

public class HomeScreenController extends HomeScreenNonManagerController{

    //Functions that are not used in both home screens
    @FXML
    private void userManagmentBtn() throws IOException{
        App.setRoot("/tom/sros/login/userManagment");
    }

    @FXML
    private void strgRoomMgmnt() throws IOException{
        App.setRoot("/tom/sros/storageRoom/storageRoomManagment");
    }
}