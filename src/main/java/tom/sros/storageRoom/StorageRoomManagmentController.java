package tom.sros.storageRoom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
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
    private ListView amountList;
    @FXML
    private ListView widthList;
    @FXML
    private ListView lengthList;
    @FXML
    private ListView heightList;
    
    @FXML
    private TextField deleteBinText;
    
    @FXML
    private TableView binTable;
    
    List<Bin> toAddList = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){

        List<Bin> binInfoList = BinDataBase.getBinInfo(dataBaseName);
        binInfoList.forEach((currentBin) -> {
            binTable.getItems().add(currentBin);
            System.out.println(currentBin.getOccupied());
        });
    }
    
    @FXML void addToDB(){
      BinDataBase BDB = new BinDataBase();
      BDB.insertBins(toAddList, dataBaseName);
      toAddList.clear();
      amountList.getItems().clear();
      widthList.getItems().clear();
      lengthList.getItems().clear();
      heightList.getItems().clear();
      
      List<Bin> binInfoList = BinDataBase.getBinInfo(dataBaseName);
      binTable.getItems().clear();
        
        binInfoList.forEach((currentBin) -> {
            binTable.getItems().add(currentBin);
            System.out.println(currentBin.toString());
        });
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
        
        List<Bin> binInfoList = BinDataBase.getBinInfo(dataBaseName);
        binTable.getItems().clear();
        
        binInfoList.forEach((currentBin) -> {
            binTable.getItems().add(currentBin);
            System.out.println(currentBin.toString());
        });
    }
    
    @FXML void deleteBin(){
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
        binTable.getItems().clear();
        
        binInfoList.forEach((currentBin) -> {
            binTable.getItems().add(currentBin);
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
    
}
