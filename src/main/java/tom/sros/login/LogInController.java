package tom.sros.login;

import com.google.common.hash.Hashing;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    
    /**
     * Processes the user information and decides if and what access they should have
     * 
     * @throws IOException 
     */
    @FXML
    public void logInButton() throws IOException{
        //Gather text box inputs
        String userNameIn = userNameText.getText();
        String passWordIn = Hashing.sha256().hashString(passWordText.getText(),StandardCharsets.UTF_8).toString();
        
        JFrame passwordWarning = new JFrame();
        
        //Check if they match any on record accounts
        System.out.println("Check if credentials are valid");
        
        Boolean loginValue = UserDatabase.logInCheck(dataBaseName, userNameIn, passWordIn);
        
        //Log in check
        //Incorect log in details
        if(loginValue == null){
            passWordText.setText("");
            JOptionPane.showMessageDialog(passwordWarning, "User name or password not recognised.", "Unrecognised user", 2);
        }
        //Manager log in
        else if (loginValue){
            App.setManager(loginValue);
            App.setRoot("/tom/sros/home/homeScreen");
        }
        //employee log in
        else if(!loginValue){
            App.setManager(loginValue);
            App.setRoot("/tom/sros/home/homeScreenNonManager");
        }
    }
}
