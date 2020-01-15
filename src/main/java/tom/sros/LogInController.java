package tom.sros;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LogInController {
    
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passWordText;
    
    @FXML
    public void logInButton() throws IOException{
        String userNameIn = userNameText.getText();
        String passWordIn = passWordText.getText();
        userManagmentController UMC = new userManagmentController();
        
        if (LogInDatabase.logInCheck(userNameIn, passWordIn)){
            App.setRoot("homeScreen");
        }
    }
    
    public boolean logInCheck() {
       return true; 
    }
}
