package tom.sros.item;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import tom.sros.sorter.Box;

@TestMethodOrder(OrderAnnotation.class)
public class ItemDatabaseJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    static ItemDatabase ITDB = new ItemDatabase();
    static Box expectedBox1 = new Box("1", "Mice", (float)12.4, (float)20.0, (float)9.6, "15 Dell mice", "Fragile, do not turn upside down");
    static Box expectedBox2 = new Box("2", "Monitors", (float) 15, (float) 6.7, (float)8.9, "2 HP monitors", "Glass, do not drop");
    static Box expectedBox3 = new Box("3", "Speakers", (float) 16, (float) 8.5, (float)6, "one speaker", "hold uproght");
    
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
    @Order(2)
    public void boxTypeTableExist(){
        tableExist("boxType");
    }
    @Test 
    @Order(3)
    public void orderListTableExist(){
        tableExist("orderList");
    }
    @Test 
    @Order(4)
    public void emptySpaceTableExist(){
        tableExist("emptySpace");
    }
    @Test 
    @Order(5)
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
    @Order(6)
    public void testIdInsert(){
        assertEquals("1", getBoxData(1), "Box ID should be '1'");
    }
    @Test
    @Order(7)
    public void testNameInsert(){
        assertEquals("Mice", getBoxData(2), "Box name should be 'Mice'");
    }
    @Test
    @Order(8)
    public void testContentsInsert(){
        assertEquals("15 Dell mice", getBoxData(3), "Box contents should be '15 Dell mice'");
    }
    @Test
    @Order(9)
    public void testWidthInsert(){
        assertEquals("12.4", getBoxData(4), "Box width should be '12.4'");
    }
    @Test
    @Order(10)
    public void testLengthInsert(){
        assertEquals("20.0", getBoxData(5), "Box length should be '20'");
    }
    @Test
    @Order(11)
    public void testHeightInsert(){
        assertEquals("9.6", getBoxData(6), "Box height should be '9.6'");
    }
    @Test
    @Order(12)
    public void testNotesInsert(){
        assertEquals("Fragile, do not turn upside down", getBoxData(8), "Box height should be 'Fragile, do not turn upside down'");
    }
    
    
    
    @Test
    @Order(12)
    public void testGetBoxTypeInformationWidth(){
        assertEquals(expectedBox1.getWidth(), ITDB.getBoxTypeInformation("1", dataBaseName).getWidth(), "Dose box have the same width");
    }
    @Test
    @Order(13)
    public void testGetBoxTypeInformationLength(){
        assertEquals(expectedBox1.getLength(), ITDB.getBoxTypeInformation("1", dataBaseName).getLength(), "Dose box have the same length");
    }
    @Test
    @Order(14)
    public void testGetBoxTypeInformationHeight(){
        assertEquals(expectedBox1.getHeight(), ITDB.getBoxTypeInformation("1", dataBaseName).getHeight(), "Dose the box have the same height");
    }
    
    
    @Test
    @Order(15)
    public void testGetDisplayBoxTypeInformationIDs(){
        assertEquals(expectedBox1.getID(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getID(), "first box ID check");
        assertEquals(expectedBox2.getID(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getID(), "second box ID check");
    }
    @Test
    @Order(16)
    public void testGetDisplayBoxTypeInformationNamess(){
        assertEquals(expectedBox1.getName(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getName(), "first box Name check");
        assertEquals(expectedBox2.getName(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getName(), "second box Name check");
    }
    @Test
    @Order(17)
    public void testGetDisplayBoxTypeInformationWidths(){
        assertEquals(expectedBox1.getWidth(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getWidth(), "first box Width check");
        assertEquals(expectedBox2.getWidth(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getWidth(), "second box Width check");
    }
    @Test
    @Order(18)
    public void testGetDisplayBoxTypeInformationLengths(){
        assertEquals(expectedBox1.getLength(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getLength(), "first box Length check");
        assertEquals(expectedBox2.getLength(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getLength(), "second box Length check");
    }
    @Test
    @Order(19)
    public void testGetDisplayBoxTypeInformationHeights(){
        assertEquals(expectedBox1.getHeight(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getHeight(), "first box Height check");
        assertEquals(expectedBox2.getHeight(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getHeight(), "second box Height check");
    }
    @Test
    @Order(20)
    public void testGetDisplayBoxTypeInformationContentses(){
        assertEquals(expectedBox1.getContents(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getContents(), "first box Contents check");
        assertEquals(expectedBox2.getContents(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getContents(), "second box Contents check");
    }
    @Test
    @Order(21)
    public void testGetDisplayBoxTypeInformationNotes(){
        assertEquals(expectedBox1.getNotes(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(0).getNotes(), "first box Notes check");
        assertEquals(expectedBox2.getNotes(), ItemDatabase.getDisplayBoxTypeInformation(dataBaseName).get(1).getNotes(), "second box Notes check");
    }
    
    
    
    @Test
    @Order(22)
    public void testIDCheckMatch(){
        if(ITDB.IDCheckBoxType(dataBaseName, "1") == false){
            fail("ID match should succeded");
        }
    }
    
    @Test
    @Order(23)
    public void testIDCheckFail(){
        if(ITDB.IDCheckBoxType(dataBaseName, "5") == true){
            fail("ID match should have failed");
        }
    }
    
    
    @Test
    @Order(24)
    public void testRemoveBox(){
        System.out.println("remove box test started");
        ITDB.removeBoxType(dataBaseName, "1");
        
        if(ITDB.IDCheckBoxType(dataBaseName, "1") == true){
            fail("Box with ID 1 should have been deleted");
        }
    }
    
    @Test
    @Order(25)
    public void testNonRemovedBox(){
        System.out.println("non removed box test started");
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
        expectedBox2.setX(0);
        expectedBox2.setY(0);
        expectedBox2.setZ(0);
        expectedBox2.setBin("1");
        ITDB.addBoxLocation(expectedBox2, dataBaseName);
        
        expectedBox3.setX(10);
        expectedBox3.setY(10);
        expectedBox3.setZ(10);
        expectedBox3.setBin("1");
        ITDB.addBoxLocation(expectedBox3, dataBaseName);
    }
    
    @Test
    @Order(26)
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
            ID2 = rs.getString("box_ID") == ("2");
            rs.next();
            ID3 = rs.getString("box_ID") == ("3");
            
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
        if(ID2){
            fail("ID 2 not inserted");
        }
        if(ID3){
            fail("ID 3 not inserted");
        }
    }
    
    @Test
    @Order(27)
    public void testGetMostRecentSortedBox(){
        assertEquals("2", ITDB.getMostRecentSortedBox(dataBaseName), "Checking most recently sorted box");
    }
    
    @Test
    @Order(28)
    public void testGetBoxLocationDisplayID(){
        assertEquals("1", ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getID(), "box 2 ID check");
        assertEquals("2", ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getID(), "box 3 ID check");
    }
    
    
    @Test
    @Order(28)
    public void testGetBoxLocationDisplayName(){
        assertEquals(expectedBox2.getName(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getName(), "box 2 Name check");
        assertEquals(expectedBox3.getName(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getName(), "box 3 Name check");
    }
    
    @Test
    @Order(28)
    public void testGetBoxLocationDisplayBin(){
        assertEquals(expectedBox2.getBin(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getBin(), "box 2 Bin check");
        assertEquals(expectedBox3.getBin(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getBin(), "box 3 Bin check");
    }
    
    @Test
    @Order(28)
    public void testGetBoxLocationDisplayXPos(){
        assertEquals(expectedBox2.getX(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getX(), "box 2 X check");
        assertEquals(expectedBox3.getX(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getX(), "box 3 X check");
    }
    
    @Test
    @Order(28)
    public void testGetBoxLocationDisplayYPos(){
        assertEquals(expectedBox2.getY(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getY(), "box 2 Y check");
        assertEquals(expectedBox3.getY(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getY(), "box 3 Y check");
    }
    
    @Test
    @Order(28)
    public void testGetBoxLocationDisplayZPos(){
        assertEquals(expectedBox2.getZ(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(0).getZ(), "box 2 Z check");
        assertEquals(expectedBox3.getZ(), ItemDatabase.getBoxLocationDisplay(dataBaseName).get(1).getZ(), "box 3 Z check");
    }
    
    @Test
    @Order(29)
    public void testReplaceBoxEmptSpace(){
        expectedBox3.setID("2");
        ITDB.removeStoredBox(expectedBox3, dataBaseName);
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
}

