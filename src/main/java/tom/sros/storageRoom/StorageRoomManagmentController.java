package tom.sros.storageRoom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tom.sros.App;
import javafx.scene.control.TextField;
import tom.sros.sorter.Bin;

public class StorageRoomManagmentController {
    String dataBaseName = ("SROSData.db");
    
    @FXML
    private TextField addAmount;
    @FXML
    private TextField addWidth;
    @FXML
    private TextField addLength;
    @FXML
    private TextField addHeight;
    
    @FXML 
    private ListView amountList;
    @FXML
    private ListView widthList;
    @FXML
    private ListView lengthList;
    @FXML
    private ListView heightList;
    
    List<Bin> toAddList = new ArrayList<>();
    
    @FXML void addToDB(){
      BinDataBase BDB = new BinDataBase();
      BDB.insertBins(toAddList, dataBaseName);
      toAddList.clear();
      amountList.getItems().clear();
      widthList.getItems().clear();
      lengthList.getItems().clear();
      heightList.getItems().clear();
    }
    
    @FXML void addToList(){
        int amount = Integer.parseInt(addAmount.getText());
        float width = Float.parseFloat(addWidth.getText());
        float length = Float.parseFloat(addLength.getText());
        float height = Float.parseFloat(addHeight.getText());
        System.out.println("Amount: " + amount + " Width: " + width + " Length: " + length + " Height: " + height);
        
        amountList.getItems().add(amount);
        widthList.getItems().add(width);
        lengthList.getItems().add(length);
        heightList.getItems().add(height);

        toAddList.add(new Bin(width, length, height, amount));
        
        addAmount.setText("");
        addWidth.setText("");
        addLength.setText("");
        addHeight.setText("");
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
