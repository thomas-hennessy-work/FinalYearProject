/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.sros;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tom.sros.item.ItemDatabase;
import tom.sros.logIn.UserDatabase;

/**
 *
 * @author thoma
 */
public class AppJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    
    public AppJUnitTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        //When the JUnit test for App is run, the test database will be cleared of its tables and have them re-initialized.
        
        Connection c = null;
        Statement stmt = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = ("DROP TABLE IF EXISTS user");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS orderList");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxLocation");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS boxType");
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        UserDatabase.main(dataBaseName);
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
    
    @Test
    public void userTableExist(){
        tableExist("user");
    }
    
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
}
