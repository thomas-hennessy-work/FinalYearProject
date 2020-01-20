package tom.sros.item;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tom.sros.App;

public class AddItemToDataBaseController {
    
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
    private TextField boxID;
    @FXML
    private TextField orderName;
    @FXML
    private TextField orderWidth;
    @FXML
    private TextField orderLength;
    @FXML
    private TextField orderHeight;
    @FXML
    private TextArea orderContents;
    @FXML
    private TextArea orderNotes;
    @FXML
    private TextField orderCustName;
    @FXML
    private TextField orderCustAddress;
    @FXML
    private TextField orderID;
    
    @FXML
    private void submitBox() throws IOException{
        //gathering the data input by the user
        String inBoxName = boxName.getText();
        String inBoxWidth = boxWidth.getText();
        String inBoxLength = boxLength.getText();
        String inBoxHeight = boxHeight.getText();
        String inBoxContents = boxContents.getText();
        String inBoxNotes = boxNotes.getText();
        String inBoxID = boxID.getText();
        
        //whilst float has been depreciated, not sure if double can be used in a database (untested)
        //converting string to float and adding the information to the database
        float floatBoxWidth = Float.parseFloat(inBoxWidth);
        float floatBoxLength = Float.parseFloat(inBoxLength);
        float floatBoxHeight = Float.parseFloat(inBoxHeight);
        ItemDatabase ITDB = new ItemDatabase();
        System.out.println("Add box to database");
        ITDB.addBox(dataBaseName,inBoxID,inBoxName,inBoxContents,floatBoxWidth,floatBoxLength,floatBoxHeight,inBoxNotes);
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
    
}
