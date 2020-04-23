package tom.sros.storageRoom;

import tom.sros.sorter.newAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.App;
import tom.sros.item.ItemDatabase;
import tom.sros.sorter.Order;

public class AddToStorageRoomController {
    
    String dataBaseName = ("SROSData.db");
    //Lists used to store box ID's as they are input
    List<Pair> BoxIDs = new ArrayList<>();
    List<Order> OrderList = new ArrayList<>();
    
    //Lists to store order information as they are input
    
    @FXML
    private TextField IDBoxText;
    @FXML
    private TextField IDBoxAmount;
    
    @FXML
    private ListView IDBoxList;
    @FXML
    private ListView BoxAmountList;
    
    @FXML
    private TextField orderTypeID;
    @FXML
    private TextField addressText;
    @FXML
    private TextField custNameText;
    
    @FXML
    private ListView IDOrderList;
    @FXML
    private ListView CustNameList;
    @FXML
    private ListView AddressList;
    
    @FXML
    private void sortBoxes() throws IOException{
        //Sorting the list of items given to the system
        newAlgorithm NA = new newAlgorithm();
        System.out.println("Adding item to storage room");
        
        //Sorting and adding boxes to the storage room database
        NA.sortAndAddToDB(dataBaseName, BoxIDs);
        
        //Clearing the stored list of items
        BoxIDs.clear();
        IDBoxList.getItems().clear();
        BoxAmountList.getItems().clear();
    }
    
    @FXML
    private void addToBoxList(){
        ItemDatabase IDB = new ItemDatabase();
        JFrame boxExistanceWarning = new JFrame();
        
        //Gather the information in the two text feilds
        String inputID = IDBoxText.getText();
        int inputAmount = Integer.parseInt(IDBoxAmount.getText());
        
        //Ensure that the ID exists in the item database
        //If they do, pass them as a pair to a list in the backend to be temporeraly stored.
        if(IDB.IDCheck(dataBaseName,inputID) != false){
            IDBoxList.getItems().add(inputID);
            BoxAmountList.getItems().add(inputAmount);
            BoxIDs.add(new Pair(inputID,inputAmount));
            
            IDBoxText.setText("");
            IDBoxAmount.setText("");
        }
        else{
            JOptionPane.showMessageDialog(boxExistanceWarning, "Box ID dose not exist.", "Unrecognised box ID", 2);
        }
    }
    
    @FXML
    private void removeBox(){
        
    }
    
    @FXML
    private void sortOrders(){
        //Sorting the list of items given to the system
        ItemDatabase IDB = new ItemDatabase();
        System.out.println("Adding item to storage room");
        
        IDB.addOrdersToDB(dataBaseName, OrderList);
        
        OrderList.clear();
        IDOrderList.getItems().clear();
        CustNameList.getItems().clear();
        AddressList.getItems().clear();
    }
    
    @FXML
    private void addToOrderList(){
        ItemDatabase IDB = new ItemDatabase();
        JFrame boxExistanceWarning = new JFrame();
        
        String boxTypeID = orderTypeID.getText();
        String address = addressText.getText();
        String custName = custNameText.getText();
        
        if(IDB.IDCheck(dataBaseName, boxTypeID) != false){
            OrderList.add(new Order(boxTypeID, address, custName));
            
            IDOrderList.getItems().add(boxTypeID);
            CustNameList.getItems().add(custName);
            AddressList.getItems().add(address);
            
            orderTypeID.setText("");
            addressText.setText("");
            custNameText.setText("");
        }
        else{
            JOptionPane.showMessageDialog(boxExistanceWarning, "Box ID dose not exist.", "Unrecognised box ID", 2);
        }
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
