package tom.sros.sorter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tom.sros.item.ItemDatabase;
import tom.sros.storageRoom.BinDataBase;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class newAlgorithmJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    static ItemDatabase ITDB = new ItemDatabase();
    static BinDataBase BDB = new BinDataBase();
    static binaryTreeAlgorithm NA = new binaryTreeAlgorithm();
    
    static Bin binType1 = new Bin("1", (float)10, (float)10, (float)10);
    
    static BoxType boxType1 = new BoxType("1", "Mice", (float)5, (float)5, (float)5, "10 mice", "do not drop");

    static BoxType addBox1 = new BoxType("1", 4);
    
    static CustOrder order1 = new CustOrder("1", "DMU", "Thomas");
    static CustOrder order2 = new CustOrder("1", "Leicester", "Patrick");
    
    public newAlgorithmJUnitTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();

            String sql = ("DROP TABLE IF EXISTS orderList");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxLocation");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxType");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS unSortedBoxes");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS emptySpace");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS binType");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS binIndividual");
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        ItemDatabase.main(dataBaseName);
        BinDataBase.main(dataBaseName);
        
        List<Bin> testBins = new ArrayList<>();
        binType1.setAmount(4);
        testBins.add(binType1);
        
        ITDB.addBoxType(dataBaseName, boxType1.getName(), boxType1.getContents(), boxType1.getWidth(), boxType1.getLength(), boxType1.getHeight(), boxType1.getNotes());
        BDB.insertBins(testBins, dataBaseName);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    public void addPerfectFit(){
        List<Bin> emptyList = new ArrayList<>();
        List<BoxType> addBoxes = new ArrayList<>();
        
        addBoxes.add(addBox1);
        
        NA.sortAndAddToDB(dataBaseName, addBoxes, emptyList);
    }
    
    @Test
    @Order(1)
    public void testPerfectFitX(){
        addPerfectFit();
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals((float) 5, returnedBoxes.get(0).getX(), "X location should match");
        assertEquals((float) 0, returnedBoxes.get(1).getX(), "X location should match");
        assertEquals((float) 5, returnedBoxes.get(2).getX(), "X location should match");
        assertEquals((float) 0, returnedBoxes.get(3).getX(), "X location should match");
    }
    
    @Test
    @Order(2)
    public void testPerfectFitY(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals((float) 0, returnedBoxes.get(0).getY(), "Y location should be 0");
        assertEquals((float) 0, returnedBoxes.get(1).getY(), "Y location should be 0");
        assertEquals((float) 0, returnedBoxes.get(2).getY(), "Y location should be 0");
        assertEquals((float) 0, returnedBoxes.get(3).getY(), "Y location should be 0");
    }
    
    @Test
    @Order(2)
    public void testPerfectFitZ(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals((float) 5, returnedBoxes.get(0).getZ(), "Z location should match");
        assertEquals((float) 5, returnedBoxes.get(1).getZ(), "Z location should match");
        assertEquals((float) 0, returnedBoxes.get(2).getZ(), "Z location should match");
        assertEquals((float) 0, returnedBoxes.get(3).getZ(), "Z location should match");
    }
    
    @Test
    @Order(2)
    public void testPerfectFitID(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals("1", returnedBoxes.get(0).getID(), "ID should match");
        assertEquals("2", returnedBoxes.get(1).getID(), "ID should match");
        assertEquals("3", returnedBoxes.get(2).getID(), "ID  should match");
        assertEquals("4", returnedBoxes.get(3).getID(), "ID should match");
    }
    
    @Test
    @Order(3)
    public void testRemoveItem(){
         List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
         ITDB.removeStoredBox(returnedBoxes.get(1), dataBaseName);
         List<BoxIndividual> returnedBoxesUpdated = ItemDatabase.getBoxLocationDisplay(dataBaseName);
         
         if(returnedBoxesUpdated.size() != 3){
             fail("Box should have been removed");
         }
         
         List<EmptySpace> spaceList = ITDB.getEmptySpaces("1", dataBaseName);
         assertEquals((float)0, spaceList.get(0).getX(), "X position should be 5");
         assertEquals((float)0, spaceList.get(0).getY(), "Y position should be 0");
         assertEquals((float)5, spaceList.get(0).getZ(), "Z position should be 0");
    }
    
    @Test
    @Order(4)
    public void testReAddBoxes(){
        List<Bin> emptyList = new ArrayList<>();
        List<BoxType> addBoxes = new ArrayList<>();
        
        addBox1.setAmount(2);
        addBoxes.add(addBox1);
        
        NA.sortAndAddToDB(dataBaseName, addBoxes, emptyList);
        
        List<EmptySpace> spaceList = ITDB.getEmptySpaces("1", dataBaseName);
        if(!spaceList.isEmpty()){
            fail("The empty space should have been removed");
        }
         
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        if(returnedBoxes.size() != 5){
            fail("Not all boxes have been added");
        }
    }
    
    @Test
    @Order(5)
    public void testBoxesReAddedX(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals((float)0, returnedBoxes.get(3).getX());
        assertEquals((float)0, returnedBoxes.get(4).getX());
    }
    
    @Test
    @Order(5)
    public void testBoxesReAddedY(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals((float)0, returnedBoxes.get(3).getY());
        assertEquals((float)0, returnedBoxes.get(4).getY());
    }
    
    @Test
    @Order(5)
    public void testBoxesReAddedZ(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals((float)5, returnedBoxes.get(3).getZ());
        assertEquals((float)0, returnedBoxes.get(4).getZ());
    }
    
    @Test
    @Order(5)
    public void testBoxesReAddedBin(){
        List<BoxIndividual> returnedBoxes = ItemDatabase.getBoxLocationDisplay(dataBaseName);
        
        assertEquals("1", returnedBoxes.get(3).getBin());
        assertEquals("2", returnedBoxes.get(4).getBin());
    }
    
    @Test
    @Order(6)
    public void testAddOrdersToDB(){
        List<CustOrder> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        
        ITDB.addOrdersToDB(dataBaseName, orderList);
        
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        if(returnedOrders.size() != 2){
            fail("2 orders should have been added");
        }
    }
    
    @Test
    @Order(7)
    public void testAddOrdersToDBID(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals("7", returnedOrders.get(0).getID());
        assertEquals("8", returnedOrders.get(1).getID());
    }
    
    @Test
    @Order(7)
    public void testAddOrdersToDBX(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals((float) 5, returnedOrders.get(0).getX());
        assertEquals((float) 0, returnedOrders.get(1).getX());
    }
    
    @Test
    @Order(7)
    public void testAddOrdersToDBY(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals((float) 0, returnedOrders.get(0).getY());
        assertEquals((float) 0, returnedOrders.get(1).getY());
    }
    
    @Test
    @Order(7)
    public void testAddOrdersToDBZ(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals((float) 0, returnedOrders.get(0).getZ());
        assertEquals((float) 5, returnedOrders.get(1).getZ());
    }
    @Test
    @Order(7)
    public void testAddOrdersToDBCustName(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals(order1.getCustName(), returnedOrders.get(0).getCustName());
        assertEquals(order2.getCustName(), returnedOrders.get(1).getCustName());
    }
    
    @Test
    @Order(7)
    public void testAddOrdersToDBBin(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals("2", returnedOrders.get(0).getBin());
        assertEquals("2", returnedOrders.get(1).getBin());
    }
    
    @Test
    @Order(7)
    public void testAddOrdersToDBAddress(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals(order1.getCustAddress(), returnedOrders.get(0).getCustAddress());
        assertEquals(order2.getCustAddress(), returnedOrders.get(1).getCustAddress());
    }
    
    @Test
    @Order(8)
    public void testRemoveStoredOrder(){
        List<CustOrder> returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        ITDB.removeStoredOrder(returnedOrders.get(0), dataBaseName);
        
        returnedOrders = ITDB.getOrderInformationDisplay(dataBaseName);
        
        assertEquals(1, returnedOrders.size(), "Order should have been deleted");
    }
    
    @Test
    @Order(9)
    public void testOrderEmptySpaceX(){
        List<EmptySpace> availableSpaces = ITDB.getEmptySpaces("2", dataBaseName);
        
        assertEquals((float)5, availableSpaces.get(0).getX(), "X position should match");
    }
    
    @Test
    @Order(9)
    public void testOrderEmptySpaceY(){
        List<EmptySpace> availableSpaces = ITDB.getEmptySpaces("2", dataBaseName);
        
        assertEquals((float)0, availableSpaces.get(0).getY(), "Y position should match");
    }
    
    @Test
    @Order(9)
    public void testOrderEmptySpaceZ(){
        List<EmptySpace> availableSpaces = ITDB.getEmptySpaces("2", dataBaseName);
        
        assertEquals((float)0, availableSpaces.get(0).getZ(), "Z position should match");
    }
    
    @Test
    @Order(10)
    public void testUnsortedBoxList(){
        addBox1.setAmount(11);
        List<Bin> emptyList = new ArrayList<>();
        List<BoxType> addBoxes = new ArrayList<>();
        
        addBoxes.add(addBox1);
        
        NA.sortAndAddToDB(dataBaseName, addBoxes, emptyList);
        
        assertEquals(addBox1.getID(), ITDB.getUnsortedBoxDisplay(dataBaseName).get(0).getID());
        assertEquals(1, ITDB.getUnsortedBoxDisplay(dataBaseName).get(0).getAmount());
    }
}
