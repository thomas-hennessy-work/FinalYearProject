package tom.sros.logIn;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import tom.sros.App;

public class LogInController {
    
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passWordText;
    
    @FXML
    public void logInButton() throws IOException{
        //Gather text box inputs
        String userNameIn = userNameText.getText();
        String passWordIn = passWordText.getText();
        
        //Check if they match any on record accounts
        System.out.println("Check if credentials are valid");
        if (LogInDatabase.logInCheck(userNameIn, passWordIn)){
            App.setRoot("/tom/sros/home/homeScreen");
        }
    }
    
    public boolean logInCheck() {
       return true; 
    }
}
