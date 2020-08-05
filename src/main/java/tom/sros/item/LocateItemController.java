package tom.sros.item;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import tom.sros.home.NonHomeScreen;
import tom.sros.sorter.Box;
import tom.sros.sorter.CustOrder;

public class LocateItemController extends NonHomeScreen implements Initializable{
    String dataBaseName = ("SROSData.db");
    
    @FXML
    private TableView boxTable;
    @FXML
    private TableView orderTable;
    
    //Populate the box location table
    @Override
    public void initialize(URL location, ResourceBundle resources){
        populateBoxTable();
        populateOrderTable();
    }
    
    //Removes a selected box from the storage room
    @FXML
    private void removeBox(){
        TableViewSelectionModel boxesSelection = boxTable.getSelectionModel();
        List<Box> selectedBoxes = boxesSelection.getSelectedItems();
        ItemDatabase ITDB = new ItemDatabase();
        
        selectedBoxes.forEach((currentSelectedBox) -> {
            ITDB.removeStoredBox(currentSelectedBox, dataBaseName);
        });
        
        boxTable.getItems().clear();
        populateBoxTable();
    }
    
    @FXML
    private void removeOrder(){
        TableViewSelectionModel ordersSelected = orderTable.getSelectionModel();
        List<CustOrder> selectedOrders = ordersSelected.getSelectedItems();
        ItemDatabase ITDB = new ItemDatabase();
        
        selectedOrders.forEach((currentSelectedOrder) -> {
            System.out.println(currentSelectedOrder.getID());
            ITDB.removeStoredOrder(currentSelectedOrder, dataBaseName);
        });
        
        orderTable.getItems().clear();
        populateOrderTable();
    }
    
    //Method for filling the box location table
    private void populateBoxTable(){
        ItemDatabase ITDB = new ItemDatabase();
        List<Box> boxInformation = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        List<CustOrder> orderInformation = ITDB.getOrderInformationDisplay(dataBaseName);
        
        boxInformation.forEach((currentBox)-> {
            if(boxIsOrder(currentBox,orderInformation)){
                boxTable.getItems().add(currentBox);
            }
        });
    }
    
    private void populateOrderTable(){
        ItemDatabase ITDB = new ItemDatabase();
        List<CustOrder> orderInformation = ITDB.getOrderInformationDisplay(dataBaseName);
        orderInformation.forEach((currentOrder) -> {
            orderTable.getItems().add(currentOrder);
        });
    }
    
    private boolean boxIsOrder(Box reviewedBox, List<CustOrder> orderList){
        boolean returnValue = true;
        
        for(CustOrder currentOrder : orderList){
            if (currentOrder.getID().equals(reviewedBox.getID())){
                returnValue = false;
            } 
        }
        return returnValue;
    }
}
