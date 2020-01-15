package tom.sros;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddToStorageRoomController {
    
    @FXML
    private TextField IDText;
    
    @FXML
    private void logOut() throws IOException {
        App.setRoot("logIn");
    }
    
    @FXML
    private void home() throws IOException{
        App.setRoot("homeScreen");
    }
    
    @FXML
    private void submit() throws IOException{
        String ID = IDText.getText();
        System.out.println(ID);
        ItemDatabase IDB = new ItemDatabase();
        StorageRoomDatabase SRDB = new StorageRoomDatabase();
        
        if(IDB.IDCheck(ID) != "missing"){
            SRDB.addToStorageRoom(ID);
        }
    }
}
