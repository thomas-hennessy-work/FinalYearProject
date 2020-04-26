package tom.sros.item;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import tom.sros.App;
import tom.sros.home.HomeScreenController;
import tom.sros.sorter.Box;

public class LocateItemNonManagerController implements Initializable{
    String dataBaseName = ("SROSData.db");
    
    boolean UserType;
    
    @FXML
    private TableView boxTable;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateBoxTable();
    }
    
    public void userTypeData(boolean userType){
        UserType = userType;
    }
    
    @FXML
    private void removeBox(){
        TableViewSelectionModel boxesSelection = boxTable.getSelectionModel();
        List<Box> selectedBoxes = boxesSelection.getSelectedItems();
        ItemDatabase ITDB = new ItemDatabase();
        
        selectedBoxes.forEach((currentSelectedBox) -> {
            ITDB.removeStoredBox(currentSelectedBox, dataBaseName);
        });
        
        boxTable.getItems().clear();
        populateBoxTable();
    }
    
    //Logout and home buttons
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        App.setRoot("/tom/sros/home/homeScreenNonManager");
    }
    
    private void populateBoxTable(){
        List<Box> boxInformation = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        boxInformation.forEach((currentBox)-> {
            boxTable.getItems().add(currentBox);
        });
    }
    
}
