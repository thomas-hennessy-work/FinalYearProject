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

public class ItemDatabaseJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    
    public ItemDatabaseJUnitTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        
        Connection c = null;
        Statement stmt = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();

            String sql = ("DROP TABLE IF EXISTS orderList");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxLocation");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxType");
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
        ItemDatabase ITDB = new ItemDatabase();
        ITDB.addBoxType(dataBaseName, "Mice", "15 Dell mice", (float) 12.4, (float) 20, (float)9.6, "Fragile, do not turn upside down");
        
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
        
        Connection c = null;
        Statement stmt = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getTables(null, null, tableName, null);

            if(rs.next() == false){
                fail(tableName + " table not created");
            }
            
            stmt.close();
            c.close();
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    //Testing the main method.
    @Test
    public void boxLocationTableExist(){
        tableExist("boxLocation");
    }
    @Test
    public void boxTypeTableExist(){
        tableExist("boxType");
    }
    @Test void orderListTableExist(){
        tableExist("orderList");
    }
    
    
    public String getBoxData(int rowID){
        
        Connection c = null;
        Statement stmt = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = 1");
            String requestedData = rs.getString(rowID);
            
            stmt.close();
            c.close();
            
            return requestedData;
            
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
    
    
    @Test
    public void testIdInsert(){
        assertEquals("1", getBoxData(1), "Box ID should be '1'");
    }
    @Test
    public void testNameInsert(){
        assertEquals("Mice", getBoxData(2), "Box name should be 'Mice'");
    }
    @Test
    public void testContentsInsert(){
        assertEquals("15 Dell mice", getBoxData(3), "Box contents should be '15 Dell mice'");
    }
    @Test
    public void testWidthInsert(){
        assertEquals("12.4", getBoxData(4), "Box width should be '12.4'");
    }
    @Test
    public void testLengthInsert(){
        assertEquals("20.0", getBoxData(5), "Box length should be '20'");
    }
    @Test
    public void testHeightInsert(){
        assertEquals("9.6", getBoxData(6), "Box height should be '9.6'");
    }
    @Test
    public void testNotesInsert(){
        assertEquals("Fragile, do not turn upside down", getBoxData(8), "Box height should be 'Fragile, do not turn upside down'");
    }
    
    
    @Test
    public void testIDCheckMatch(){
        ItemDatabase ITDB = new ItemDatabase();
        if(ITDB.IDCheckBoxType(dataBaseName, "1") == false){
            fail("ID match should succeded");
        }
    }
    
    @Test
    public void testIDCheckFail(){
        ItemDatabase ITDB = new ItemDatabase();
        if(ITDB.IDCheckBoxType(dataBaseName, "5") == true){
            fail("ID match should have failed");
        }
    }
    
    
    @Test
    public void testRemoveBox(){
        System.out.println("remove box test started");
        ItemDatabase ITDB = new ItemDatabase();
        ITDB.removeBoxType(dataBaseName, "1");
        
        Connection c = null;
        Statement stmt = null;
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = 1");
            
           
            
            //If the result set dose not have any data in it
            if(rs.next()){
                fail("Test data should have been removed");
            }
            
            stmt.close();
            c.close();
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}

