package tom.sros.login;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
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


@TestMethodOrder(OrderAnnotation.class)
public class UserDatabaseJUnitTest {
    
    static UserDatabase UDB = new UserDatabase();
    static String dataBaseName = ("SROSTestData.db");
    
    static User defaultUser = new User("1", "Default", true);
    static User user1 = new User("15", "Tom", false);
    static User user2 = new User("8", "Professor Yang", true);
    
    public UserDatabaseJUnitTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = ("DROP TABLE IF EXISTS user");
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        }
        catch (SQLException e){
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
        
        Connection c;
        Statement stmt;
        
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
        catch (SQLException e){
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
        Connection c;
        Statement stmt;
        
        try{
                //Conect to the database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                stmt = c.createStatement();

                String hashedPass = Hashing.sha256().hashString("password",StandardCharsets.UTF_8).toString();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE user_ID = '1'"
                        + " AND user_name = 'Default' AND is_manager = true AND"
                        + " password = '" + hashedPass + "'");
                
                if(!rs.next()){
                    fail("Default user should be created");
                }
          
                stmt.close();
            }
            //Exception catching
            catch (SQLException e){
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
        UDB.populate(dataBaseName, "8", "Professor Yang", true, "supervisor");
        
        Connection c;
        Statement stmt;
        
        try{
                //Conect to the database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                stmt = c.createStatement();
                
                ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE user_ID = '15'"
                        + " AND user_name = 'Tom' AND is_manager = false AND"
                        + " password = 'populate'");
                
                if(rs.next() == false){
                    fail("User should be inserted in to the user table");
                }
          
                stmt.close();
                c.close();
            }
            //Exception catching
            catch (SQLException e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
    }
    
    @Test
    @Order(4)
    public void logInCheckTestNonManager(){
        user1.setPassword("populate");
        if(UserDatabase.logInCheck(dataBaseName, user1.getUserName(), user1.getPassword()) != false){
            fail("The check should have passed");
        }
    }
    
    @Test
    @Order(4)
    public void logInCheckTestManager(){
        user2.setPassword("supervisor");
        if(UserDatabase.logInCheck(dataBaseName, user2.getUserName(), user2.getPassword()) != true){
            fail("The check should have passed");
        }
    }
    
    @Test
    @Order(4)
    public void logInCheckTestInvalidUser(){
        if(UserDatabase.logInCheck(dataBaseName, "Bary", "invalid") != null){
            fail("The check should not have passed");
        }
    }
    
    @Test
    @Order(5)
    public void testGetAllUsersNoPasswordID(){
        assertEquals(defaultUser.getID(), UDB.getAllUsersNoPassword(dataBaseName).get(0).getID(), "Default user ID should match");
        assertEquals(user1.getID(), UDB.getAllUsersNoPassword(dataBaseName).get(1).getID(), "user1 user ID should match");
        assertEquals(user2.getID(), UDB.getAllUsersNoPassword(dataBaseName).get(2).getID(), "user1 user ID should match");
    }
    
    @Test
    @Order(5)
    public void testGetAllUsersNoPasswordUserName(){
        assertEquals(defaultUser.getUserName(), UDB.getAllUsersNoPassword(dataBaseName).get(0).getUserName(), "Default user name should match");
        assertEquals(user1.getUserName(), UDB.getAllUsersNoPassword(dataBaseName).get(1).getUserName(), "user1 user name should match");
        assertEquals(user2.getUserName(), UDB.getAllUsersNoPassword(dataBaseName).get(2).getUserName(), "user1 user name should match");
    }
    
    @Test
    @Order(5)
    public void testGetAllUsersNoPasswordManager(){
        assertEquals(defaultUser.getIsManager(), UDB.getAllUsersNoPassword(dataBaseName).get(0).getIsManager(), "Default manager status should match");
        assertEquals(user1.getIsManager(), UDB.getAllUsersNoPassword(dataBaseName).get(1).getIsManager(), "user1 manager status should match");
        assertEquals(user2.getIsManager(), UDB.getAllUsersNoPassword(dataBaseName).get(2).getIsManager(), "user1 manager status should match");
    }
    
    @Test
    @Order(6)
    public void testRemoveUser(){
        UDB.removeUser(dataBaseName, user1.getID());
        if(UserDatabase.logInCheck(dataBaseName, user1.getUserName(), user1.getPassword()) != null){
            fail("User should have been removed");
        }
        
        user2.setPassword("supervisor");
        if(UserDatabase.logInCheck(dataBaseName, user2.getUserName(), user2.getPassword()) == null){
            fail("User should not have been removed");
        }
    }
    
    @Test
    @Order(7)
    public void testUserNameExists(){
        assertEquals(false, UDB.userNameExists(dataBaseName, user1.getUserName()));
        assertEquals(true, UDB.userNameExists(dataBaseName, user2.getUserName()));
    }
    
    @Test
    @Order(7)
    public void testUserIDExists(){
        assertEquals(false, UDB.userIDExists(dataBaseName, user1.getID()));
        assertEquals(true, UDB.userIDExists(dataBaseName, user2.getID()));
    }
}
