package tom.sros.logIn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
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


@TestMethodOrder(OrderAnnotation.class)
public class UserDatabaseJUnitTest {
    
    static String dataBaseName = ("SROSTestData.db");
    
    public UserDatabaseJUnitTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        Connection c = null;
        Statement stmt = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = ("DROP TABLE IF EXISTS user");
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
    @Order(1)
    public void userTableExist(){
        tableExist("user");
    }
    
    @Test
    @Order(2)
    public void defaultUserExist(){
        Connection c = null;
        Statement stmt = null;
        
        try{
                //Conect to the database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                stmt = c.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE user_ID = '1'"
                        + " AND user_name = 'Default' AND is_manager = true AND"
                        + " password = 'password'");
                
                if(rs.next() == false){
                    fail("Default user should be created");
                }
          
                stmt.close();
                c.close();
                System.out.println("Database connection closed");
            }
            //Exception catching
            catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
    }
    
    @Test
    @Order(3)
    public void testPopulate(){
        
        System.out.println("testPopulate running");
        
        UserDatabase UDB = new UserDatabase();
        UDB.populate(dataBaseName, "15", "Tom", false, "populate");
        
        Connection c = null;
        Statement stmt = null;
        
        try{
                //Conect to the database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                stmt = c.createStatement();
                
                ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE user_ID = '15'"
                        + " AND user_name = 'Tom' AND is_manager = false AND"
                        + " password = 'populate'");
                
                if(rs.next() == false){
                    fail("User should be inserted in to the user table");
                }
          
                stmt.close();
                c.close();
                System.out.println("Database connection closed");
            }
            //Exception catching
            catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
    }
    
    @Test
    @Order(4)
    public void logInCheckTestMatch(){
        if(UserDatabase.logInCheck(dataBaseName, "Tom", "populate") == false){
            System.out.println(UserDatabase.logInCheck(dataBaseName, "Tom", "populate"));
            fail("The check should have passed");
        }
    }
    
    @Test
    @Order(5)
    public void logInCheckTestMiss(){
        if(UserDatabase.logInCheck(dataBaseName, "Bary", "populate")){
            fail("The check should have passed");
        }
    }
}
