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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
        
        JFrame inputDimensionWarning = new JFrame();
        JFrame inputNameWarning = new JFrame();

        boolean dimensionValid = dimensionsValidation(inBoxWidth, inBoxLength, inBoxHeight);
        
        if(dimensionValid == true && !"".equals(inBoxName) && !" ".equals(inBoxName)){
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
        else if(dimensionValid == false){
            JOptionPane.showMessageDialog(inputDimensionWarning, "Dimensions are not valid, please enter valid dimensions", "Invalid dimensions", 1);
        }
        else {
            JOptionPane.showMessageDialog(inputNameWarning, "No name input. A name for the box is requiered", "Empty name", 1);
        }
    }
    
    //Home and log out buttons
    @FXML
    private void logOut() throws IOException {
        App.clearManagerStatus();
        App.setRoot("/tom/sros/login/logInScreen");
    }
    @FXML
    private void home() throws IOException{
        if(App.getManager()){
            App.setRoot("/tom/sros/home/homeScreen");
        } else {
            App.setRoot("/tom/sros/home/homeScreenNonManager");
        }
    }
    
    //method for populating box type table
    private void populateTable(){
        List<Box> boxInformation = ItemDatabase.getDisplayBoxTypeInformation(dataBaseName);
        boxInfoTable.getItems().clear();
        boxInformation.forEach((currentBox)-> {
            boxInfoTable.getItems().add(currentBox);
        });
    }
    
    private boolean dimensionsValidation(String width, String length, String height){
        boolean valid = true;
        
        //Validating dimensions are boolean values and are larger than 0
        try{
            int widthDecPos = width.indexOf(".");
            if(widthDecPos != -1 && (width.length() - widthDecPos) != 2){
                valid = false;
            }
            Float f = Float.parseFloat(width);
            if(f <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        
        try{
            int lengthDecPos = length.indexOf(".");
            if(lengthDecPos != -1 && (length.length() - lengthDecPos) != 2){
                valid = false;
            }
            Float f = Float.parseFloat(length);
            if(f <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        
        try{
            int heightDecPos = height.indexOf(".");
            if(heightDecPos != -1 && (height.length() - heightDecPos) != 2){
                valid = false;
            }
            Float f = Float.parseFloat(height);
            if(f <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        return valid;
    }
}
