package tom.sros.item;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tom.sros.App;
import tom.sros.sorter.Box;

public class AddItemToDataBaseController implements Initializable{
    
    String dataBaseName = ("SROSData.db");
    
    @FXML
    private TextField boxName;
    @FXML
    private TextField boxWidth;
    @FXML
    private TextField boxLength;
    @FXML
    private TextField boxHeight;
    @FXML
    private TextArea boxContents;
    @FXML
    private TextArea boxNotes;
    
    @FXML
    private TableView boxInfoTable;
    
    //Populates the box information table on load
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateTable();
    }
    
    /**
     * Takes the users inputs, creates a box type with them
     * and adds it to the box type table.
     * 
     * @throws IOException 
     */
    @FXML
    private void submitBox() throws IOException{
        //gathering the data input by the user
        String inBoxName = boxName.getText();
        String inBoxWidth = boxWidth.getText();
        String inBoxLength = boxLength.getText();
        String inBoxHeight = boxHeight.getText();
        String inBoxContents = boxContents.getText();
        String inBoxNotes = boxNotes.getText();
        
        //whilst float has been depreciated, not sure if double can be used in a database (untested)
        //converting string to float and adding the information to the database
        float floatBoxWidth = Float.parseFloat(inBoxWidth);
        float floatBoxLength = Float.parseFloat(inBoxLength);
        float floatBoxHeight = Float.parseFloat(inBoxHeight);
        ItemDatabase ITDB = new ItemDatabase();
        System.out.println("Add box to database");
        ITDB.addBoxType(dataBaseName,inBoxName,inBoxContents,floatBoxWidth,floatBoxLength,floatBoxHeight,inBoxNotes);
    
        populateTable();
        
        //empty input boxes
        boxName.setText("");
        boxWidth.setText("");
        boxLength.setText("");
        boxHeight.setText("");
        boxContents.setText("");
        boxNotes.setText("");
    }
    
    //Home and log out buttons
    @FXML
    private void logOut() throws IOException {
        App.setRoot("/tom/sros/logIn/logInScreen");
    }
    @FXML
    private void home() throws IOException{
            App.setRoot("/tom/sros/home/homeScreen");
    }
    
    //method for populating box type table
    private void populateTable(){
        List<Box> boxInformation = ItemDatabase.getDisplayBoxTypeInformation(dataBaseName);
        boxInformation.forEach((currentBox)-> {
            boxInfoTable.getItems().add(currentBox);
        });
    }
    
}
