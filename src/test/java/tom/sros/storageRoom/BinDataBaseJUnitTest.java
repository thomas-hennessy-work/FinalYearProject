package tom.sros.storageRoom;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tom.sros.item.ItemDatabase;
import tom.sros.sorter.Bin;
import tom.sros.sorter.Box;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BinDataBaseJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    static BinDataBase BDB = new BinDataBase();
    static ItemDatabase ITDB = new ItemDatabase();
    
    Bin bin1 = new Bin("1", (float)10, (float)10, (float)10);
    Bin bin2 = new Bin("2", (float)13.4, (float)8, (float)9);
    Bin bin3 = new Bin("3", (float)20.6, (float)13.6, (float)14.2);
    
    Bin binIndi1 = new Bin("1", (float)10, (float)10, (float)10);
    Bin binIndi2 = new Bin("2", (float)10, (float)10, (float)10);
    Bin binIndi3 = new Bin("3", (float)13.4, (float)8, (float)9);
    
    Box boxType1 = new Box("1", "Mice", (float)5, (float)5, (float)5, "10 mice", "do not drop");
    
    Box boxIndi1 = new Box("1", "1", (float)0, (float)0, (float)0);
    Box boxIndi2 = new Box("1", "1", (float)5, (float)0, (float)0);
    Box boxIndi3 = new Box("1", "2", (float)0, (float)0, (float)0);
    
    public BinDataBaseJUnitTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = ("DROP TABLE IF EXISTS binType");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS binIndividual");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxType");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxLocation");
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        BinDataBase.main(dataBaseName);
        ItemDatabase.main(dataBaseName);
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
    
    @Test
    @Order(1)
    public void binTypeTableExists(){
        tableExist("binType");
    }
    @Test
    @Order(1)
    public void binIndividualTableExists(){
        tableExist("binIndividual");
    }
    
    @Test
    @Order(2)
    public void testBinExists(){
        bin1.setAmount(2);
        BDB.addNewBinType(bin1, dataBaseName);
        
        assertEquals("1", BDB.binExists(bin1, dataBaseName), "Bin should have been found");
        assertEquals(null, BDB.binExists(bin2, dataBaseName), "Bin should have been found");
        
        bin2.setAmount(1);
        BDB.addNewBinType(bin2, dataBaseName);
        
        assertEquals("2", BDB.binExists(bin2, dataBaseName), "Bin should have been found");
    }
    
    @Test
    @Order(3)
    public void testAddBinIndividual(){
        BDB.addBinIndividual(bin1.getName(), dataBaseName);
        BDB.addBinIndividual(bin1.getName(), dataBaseName);
        BDB.addBinIndividual(bin2.getName(), dataBaseName);
        
        Connection c;
        Statement stmt;
        
        String firstBin = null;
        String secondBin = null;
        String thirdBin = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM binIndividual");
            
            rs.next();
            firstBin = rs.getString("type_ID");
            rs.next();
            secondBin = rs.getString("type_ID");
            rs.next();
            thirdBin = rs.getString("type_ID");
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        assertEquals(bin1.getName(), firstBin, "Bin names should match");
        assertEquals(bin1.getName(), secondBin, "Bin names should match");
        assertEquals(bin2.getName(), thirdBin, "Bin names should match");
    }
    
    @Test
    @Order(4)
    public void testBinIDCheck(){
        assertEquals(true, BinDataBase.binIDCheck(dataBaseName, "1"), "The bin should return as present");
        assertEquals(true, BinDataBase.binIDCheck(dataBaseName, "3"), "The bin should return as present");
        assertEquals(false, BinDataBase.binIDCheck(dataBaseName, "4"), "The bin should not return as present");
    }
    
    @Test
    @Order(5)
    public void testGetStoredBoxesAndIndividualize(){
        ITDB.addBoxType(dataBaseName, boxType1.getName(), boxType1.getContents(), boxType1.getWidth(), boxType1.getLength(), boxType1.getHeight(), boxType1.getNotes());
        ITDB.addBoxLocation(boxIndi1, dataBaseName);
        ITDB.addBoxLocation(boxIndi2, dataBaseName);
        ITDB.addBoxLocation(boxIndi3, dataBaseName);
        
        assertEquals(boxIndi1.getID(), BinDataBase.getStoredBoxesAndIndividualize(dataBaseName, binIndi1).getBoxes().get(0).getID(), "Box ID's should match 1");
        assertEquals(boxIndi2.getID(), BinDataBase.getStoredBoxesAndIndividualize(dataBaseName, binIndi1).getBoxes().get(1).getID(), "Box ID's should match 2");
        assertEquals(boxIndi3.getID(), BinDataBase.getStoredBoxesAndIndividualize(dataBaseName, binIndi2).getBoxes().get(0).getID(), "Box ID's should match 3");
    }
    
    @Test
    @Order(6)
    public void testConvertGeneralBinToIndividual(){
        bin1.setAmount(2);
        List<Bin> binTypes = new ArrayList<>();
        binTypes.add(bin1);
        binTypes.add(bin2);
        
        List<Bin> individualListBin = BinDataBase.convertGeneralBinToIndividual(binTypes, dataBaseName);
        
        assertEquals(binIndi1.getName(), individualListBin.get(0).getName(), "Names should match");
        assertEquals(binIndi2.getName(), individualListBin.get(1).getName(), "Names should match");
        assertEquals(binIndi3.getName(), individualListBin.get(2).getName(), "Names should match");
    }
    
    @Test
    @Order(7)
    public void testConvertGeneralBinToIndividualWidth(){
        List<Bin> binTypes = new ArrayList<>();
        binTypes.add(bin1);
        binTypes.add(bin2);
        List<Bin> individualListBin = BinDataBase.convertGeneralBinToIndividual(binTypes, dataBaseName);

        assertEquals(binIndi1.getWidth(), individualListBin.get(0).getWidth(), "Width should match");
        assertEquals(binIndi2.getWidth(), individualListBin.get(1).getWidth(), "Width should match");
        assertEquals(binIndi3.getWidth(), individualListBin.get(2).getWidth(), "Width should match");
    }
    
    @Test
    @Order(7)
    public void testConvertGeneralBinToIndividualLength(){
        List<Bin> binTypes = new ArrayList<>();
        binTypes.add(bin1);
        binTypes.add(bin2);
        List<Bin> individualListBin = BinDataBase.convertGeneralBinToIndividual(binTypes, dataBaseName);

        assertEquals(binIndi1.getLength(), individualListBin.get(0).getLength(), "Length should match");
        assertEquals(binIndi2.getLength(), individualListBin.get(1).getLength(), "Length should match");
        assertEquals(binIndi3.getLength(), individualListBin.get(2).getLength(), "Length should match");
    }
    
    @Test
    @Order(7)
    public void testConvertGeneralBinToIndividualHeight(){
        List<Bin> binTypes = new ArrayList<>();
        binTypes.add(bin1);
        binTypes.add(bin2);
        List<Bin> individualListBin = BinDataBase.convertGeneralBinToIndividual(binTypes, dataBaseName);

        assertEquals(binIndi1.getHeight(), individualListBin.get(0).getHeight(), "Height should match");
        assertEquals(binIndi2.getHeight(), individualListBin.get(1).getHeight(), "Height should match");
        assertEquals(binIndi3.getHeight(), individualListBin.get(2).getHeight(), "Height should match");
    }
    
    @Test
    @Order(8)
    public void testGetBinInfoName(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);
        
        assertEquals(binIndi1.getName(), returnedBins.get(0).getName(), "Names should match");
        assertEquals(binIndi2.getName(), returnedBins.get(1).getName(), "Names should match");
        assertEquals(binIndi3.getName(), returnedBins.get(2).getName(), "Names should match");
    }
    
    @Test
    @Order(8)
    public void testGetBinInfoWidth(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(binIndi1.getWidth(), returnedBins.get(0).getWidth(), "Width should match");
        assertEquals(binIndi2.getWidth(), returnedBins.get(1).getWidth(), "Width should match");
        assertEquals(binIndi3.getWidth(), returnedBins.get(2).getWidth(), "Width should match");
    }
    
    @Test
    @Order(8)
    public void testGetBinInfoLength(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(binIndi1.getLength(), returnedBins.get(0).getLength(), "Length should match");
        assertEquals(binIndi2.getLength(), returnedBins.get(1).getLength(), "Length should match");
        assertEquals(binIndi3.getLength(), returnedBins.get(2).getLength(), "Length should match");
    }
    
    @Test
    @Order(8)
    public void testGetBinInfoHeight(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(binIndi1.getHeight(), returnedBins.get(0).getHeight(), "Height should match");
        assertEquals(binIndi2.getHeight(), returnedBins.get(1).getHeight(), "Height should match");
        assertEquals(binIndi3.getHeight(), returnedBins.get(2).getHeight(), "Height should match");
    }
    
    @Test
    @Order(8)
    public void testGetBinInfoBoxesID(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(boxType1.getID(), returnedBins.get(0).getBoxes().get(0).getID(), "Box should be present");
        assertEquals(boxType1.getID(), returnedBins.get(0).getBoxes().get(1).getID(), "Box should be present");
        assertEquals(boxType1.getID(), returnedBins.get(1).getBoxes().get(0).getID(), "Box should be present");
    }
    
    @Test
    @Order(8)
    public void testGetBinInfoBoxesWidth(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(boxType1.getWidth(), returnedBins.get(0).getBoxes().get(0).getWidth(), "Box width should match");
        assertEquals(boxType1.getWidth(), returnedBins.get(0).getBoxes().get(1).getWidth(), "Box width should match");
        assertEquals(boxType1.getWidth(), returnedBins.get(1).getBoxes().get(0).getWidth(), "Box width should match");
    }
    
        @Test
    @Order(8)
    public void testGetBinInfoBoxesLength(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(boxType1.getLength(), returnedBins.get(0).getBoxes().get(0).getLength(), "Box length should match");
        assertEquals(boxType1.getLength(), returnedBins.get(0).getBoxes().get(1).getLength(), "Box length should match");
        assertEquals(boxType1.getLength(), returnedBins.get(1).getBoxes().get(0).getLength(), "Box length should match");
    }
    
        @Test
    @Order(8)
    public void testGetBinInfoBoxesHeight(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals(boxType1.getHeight(), returnedBins.get(0).getBoxes().get(0).getHeight(), "Box height should match");
        assertEquals(boxType1.getHeight(), returnedBins.get(0).getBoxes().get(1).getHeight(), "Box height should match");
        assertEquals(boxType1.getHeight(), returnedBins.get(1).getBoxes().get(0).getHeight(), "Box height should match");
    }
    
    @Test
    @Order(9)
    public void testInsertBins(){
        List<Bin> newBins = new ArrayList<>();
        bin3.setAmount(3);
        bin1.setAmount(2);
        newBins.add(bin3);
        newBins.add(bin1);

        BDB.insertBins(newBins, dataBaseName);
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);

        assertEquals("4", returnedBins.get(3).getName());
        assertEquals("5", returnedBins.get(4).getName());
        assertEquals("6", returnedBins.get(5).getName());
        assertEquals("7", returnedBins.get(6).getName());
        assertEquals("8", returnedBins.get(7).getName());
    }
    
    @Test
    @Order(10)
    public void testInsertBinsWidth(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);
        
        assertEquals(bin3.getWidth(), returnedBins.get(3).getWidth());
        assertEquals(bin3.getWidth(), returnedBins.get(4).getWidth());
        assertEquals(bin3.getWidth(), returnedBins.get(5).getWidth());
        assertEquals(bin1.getWidth(), returnedBins.get(6).getWidth());
        assertEquals(bin1.getWidth(), returnedBins.get(7).getWidth());
    }
    
    @Test
    @Order(10)
    public void testInsertBinsLength(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);
        
        assertEquals(bin3.getLength(), returnedBins.get(3).getLength());
        assertEquals(bin3.getLength(), returnedBins.get(4).getLength());
        assertEquals(bin3.getLength(), returnedBins.get(5).getLength());
        assertEquals(bin1.getLength(), returnedBins.get(6).getLength());
        assertEquals(bin1.getLength(), returnedBins.get(7).getLength());
    }
    
    @Test
    @Order(10)
    public void testInsertBinsHeight(){
        List<Bin> returnedBins = BinDataBase.getBinInfo(dataBaseName);
        
        assertEquals(bin3.getHeight(), returnedBins.get(3).getHeight());
        assertEquals(bin3.getHeight(), returnedBins.get(4).getHeight());
        assertEquals(bin3.getHeight(), returnedBins.get(5).getHeight());
        assertEquals(bin1.getHeight(), returnedBins.get(6).getHeight());
        assertEquals(bin1.getHeight(), returnedBins.get(7).getHeight());
    }
    
    public boolean checkBinExistance(String bin_ID){
        Connection c;
        Statement stmt;
        
        boolean exists = false;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM binIndividual WHERE bin_ID = " + bin_ID + ";");
            exists = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return exists;
    }
    
    @Test
    @Order(11)
    public void testDeleteBin(){
        if(!checkBinExistance("7")){
            fail("Bin should be present");
        }
        
        BinDataBase.deleteBin("7", dataBaseName);
        
        if(checkBinExistance("7")){
            fail("Bin should not be present");
        }
    }
}