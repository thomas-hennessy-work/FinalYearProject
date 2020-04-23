package tom.sros.storageRoom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import tom.sros.App;
import javafx.scene.control.TextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.sorter.Bin;

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
    private TableView existingBinTable;
    @FXML
    private TableView newBinTable;
    
    List<Bin> toAddList = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateTable();
    }
    
    @FXML 
    private void addToDB(){
      BinDataBase BDB = new BinDataBase();
      BDB.insertBins(toAddList, dataBaseName);
      toAddList.clear();
      newBinTable.getItems().clear();
      
      existingBinTable.getItems().clear();     
      populateTable();
    }
    
    @FXML
    private void addToList(){
        int amount = Integer.parseInt(addAmount.getText());
        float width = Float.parseFloat(addWidth.getText());
        float length = Float.parseFloat(addLength.getText());
        float height = Float.parseFloat(addHeight.getText());
        System.out.println("Amount: " + amount + " Width: " + width + " Length: " + length + " Height: " + height);
        
        Bin binToAdd = new Bin(amount, width, length, height);
        newBinTable.getItems().add(binToAdd);
        toAddList.add(binToAdd);
        
        addAmount.setText("");
        addWidth.setText("");
        addLength.setText("");
        addHeight.setText("");
    }
    
    @FXML
    private void removeBin(){
        TableViewSelectionModel binSelection = newBinTable.getSelectionModel();
        List<Bin> binsToRemove = binSelection.getSelectedItems();
        
        binsToRemove.forEach((currentBin) -> {
            toAddList.remove(currentBin);
        });
        
        newBinTable.getItems().removeAll(binsToRemove);
    }
    
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
        
        List<Bin> binInfoList = BinDataBase.getBinInfo(dataBaseName);
        existingBinTable.getItems().clear();
        
        binInfoList.forEach((currentBin) -> {
            existingBinTable.getItems().add(currentBin);
            System.out.println(currentBin.toString());
        });
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
    
    private void populateTable(){
        List<Bin> binInfoList = BinDataBase.getBinInfo(dataBaseName);
        binInfoList.forEach((currentBin) -> {
            existingBinTable.getItems().add(currentBin);
        });
    }
    
}
