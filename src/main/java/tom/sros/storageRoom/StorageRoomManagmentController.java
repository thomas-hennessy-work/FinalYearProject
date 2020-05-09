package tom.sros.storageRoom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import tom.sros.App;
import javafx.scene.control.TextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.item.ItemDatabase;
import tom.sros.sorter.Bin;
import tom.sros.sorter.Box;
import tom.sros.sorter.newAlgorithm;

public class StorageRoomManagmentController implements Initializable{
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
    private TextField deleteBinText;
    
    @FXML
    private TextField deleteSortText;
    
    @FXML
    private TableView existingBinTable;
    @FXML
    private TableView newBinTable;
    @FXML
    private TableView sortDeleteTable;
    @FXML
    private TableView unsortedTable;
    
    List<Bin> toAddList = new ArrayList<>();
    List<Bin> toRemoveList = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateBinTable();
        populateUnsortedTable();
    }
    
    /**
     * Adds bins to the bin individual table
     */
    @FXML 
    private void addToDB(){
      BinDataBase BDB = new BinDataBase();
      
      BDB.insertBins(toAddList, dataBaseName);
      
      toAddList.clear();
      newBinTable.getItems().clear();
      
      existingBinTable.getItems().clear();     
      populateBinTable();
    }
    
    /**
     * Adds a bin to the list of bins ready to be added to the database
     */
    @FXML
    private void addToList(){
        String inAmount = addAmount.getText();
        String inWidth = addWidth.getText();
        String inLength = addLength.getText();
        String inHeight = addHeight.getText();
        
        JFrame inputDimensionWarning = new JFrame();
        JFrame inputAmoutnWarning = new JFrame();
        
        boolean dimensionValid = dimensionsValidation(inWidth, inLength, inHeight);
        boolean amountValid = amountValidation(inAmount);

        if(dimensionValid == true && amountValid == true){
            int amount = Integer.parseInt(inAmount);
            float width = Float.parseFloat(inWidth);
            float length = Float.parseFloat(inLength);
            float height = Float.parseFloat(inHeight);
            
            Bin binToAdd = new Bin(amount, width, length, height);
            newBinTable.getItems().add(binToAdd);
            toAddList.add(binToAdd);

            addAmount.setText("");
            addWidth.setText("");
            addLength.setText("");
            addHeight.setText("");
        }
        else if(dimensionValid == false){
            JOptionPane.showMessageDialog(inputDimensionWarning, "Dimensions are not valid, please enter valid dimensions", "Invalid dimensions", 1);
        }
        else if(amountValid == false){
            JOptionPane.showMessageDialog(inputAmoutnWarning, "Amount is not valid, please enter a valid amount", "Invalid amount", 1);
        }
    }
    
    /**
     * Removes a bin from the list of bins ready to be added
     */
    @FXML
    private void removeBin(){
        TableViewSelectionModel binSelection = newBinTable.getSelectionModel();
        List<Bin> binsToRemove = binSelection.getSelectedItems();
        
        binsToRemove.forEach((currentBin) -> {
            toAddList.remove(currentBin);
        });
        
        newBinTable.getItems().removeAll(binsToRemove);
    }
    
    /**
     * Deletes a bin from the din individual table, only if the bin is empty
     */
    @FXML 
    private void deleteBin(){
        String deleteID = deleteBinText.getText();
        
        JFrame binExistanceWarning = new JFrame();
        JFrame binOccupiedWarning = new JFrame();
        
        //Checks the existance of the bin
        if(BinDataBase.binIDCheck(dataBaseName, deleteID) == false){
            JOptionPane.showMessageDialog(binExistanceWarning, "Bin with that ID dose not exist.", "Unrecognised bin", 2);
        }
        //Checks if the bin is occupied
        else if(BinDataBase.getStoredBoxesAndIndividualize(dataBaseName, (new Bin(deleteID))).getOccupied()){
            JOptionPane.showMessageDialog(binOccupiedWarning, "The bin selected for deletion is occupied.", "Occupied bin", 2);
        }
        else{
            BinDataBase.deleteBin(deleteID, dataBaseName);
        }
        
        existingBinTable.getItems().clear();
        
        populateBinTable();
    }
    
    /**
     * Adds a bin to the list of bins to be removed when the full storage room sort is executed
     */
    @FXML
    private void addDeleteSortTable(){
        String deleteSortID = deleteSortText.getText();
        
        JFrame binExistanceWarning = new JFrame();
        JFrame binNotOccupiedWarning = new JFrame();
        
        //Checks the existance of the bin
        if(BinDataBase.binIDCheck(dataBaseName, deleteSortID) == false){
            JOptionPane.showMessageDialog(binExistanceWarning, "Bin with that ID dose not exist.", "Unrecognised bin", 2);
        }
        //Checks if the bin is occupied, if not, it is recomended to delete it from the remove bins tab
        else if(!(BinDataBase.getStoredBoxesAndIndividualize(dataBaseName, (new Bin(deleteSortID))).getOccupied())){
            JOptionPane.showMessageDialog(binNotOccupiedWarning, "The bin selected for deletion is not occupied. It can be deleted from the remove bins tab.", "Occupied bin", 2);
        }
        else{
            sortDeleteTable.getItems().add(new Bin(deleteSortID));
            toRemoveList.add(new Bin(deleteSortID));
        }
    }
    
    /**
     * Resorts all the bins within the system, removing any specified bins
     */
    @FXML
    private void reSortBins(){
        newAlgorithm NA = new newAlgorithm();
        ItemDatabase ITDB = new ItemDatabase();
        
        //Obtains the information about all boxes
        List<Box> allBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        ITDB.clearBoxLocationData(dataBaseName);
        
        //Puts all boxes through the sorting algorithm, minus the bins specified
        NA.sortAndAddToDB(dataBaseName, allBoxes, toRemoveList);
        
        //Removes any bins from the database that were specified
        toRemoveList.forEach((currentBin) -> {
            BinDataBase.deleteBin(currentBin.getName(), dataBaseName);
        });
    }
    
    @FXML
    private void addUnsortedBoxes(){
        ItemDatabase ITDB = new ItemDatabase();
        newAlgorithm NA = new newAlgorithm();
        
        List<Box> boxUnsortedList = ITDB.getUnsortedBoxDisplay(dataBaseName);
        List<Bin> emptyLisy = new ArrayList<>();
        
        ITDB.emptyUnsortedTable(dataBaseName);
        NA.sortAndAddToDB(dataBaseName, boxUnsortedList, emptyLisy);
        
        unsortedTable.getItems().clear();
        populateUnsortedTable();
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
    
    /**
     * Populates the table of existing bins
     */
    private void populateBinTable(){
        List<Bin> binInfoList = BinDataBase.getBinInfo(dataBaseName);
        binInfoList.forEach((currentBin) -> {
            existingBinTable.getItems().add(currentBin);
        });
    }
    
    private void populateUnsortedTable(){
        ItemDatabase ITDB = new ItemDatabase();
        List<Box> boxInfoList =  ITDB.getUnsortedBoxDisplay(dataBaseName);
        boxInfoList.forEach((currentBox) -> {
            unsortedTable.getItems().add(currentBox);
        });
    }
    
     private boolean dimensionsValidation(String width, String length, String height){
        boolean valid = true;
        
        //Validating dimensions are boolean values and are larger than 0
        try{
            Float f = Float.parseFloat(width);
            if(f <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        try{
            Float f = Float.parseFloat(length);
            if(f <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        try{
            Float f = Float.parseFloat(height);
            if(f <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        return valid;
    }
     
     private boolean amountValidation(String amount){
        boolean valid = true;
        
        //Validating dimensions are boolean values and are larger than 0
        try{
            int i = Integer.parseInt(amount);
            if(i <= 0){
                valid = false;
            }
        } catch (NumberFormatException NFE){
            valid = false;
        }
        return valid;
    }
}
