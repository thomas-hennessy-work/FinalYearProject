package tom.sros.logIn;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.App;

import java.text.DecimalFormat;

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
        String passWordIn = passWordText.getText();
        
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
            App.setRoot("/tom/sros/home/homeScreen");
        }
        //employee log in
        else if(!loginValue){
            App.setRoot("/tom/sros/home/homeScreenNonManager");
        }
    }
}
