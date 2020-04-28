package tom.sros.storageRoom;

import tom.sros.sorter.newAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.App;
import tom.sros.item.ItemDatabase;
import tom.sros.sorter.Bin;
import tom.sros.sorter.Box;
import tom.sros.sorter.Order;

public class AddToStorageRoomNonManagerController {
    
    String dataBaseName = ("SROSData.db");
    
    boolean UserType;
    
    //Lists used to store box ID's as they are input
    List<Box> BoxIDs = new ArrayList<>();
    List<Order> OrderList = new ArrayList<>();
    
    @FXML
    private TextField IDBoxText;
    @FXML
    private TextField IDBoxAmount;
    
    @FXML
    private TextField orderTypeID;
    @FXML
    private TextField addressText;
    @FXML
    private TextField custNameText;
    
    @FXML
    private TableView itemTable;
    @FXML
    private TableView orderTable;
    
    /**
     * Gets the boxes in the list and puts them through the sorting algorithm
     */
    @FXML
    private void sortBoxes() throws IOException{
        //Sorting the list of items given to the system
        newAlgorithm NA = new newAlgorithm();
        System.out.println("Adding item to storage room");
        
        //Sorting and adding boxes to the storage room database
        List<Bin> emptyBin = null;
        NA.sortAndAddToDB(dataBaseName, BoxIDs, emptyBin);
        
        //Clearing the stored list of items
        BoxIDs.clear();
        itemTable.getItems().clear();
        orderTable.getItems().clear();
    }
    
    /**
     * Adds a box (or boxes) to the list of boxes prepared to be added
     * to the box location table.
     */
    @FXML
    private void addToBoxList(){
        ItemDatabase IDB = new ItemDatabase();
        JFrame boxExistanceWarning = new JFrame();
        
        //Gather the information in the two text feilds
        String inputID = IDBoxText.getText();
        int inputAmount = Integer.parseInt(IDBoxAmount.getText());
        
        //Ensure that the ID exists in the item database
        //If they do, pass them as a pair to a list in the backend to be temporeraly stored.
        if(IDB.IDCheckBoxType(dataBaseName,inputID) != false){
            Box inputBox = new Box(inputID, inputAmount);
            BoxIDs.add(inputBox);
            itemTable.getItems().add(inputBox);
            
            IDBoxText.setText("");
            IDBoxAmount.setText("");
        }
        else{
            JOptionPane.showMessageDialog(boxExistanceWarning, "Box ID dose not exist.", "Unrecognised box ID", 2);
        }
    }
    
    /**
     * Removes a box from the list of boxes to be added
     */
    @FXML
    private void removeBox(){
        TableViewSelectionModel boxesSelection = itemTable.getSelectionModel();
        List<Box> selectedBoxes = boxesSelection.getSelectedItems();
        
        selectedBoxes.forEach((currentBox) -> {
            BoxIDs.remove(currentBox);
        });
        
        itemTable.getItems().removeAll(selectedBoxes);
    }
    
    /**
     * Gets the orders in the list and puts them through the sorting algorithm
     */
    @FXML
    private void sortOrders(){
        //Sorting the list of items given to the system
        ItemDatabase IDB = new ItemDatabase();
        System.out.println("Adding item to storage room");
        
        IDB.addOrdersToDB(dataBaseName, OrderList);
        
        OrderList.clear();
        orderTable.getItems().clear();
    }
    
    /**
     * Adds an order to the order list with the information provided by the user
     */
    @FXML
    private void addToOrderList(){
        ItemDatabase IDB = new ItemDatabase();
        JFrame boxExistanceWarning = new JFrame();
        
        String boxTypeID = orderTypeID.getText();
        String address = addressText.getText();
        String custName = custNameText.getText();
        
        if(IDB.IDCheckBoxType(dataBaseName, boxTypeID) != false){
            Order inputOrder = new Order(boxTypeID, address, custName);
            OrderList.add(inputOrder);
            
            orderTable.getItems().add(inputOrder);
            
            orderTypeID.setText("");
            addressText.setText("");
            custNameText.setText("");
        }
        else{
            JOptionPane.showMessageDialog(boxExistanceWarning, "Box ID dose not exist.", "Unrecognised box ID", 2);
        }
    }
    
    /**
     * Removes an order from the order list
     */
    @FXML
    private void removeOrder(){
        TableViewSelectionModel ordersSelection = orderTable.getSelectionModel();
        List<Order> selectedOrders = ordersSelection.getSelectedItems();

        selectedOrders.forEach((currentOrder) -> {
            OrderList.remove(currentOrder);
        });
        
        orderTable.getItems().removeAll(selectedOrders);
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
}
