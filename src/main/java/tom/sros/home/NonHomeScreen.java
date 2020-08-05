package tom.sros.home;

import java.io.IOException;
import javafx.fxml.FXML;
import tom.sros.App;

public class NonHomeScreen {
    //Home and log out buttons
    @FXML
    private void logOut() throws IOException {
        App.clearManagerStatus();
        App.setRoot("/tom/sros/login/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        if(App.getManager()){
            App.setRoot("/tom/sros/home/homeScreen");
        } else {
            App.setRoot("/tom/sros/home/homeScreenNonManager");
        }
    }
}
