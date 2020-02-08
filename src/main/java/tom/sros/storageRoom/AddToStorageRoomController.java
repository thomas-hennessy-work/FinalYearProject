package tom.sros.storageRoom;

import com.github.skjolber.packing.BoxItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import tom.sros.App;
import tom.sros.item.ItemDatabase;

public class AddToStorageRoomController {
    
    String dataBaseName = ("SROSData.db");
    //Lists used to store ID's as they are input
    List<Pair> IDs = new ArrayList<Pair>();
    
    @FXML
    private TextField IDText;
    @FXML
    private TextField IDAmount;
    
    @FXML
    private ListView IDList;
    @FXML
    private ListView AmountList;
    
    @FXML
    private void sort() throws IOException{
        //Sorting the list of items given to the system
        StorageRoomDatabase SRDB = new StorageRoomDatabase();
        
        System.out.println("Adding item to storage room");
        SRDB.sortAndAddToStorageRoom(dataBaseName, IDs);
        
        //Clearing the stored list of items
        IDs = null;
    }
    
    @FXML
    private void addToList(){
        ItemDatabase IDB = new ItemDatabase();
        
        //Gather the information in the two text feilds
        String inputID = IDText.getText();
        int inputAmount = Integer.parseInt(IDAmount.getText());
        
        //Ensure that the ID exists in the item database
        //If they do, pass them as a pair to a list in the backend to be temporeraly stored.
        if(IDB.IDCheck(dataBaseName,inputID) != false){
            IDList.getItems().add(inputID);
            IDs.add(new Pair(inputID,inputAmount));
            
            IDText.setText("");
            IDAmount.setText("");
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
