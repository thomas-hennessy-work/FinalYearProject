package tom.sros;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddToDataBaseController {
    
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
        String inBoxName = boxName.getText();
        String inBoxWidth = boxWidth.getText();
        String inBoxLength = boxLength.getText();
        String inBoxHeight = boxHeight.getText();
        String inBoxContents = boxContents.getText();
        String inBoxNotes = boxNotes.getText();
        String inBoxID = boxID.getText();
        
        //whilst float has been depreciated, not sure if double can be used in a database (untested)
        float floatBoxWidth = Float.parseFloat(inBoxWidth);
        float floatBoxLength = Float.parseFloat(inBoxLength);
        float floatBoxHeight = Float.parseFloat(inBoxHeight);
        ItemDatabase ITDB = new ItemDatabase();
        ITDB.addBox(inBoxID,inBoxName,inBoxContents,floatBoxWidth,floatBoxLength,floatBoxHeight,inBoxNotes);
    }
    
    @FXML
    private void logOut() throws IOException {
        App.setRoot("logIn");
    }
    
    @FXML
    private void home() throws IOException{
        App.setRoot("homeScreen");
    }
    
}
