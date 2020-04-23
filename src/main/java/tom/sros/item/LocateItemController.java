package tom.sros.item;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import tom.sros.App;
import tom.sros.sorter.Box;

public class LocateItemController implements Initializable{
    String dataBaseName = ("SROSData.db");
    
    @FXML
    private TableView boxTable;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateBoxTable();
    }
    
    @FXML
    private void removeBox(){
        
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
    
    private void populateBoxTable(){
        List<Box> boxInformation = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        boxInformation.forEach((currentBox)-> {
            boxTable.getItems().add(currentBox);
        });
    }
    
}
