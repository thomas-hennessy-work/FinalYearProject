package tom.sros.item;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import tom.sros.sorter.Bin;
import tom.sros.sorter.BoxIndividual;
import tom.sros.sorter.BoxType;
import tom.sros.sorter.CustOrder;
import tom.sros.sorter.EmptySpace;

@TestMethodOrder(OrderAnnotation.class)
public class ItemDatabaseJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    static ItemDatabase ITDB = new ItemDatabase();
    
    static BoxType expectedBox1 = new BoxType("1", "Mice", (float) 12.4, (float) 20.0, (float) 9.6, "15 Dell mice", "Fragile, do not turn upside down");
    static BoxType expectedBox2 = new BoxType("2", "Monitors", (float) 15, (float) 6.7, (float) 8.9, "2 HP monitors", "Glass, do not drop");
    static BoxType expectedBox3 = new BoxType("3", "Speakers", (float) 16, (float) 8.5, (float) 6, "one speaker", "hold uproght");
    static BoxType expectedBox4 = new BoxType("4", "Radio", (float) 8, (float) 13.2, (float) 5, "Old radio", "very fragile");
    static BoxType expectedBox5 = new BoxType("4", "Radio", (float) 8, (float) 13.2, (float) 5, "Old radio", "very fragile");
    
    static BoxIndividual expectedIndividualBox1 = new BoxIndividual("1", (float) 12.4, (float) 20.0, (float) 9.6);
    static BoxIndividual expectedIndividualBox2 = new BoxIndividual("2", (float) 15, (float) 6.7, (float) 8.9, 0, 0, 0);
    static BoxIndividual expectedIndividualBox3 = new BoxIndividual("3", (float) 16, (float) 8.5, (float) 6, 10, 10, 10);
    static BoxIndividual expectedIndividualBox4 = new BoxIndividual("4", (float) 8, (float) 13.2, (float) 5, 0, 0, 0);
    static BoxIndividual expectedIndividualBox5 = new BoxIndividual("4", (float) 8, (float) 13.2, (float) 5, 0, 0, 0);
    
    static EmptySpace empty1 = new EmptySpace((float) 16, (float) 8.5, (float)6, (float)10, (float)10, (float)10, "2");
    static EmptySpace empty2 = new EmptySpace((float) 3.5, (float) 9, (float)9.7, (float)10, (float)0, (float)0, "1");
    
    static CustOrder order1 = new CustOrder("1", "DMU Library", "Thomas");
    
    static Bin bin1 = new Bin("1");
    static Bin bin2 = new Bin("2");
    static Bin bin3 = new Bin("3");
    
    public ItemDatabaseJUnitTest() {
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
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        ItemDatabase.main(dataBaseName);
        ITDB.addBoxType(dataBaseName, "Mice", "15 Dell mice", (float) 12.4, (float) 20, (float)9.6, "Fragile, do not turn upside down");
        ITDB.addBoxType(dataBaseName, "Monitors", "2 HP monitors", (float) 15, (float) 6.7, (float)8.9, "Glass, do not drop");
        ITDB.addBoxType(dataBaseName, "Speakers", "one speaker", (float) 16, (float) 8.5, (float)6,  "hold uproght");
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
    
    public void tableExist(String tableName){
        //A method to check if the database table exists.
        Connection c;
        Statement stmt;
        
        boolean isCreated = false;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getTables(null, null, tableName, null);

            isCreated = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        if(isCreated == false){
            fail(tableName + " table not created");
        }
    }
    
    //Testing the main method.
    @Test
    @Order(1)
    public void boxLocationTableExist(){
        tableExist("boxLocation");
    }
    @Test
    @Order(1)
    public void boxTypeTableExist(){
        tableExist("boxType");
    }
    @Test 
    @Order(1)
    public void orderListTableExist(){
        tableExist("orderList");
    }
    @Test 
    @Order(1)
    public void emptySpaceTableExist(){
        tableExist("emptySpace");
    }
    @Test 
    @Order(1)
    public void unSortedBoxesTableExist(){
        tableExist("unSortedBoxes");
    }
    
    
    public String getBoxData(int rowID){
        Connection c;
        Statement stmt;
        
        String returnValue = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = 1");
            returnValue = rs.getString(rowID);
            
            stmt.close();
            c.close();
            
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnValue;
    }
    
    
    @Test
    @Order(2)
    public void testIdInsert(){
        assertEquals("1", getBoxData(1), "Box ID should be '1'");
    }
    @Test
    @Order(2)
    public void testNameInsert(){
        assertEquals("Mice", getBoxData(2), "Box name should be 'Mice'");
    }
    @Test
    @Order(2)
    public void testContentsInsert(){
        assertEquals("15 Dell mice", getBoxData(3), "Box contents should be '15 Dell mice'");
    }
    @Test
    @Order(2)
    public void testWidthInsert(){
        assertEquals("12.4", getBoxData(4), "Box width should be '12.4'");
    }
    @Test
    @Order(2)
    public void testLengthInsert(){
        assertEquals("20.0", getBoxData(5), "Box length should be '20'");
    }
    @Test
    @Order(2)
    public void testHeightInsert(){
        assertEquals("9.6", getBoxData(6), "Box height should be '9.6'");
    }
    @Test
    @Order(2)
    public void testNotesInsert(){
        assertEquals("Fragile, do not turn upside down", getBoxData(8), "Box height should be 'Fragile, do not turn upside down'");
    }
    
    
    
    @Test
    @Order(3)
    public void testGetBoxTypeInformationWidth(){
        assertEquals(expectedIndividualBox1.getWidth(), ITDB.getBoxTypeInformation("1", dataBaseName).getWidth(), "Dose box have the same width");
    }
    @Test
    @Order(3)
    public void testGetBoxTypeInformationLength(){
        assertEquals(expectedIndividualBox1.getLength(), ITDB.getBoxTypeInformation("1", dataBaseName).getLength(), "Dose box have the same length");
    }
    @Test
    @Order(3)
    public void testGetBoxTypeInformationHeight(){
        assertEquals(expectedIndividualBox1.getHeight(), ITDB.getBoxTypeInformation("1", dataBaseName).getHeight(), "Dose the box have the same height");
    }
    
    
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationIDs(){
        assertEquals(expectedBox1.getID(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getID(), "first box ID check");
        assertEquals(expectedBox2.getID(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getID(), "second box ID check");
    }
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationNamess(){
        assertEquals(expectedBox1.getName(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getName(), "first box Name check");
        assertEquals(expectedBox2.getName(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getName(), "second box Name check");
    }
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationWidths(){
        assertEquals(expectedBox1.getWidth(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getWidth(), "first box Width check");
        assertEquals(expectedBox2.getWidth(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getWidth(), "second box Width check");
    }
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationLengths(){
        assertEquals(expectedBox1.getLength(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getLength(), "first box Length check");
        assertEquals(expectedBox2.getLength(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getLength(), "second box Length check");
    }
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationHeights(){
        assertEquals(expectedBox1.getHeight(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getHeight(), "first box Height check");
        assertEquals(expectedBox2.getHeight(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getHeight(), "second box Height check");
    }
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationContentses(){
        assertEquals(expectedBox1.getContents(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getContents(), "first box Contents check");
        assertEquals(expectedBox2.getContents(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getContents(), "second box Contents check");
    }
    @Test
    @Order(4)
    public void testGetDisplayBoxTypeInformationNotes(){
        assertEquals(expectedBox1.getNotes(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getNotes(), "first box Notes check");
        assertEquals(expectedBox2.getNotes(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getNotes(), "second box Notes check");
    }
    
    
    
    @Test
    @Order(5)
    public void testIDCheckMatch(){
        if(ITDB.IDCheckBoxType(dataBaseName, "1") == false){
            fail("ID match should succeded");
        }
    }
    
    @Test
    @Order(5)
    public void testIDCheckFail(){
        if(ITDB.IDCheckBoxType(dataBaseName, "5") == true){
            fail("ID match should have failed");
        }
    }
    
    
    @Test
    @Order(5)
    public void testRemoveBox(){
        System.out.println("remove box test started");
        ITDB.removeBoxType(dataBaseName, "1");
        
        if(ITDB.IDCheckBoxType(dataBaseName, "1") == true){
            fail("Box with ID 1 should have been deleted");
        }
    }
    
    @Test
    @Order(5)
    public void testNonRemovedBox(){
        Connection c;
        Statement stmt;
        
        boolean dataFound = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = 2");
            
            dataFound = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        if(dataFound == false){
            fail("Test data should have been found");
        }
    }

    public void addBoxLocationData(){
        expectedIndividualBox2.setBin("1");
        expectedIndividualBox3.setBin("2");

        ITDB.addBoxLocation(expectedIndividualBox2, dataBaseName);
        ITDB.addBoxLocation(expectedIndividualBox3, dataBaseName);
    }
    
    @Test
    @Order(6)
    public void testAddLocation(){
        System.out.println("testAddLocation started");
        addBoxLocationData();
        
        boolean noData = false;
        boolean ID2 = false;
        boolean ID3 = false;
        
        Connection c;
        Statement stmt;
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxLocation");
            
            noData = !rs.next();
            ID2 = rs.getString("box_ID").equals("2");
            rs.next();
            ID3 = rs.getString("box_ID").equals("3");
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        if(noData){
            fail("No data inserted in the boxLocation table");
        }
        if(!ID2){
            fail("ID 2 not inserted");
        }
        if(!ID3){
            fail("ID 3 not inserted");
        }
    }
    
    @Test
    @Order(7)
    public void testGetMostRecentSortedBox(){
        assertEquals("2", ITDB.getMostRecentSortedBox(dataBaseName), "Checking most recently sorted box");
    }
    
    @Test
    @Order(7)
    public void testGetBoxLocationDisplayID(){
        assertEquals("1", ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getID(), "box 2 ID check");
        assertEquals("2", ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getID(), "box 3 ID check");
    }
    
    @Test
    @Order(7)
    public void testGetBoxLocationDisplayName(){
        assertEquals(expectedBox2.getName(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getName(), "box 2 Name check");
        assertEquals(expectedBox3.getName(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getName(), "box 3 Name check");
    }
    
    @Test
    @Order(7)
    public void testGetBoxLocationDisplayBin(){
        assertEquals(expectedIndividualBox2.getBin(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getBin(), "box 2 Bin check");
        assertEquals(expectedIndividualBox3.getBin(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getBin(), "box 3 Bin check");
    }
    
    @Test
    @Order(7)
    public void testGetBoxLocationDisplayXPos(){
        assertEquals(expectedIndividualBox2.getX(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getX(), "box 2 X check");
        assertEquals(expectedIndividualBox3.getX(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getX(), "box 3 X check");
    }
    
    @Test
    @Order(7)
    public void testGetBoxLocationDisplayYPos(){
        assertEquals(expectedIndividualBox2.getY(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getY(), "box 2 Y check");
        assertEquals(expectedIndividualBox3.getY(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getY(), "box 3 Y check");
    }
    
    @Test
    @Order(7)
    public void testGetBoxLocationDisplayZPos(){
        assertEquals(expectedIndividualBox2.getZ(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getZ(), "box 2 Z check");
        assertEquals(expectedIndividualBox3.getZ(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getZ(), "box 3 Z check");
    }
    
    @Test
    @Order(8)
    public void testReplaceBoxRemoved(){
        expectedIndividualBox3.setID("2");
        ITDB.removeStoredBox(expectedIndividualBox3, dataBaseName);
        boolean ID2 = false;
        boolean ID3 = false;
        
        Connection c;
        Statement stmt;
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxLocation");
            
            ID2 = rs.getString("individual_ID") == ("1");
            rs.next();
            ID3 = (rs.getString("individual_ID") == ("2"));
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        if(ID2){
            fail("Box 1 removed");
        }
        if(ID3){
            fail("ID 2 not removed");
        }
    }
    
    @Test
    @Order(9)
    public void testReplaceEmptSpaceAdded(){
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM emptySpace WHERE space_ID = 1");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        assertEquals(true, success, "Empty space object should be added");
    }
    
    @Test
    @Order(10)
    public void testAddEmptySpace(){
        ITDB.addEmptySpace(empty2, dataBaseName);
        
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM emptySpace WHERE space_ID = 2");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        assertEquals(true, success, "Empty space object should be added");
    }
    
    @Test
    @Order(11)
    public void testAddGetEmptySpacesWidth(){
        assertEquals(empty1.getWidth(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getWidth(), "First Width should match");
        assertEquals(empty2.getWidth(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getWidth(), "Width should match");
    }
    @Test
    @Order(11)
    public void testAddGetEmptySpacesLength(){
        assertEquals(empty1.getLength(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getLength(), "Length should match");
        assertEquals(empty2.getLength(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getLength(), "Length should match");
    }
    @Test
    @Order(11)
    public void testAddGetEmptySpacesHeight(){
        assertEquals(empty1.getHeight(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getHeight(), "Height should match");
        assertEquals(empty2.getHeight(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getHeight(), "Height should match");
    }
    @Test
    @Order(11)
    public void testAddGetEmptySpacesXPos(){
        assertEquals(empty1.getX(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getX(), "X position should match");
        assertEquals(empty2.getX(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getX(), "X position should match");
    }
    @Test
    @Order(11)
    public void testAddGetEmptySpacesYPos(){
        assertEquals(empty1.getY(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getY(), "Y position should match");
        assertEquals(empty2.getY(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getY(), "Y position should match");
    }
    @Test
    @Order(11)
    public void testAddGetEmptySpacesZPos(){
        assertEquals(empty1.getZ(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getZ(), "Z position should match");
        assertEquals(empty2.getZ(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getZ(), "Z position should match");
    }
    @Test
    @Order(11)
    public void testAddGetEmptySpacesBin(){
        assertEquals(empty1.getBin(), ITDB.getEmptySpaces("2",dataBaseName).get(0).getBin(), "Bins should match");
        assertEquals(empty2.getBin(), ITDB.getEmptySpaces("1",dataBaseName).get(0).getBin(), "Bins should match");
    }
    
    @Test
    @Order(12)
    public void testDeleteBoxLocation(){
        expectedIndividualBox3.setID("1");
        ITDB.deleteBoxLocation(expectedIndividualBox3, dataBaseName);
        
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxLocation");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if(success){
            fail("All boxes should be removed");
        }
    }
    
    @Test
    @Order(13)
    public void tesgetRemovedEmptySpace(){
        ITDB.removeEmptySpace(empty1, dataBaseName);
        
        if(ITDB.getEmptySpaces("1", dataBaseName).get(0).getID() != empty2.getID()){
            fail("fail, empty space 1 has not been found");
        } 
        
        ITDB.removeEmptySpace(empty2, dataBaseName);

        System.out.println(ITDB.getEmptySpaces("1", dataBaseName).size());
        if(!ITDB.getEmptySpaces("1", dataBaseName).isEmpty()){
            fail("fail, empty space 2 should have been removed");
        }
    }
    
    @Test
    @Order(13)
    public void testRemoveAndGetEmptySpace(){
        ITDB.removeEmptySpace(empty1, dataBaseName);
        
        if(ITDB.getEmptySpaces("1", dataBaseName).get(0).getID() != null){
            fail("fail, empty space 1 has not been removed");
        }
    }

    @Test
    @Order(14)
    public void testAddOrderInformation(){
        ITDB.addBoxType(dataBaseName, "Radio", "Old radio", (float) 8, (float) 13.2, (float) 5,  "very fragile");
        expectedIndividualBox4.setBin("1");
        ITDB.addBoxLocation(expectedIndividualBox4, dataBaseName);
        
        order1.setID("3");
        order1.setOrderID("1");
        order1.setBin("1");
        ITDB.addOrderInformation(dataBaseName, order1);
        
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM orderList WHERE order_ID = 1");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if(!success){
            fail("Order should have been added to table");
        }
    }
    
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayBoxID(){
        assertEquals(order1.getID(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getID(), "box ID's should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayOrderID(){
        assertEquals(order1.getOrderID(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getOrderID(), "order ID's should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayName(){
        assertEquals(order1.getCustName(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getCustName(), "customer names should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayAddress(){
        assertEquals(order1.getCustAddress(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getCustAddress(), "custoemr addresses should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayX(){
        assertEquals(order1.getX(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getX(), "X position should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayY(){
        assertEquals(order1.getY(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getY(), "Y position should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayZ(){
        assertEquals(order1.getZ(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getZ(), "custoemr addresses should match");
    }
    @Test
    @Order(15)
    public void testGetOrderInformationDisplayBin(){
        assertEquals(order1.getBin(), ITDB.getOrderInformationDisplay(dataBaseName).get(0).getBin(), "custoemr addresses should match");
    }
    
    @Test
    @Order(16)
    public void testGetExistingBoxes(){
        expectedIndividualBox4.setX(8);
        expectedIndividualBox4.setY(0);
        expectedIndividualBox4.setZ(0);
        expectedIndividualBox4.setBin("1");
        expectedIndividualBox5.setBin("2");
        ITDB.addBoxLocation(expectedIndividualBox4, dataBaseName);
        ITDB.addBoxLocation(expectedIndividualBox5, dataBaseName);
        
        assertEquals(2, ITDB.getExistingBoxes(bin1, dataBaseName).size(), "There should be two boxes in the list");
        assertEquals(1, ITDB.getExistingBoxes(bin2, dataBaseName).size(), "There should be two boxes in the list");
        assertEquals(0, ITDB.getExistingBoxes(bin3, dataBaseName).size(), "There should be two boxes in the list");
    }
    
    @Test
    @Order(16)
    public void testGetBoxLocationReSort(){
        assertEquals("4", ItemDatabase.getBoxLocationReSort(dataBaseName).get(0).getID());
        assertEquals(2, ItemDatabase.getBoxLocationReSort(dataBaseName).get(0).getAmount());
        assertEquals("2", ItemDatabase.getBoxLocationReSort(dataBaseName).get(1).getID());
        assertEquals(1, ItemDatabase.getBoxLocationReSort(dataBaseName).get(1).getAmount());
    }
    
    @Test
    @Order(17)
    public void TestgetExistingBoxesID(){
        expectedBox2.setID("3");
        assertEquals(expectedBox2.getID(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getID(), "Box 2 ID should match");
        assertEquals(expectedBox4.getID(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getID(), "Box 3 ID should match");
    }
    @Test
    @Order(17)
    public void TestgetExistingBoxesWidth(){
        assertEquals(expectedBox2.getWidth(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getWidth(), "Box 2 width should match");
        assertEquals(expectedBox4.getWidth(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getWidth(), "Box 3 width should match");
    }
    @Test
    @Order(17)
    public void TestgetExistingBoxesLength(){
        assertEquals(expectedBox2.getLength(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getLength(), "Box 2 length should match");
        assertEquals(expectedBox4.getLength(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getLength(), "Box 3 length should match");
    }
    @Test
    @Order(17)
    public void TestgetExistingBoxesHeight(){
        assertEquals(expectedBox2.getHeight(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getHeight(), "Box 2 height should match");
        assertEquals(expectedBox4.getHeight(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getHeight(), "Box 3 height should match");
    }
    @Test
    @Order(17)
    public void TestgetExistingBoxesX(){
        assertEquals(expectedIndividualBox2.getX(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getX(), "Box 2 X should match");
        assertEquals(expectedIndividualBox4.getX(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getX(), "Box 3 X should match");
    }
    @Test
    @Order(17)
    public void TestgetExistingBoxesY(){
        assertEquals(expectedIndividualBox2.getY(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getY(), "Box 2 Y should match");
        assertEquals(expectedIndividualBox4.getY(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getY(), "Box 3 Y should match");
    }
    @Test
    @Order(17)
    public void TestgetExistingBoxesZ(){
        assertEquals(expectedIndividualBox2.getZ(), ITDB.getExistingBoxes(bin1, dataBaseName).get(0).getZ(), "Box 2 Z should match");
        assertEquals(expectedIndividualBox4.getZ(), ITDB.getExistingBoxes(bin1, dataBaseName).get(1).getZ(), "Box 3 Z should match");
    }
    
    @Test
    @Order(18)
    public void testGetSpecificExistingBoxDimensionsWidth(){
        assertEquals(expectedIndividualBox4.getWidth(), ITDB.getSpecificExistingBoxDimensions(expectedIndividualBox4, dataBaseName).getWidth(), "Box width should match");
    }
    @Test
    @Order(18)
    public void testGetSpecificExistingBoxDimensionsLength(){
        assertEquals(expectedIndividualBox4.getLength(), ITDB.getSpecificExistingBoxDimensions(expectedIndividualBox4, dataBaseName).getLength(), "Box length should match");
    }
    @Test
    @Order(18)
    public void testGetSpecificExistingBoxDimensionsHeight(){
        assertEquals(expectedIndividualBox4.getHeight(), ITDB.getSpecificExistingBoxDimensions(expectedIndividualBox4, dataBaseName).getHeight(), "Box height should match");
    }
    
    @Test
    @Order(19)
    public void blockRepeatingBoxEntryExists(){
        assertEquals(false, ITDB.blockRepeatingBoxEntry(expectedIndividualBox4, dataBaseName), "Should return that box exists");
    }
    @Test
    @Order(19)
    public void blockRepeatingBoxEntryNotExist(){
        assertEquals(true, ITDB.blockRepeatingBoxEntry(expectedIndividualBox1, dataBaseName), "Should return that box dose not exists");
    }
    
    @Test
    @Order(20)
    public void testGetOrderBoxBin(){
        order1.setOrderID("1");
        assertEquals(expectedIndividualBox2.getBin(), ITDB.getOrderBox("1", dataBaseName).getBin(), "Bins should match");
    }
    @Test
    @Order(20)
    public void testGetOrderBoxX(){
        order1.setOrderID("1");
        assertEquals(expectedIndividualBox2.getX(), ITDB.getOrderBox("1", dataBaseName).getX(), "X positions should match");
    }
    @Test
    @Order(20)
    public void testGetOrderBoxY(){
        order1.setOrderID("1");
        assertEquals(expectedIndividualBox2.getY(), ITDB.getOrderBox("1", dataBaseName).getY(), "Y positions should match");
    }
    @Test
    @Order(20)
    public void testGetOrderBoxZ(){
        order1.setOrderID("1");
        assertEquals(expectedIndividualBox2.getZ(), ITDB.getOrderBox("1", dataBaseName).getZ(), "Z positions should match");
    }
    @Test
    @Order(20)
    public void testGetOrderBoxID(){
        order1.setOrderID("1");
        assertEquals(expectedIndividualBox2.getID(), ITDB.getOrderBox("1", dataBaseName).getID(), "X positions should match");
    }
    
    @Test
    @Order(21)
    public void testDeleteStoredOrder(){
        ITDB.deleteStoredOrder(order1, dataBaseName);
        
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM orderList WHERE order_ID = 1");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if(success){
            fail("Order should have been removed");
        }
    }
    
    @Test
    @Order(22)
    public void testAddGetUnsortedID(){
        List<BoxIndividual> unsortedList = new ArrayList<>();
        unsortedList.add(expectedIndividualBox1);
        unsortedList.add(expectedIndividualBox1);
        unsortedList.add(expectedIndividualBox2);

        ITDB.addUnsorted(unsortedList, dataBaseName);
        
        assertEquals(expectedBox1.getID(), ITDB.getUnsortedBoxDisplay(dataBaseName).get(0).getID(), "ID should match");
        assertEquals(expectedBox2.getID(), ITDB.getUnsortedBoxDisplay(dataBaseName).get(1).getID(), "ID should match");
    }
    
    @Test
    @Order(23)
    public void testGetUnsortedBoxDisplay(){
        assertEquals(2, ITDB.getUnsortedBoxDisplay(dataBaseName).get(0).getAmount(), "Amount should match");
        assertEquals(1, ITDB.getUnsortedBoxDisplay(dataBaseName).get(1).getAmount(), "Amount should match");
    }
    
    @Test
    @Order(24)
    public void testClearBoxLocationDataEmptySpace(){
        ITDB.addEmptySpace(empty1, dataBaseName);
        ITDB.clearBoxLocationData(dataBaseName);
        
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM emptySpace");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if(success){
            fail("All feilds in empty space should be removed");
        }
    }
    
    @Test
    @Order(25)
    public void testClearBoxLocationDataLocation(){
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxLocation");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if(success){
            fail("All feilds in box location should be removed");
        }
    }
    
    @Test
    @Order(26)
    public void testEmptyUnsortedTable(){
        ITDB.emptyUnsortedTable(dataBaseName);
        
        Connection c;
        Statement stmt;
        
        boolean success = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM unSortedBoxes");
            success = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if(success){
            fail("All feilds in unsorted boxes should be removed");
        }
    }
}