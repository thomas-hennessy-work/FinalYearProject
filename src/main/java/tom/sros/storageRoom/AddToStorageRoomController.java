package tom.sros.storageRoom;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tom.sros.App;
import tom.sros.item.ItemDatabase;

public class AddToStorageRoomController {
    
    String dataBaseName = ("SROSData.db");
    @FXML
    private TextField IDText;
    
    
    @FXML
    private void submit() throws IOException{
        //Gather the ID the user submited
        String ID = IDText.getText();
        ItemDatabase IDB = new ItemDatabase();
        StorageRoomDatabase SRDB = new StorageRoomDatabase();
        
        //If the ID exists in the database, add it to the storage room
        System.out.println("Adding item to storage room");
        if(IDB.IDCheck(dataBaseName,ID) != "missing"){
            SRDB.addToStorageRoom(dataBaseName, ID);
        }
    }
    
    
    //Logout and home buttons
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        App.setRoot("/tom/sros/home/homeScreen");
    }
}
