package tom.sros.logIn;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.App;

public class LogInController {
    
    String dataBaseName = ("SROSData.db");
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passWordText;
    
    @FXML
    public void logInButton() throws IOException{
        //Gather text box inputs
        String userNameIn = userNameText.getText();
        String passWordIn = passWordText.getText();
        
        JFrame passwordWarning = new JFrame();
        
        //Check if they match any on record accounts
        System.out.println("Check if credentials are valid");
        if (UserDatabase.logInCheck(dataBaseName, userNameIn, passWordIn)){
            App.setRoot("/tom/sros/home/homeScreen");
        }
        else{
            passWordText.setText("");
            JOptionPane.showMessageDialog(passwordWarning, "User name or password not recognised.", "Unrecognised user", 2);
        }
    }
    
    public boolean logInCheck() {
       return true; 
    }
}
