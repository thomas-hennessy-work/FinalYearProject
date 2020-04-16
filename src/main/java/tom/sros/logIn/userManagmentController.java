package tom.sros.logIn;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tom.sros.App;
import javafx.scene.control.TextField;

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
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        UserDatabase UDB = new UserDatabase();
        List<user> userInfoList = UDB.getAllUsersNoPassword(dataBaseName);
        
        userInfoList.forEach((currentUser) -> {
            userTable.getItems().add(currentUser);
        });
    }
    
    @FXML
    private void createUser() throws IOException {
        UserDatabase UDB = new UserDatabase();
        boolean isManager = radioManager.isSelected();
        UDB.populate(dataBaseName, userIDInput.getText(), userNameInput.getText(), isManager, passWordInput.getText());
        userIDInput.setText("");
        userNameInput.setText("");
        passWordInput.setText("");
    }
    
    @FXML
    private void removeUser() throws IOException {
        UserDatabase UDB = new UserDatabase();
        UDB.removeUser(dataBaseName, deleteUserIDInput.getText());
        deleteUserIDInput.setText("");
        
        userTable.getItems().clear();
        List<user> userInfoList = UDB.getAllUsersNoPassword(dataBaseName);
        
        userInfoList.forEach((currentUser) -> {
            userTable.getItems().add(currentUser);
        });
    }

    //Log out and home button
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        App.setRoot("/tom/sros/home/homeScreen");
    }
}