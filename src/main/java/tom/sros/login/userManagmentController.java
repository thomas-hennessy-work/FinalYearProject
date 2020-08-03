package tom.sros.login;

import com.google.common.hash.Hashing;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tom.sros.App;
import javafx.scene.control.TextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class userManagmentController implements Initializable{
    
    String dataBaseName = ("SROSData.db");
    
    @FXML
    ListView<String> userList = new ListView<String>();
    ObservableList<String> items = FXCollections.observableArrayList();
    
    @FXML
    private TextField userIDInput;
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField passWordInput;
    @FXML
    private RadioButton radioManager;
    @FXML 
    private TextField deleteUserIDInput;
    
    @FXML
    private TableView userTable;
    
    /**
     * Populates the user table when initialized
     * 
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateTable();
    }
    
    /**
     * Gathers the information from the text feilds and uses them to create a user
     * 
     * @throws IOException 
     */
    @FXML
    private void createUser() throws IOException {
        UserDatabase UDB = new UserDatabase();
        JFrame userIDWarning = new JFrame();
        JFrame userNameWarning = new JFrame();
        
        if((UDB.userIDExists(dataBaseName, userIDInput.getText()) == false) && (UDB.userNameExists(dataBaseName, userNameInput.getText()) == false)){
            boolean isManager = radioManager.isSelected();
            UDB.populate(dataBaseName, userIDInput.getText(), userNameInput.getText(), isManager, Hashing.sha256().hashString(passWordInput.getText(),StandardCharsets.UTF_8).toString());
            
            //Testing the hashing
            //System.out.println(Hashing.sha256().hashString(passWordInput.getText(),StandardCharsets.UTF_8).toString());

            userIDInput.setText("");
            userNameInput.setText("");
            passWordInput.setText("");

            userTable.getItems().clear();
            populateTable();
        } else if(UDB.userIDExists(dataBaseName, userIDInput.getText()) == true) {
            JOptionPane.showMessageDialog(userIDWarning, "User with that ID already exists.", "ID exists", 2);
            userIDInput.setText("");
        } else if(UDB.userNameExists(dataBaseName, userNameInput.getText()) == true){
            JOptionPane.showMessageDialog(userNameWarning, "User with that user name already exists.", "Name esists", 2);
            userNameInput.setText("");
        }
    }
    
    /**
     * Removes a user from the user table
     * 
     * @throws IOException 
     */
    @FXML
    private void removeUser() throws IOException {
        UserDatabase UDB = new UserDatabase();
        UDB.removeUser(dataBaseName, deleteUserIDInput.getText());
        deleteUserIDInput.setText("");
        
        userTable.getItems().clear();
        populateTable();
    }

    //Log out and home button
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/login/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        App.setRoot("/tom/sros/home/homeScreen");
    }
    
    //Method for populating table
    private void populateTable(){
        UserDatabase UDB = new UserDatabase();
        List<User> userInfoList = UDB.getAllUsersNoPassword(dataBaseName);
        
        userInfoList.forEach((currentUser) -> {
            userTable.getItems().add(currentUser);
        });
    }
}