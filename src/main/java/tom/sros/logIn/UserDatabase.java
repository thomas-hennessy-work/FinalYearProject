package tom.sros.logIn;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UserDatabase {
    
    /**
     * Creates the user table and inserts a default user if no user exists
     * 
     * @param dataBaseName 
     */
    public static void main(String dataBaseName){
        Connection c;
        Statement stmt;
           
        try{
            //Conect to the database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                
            //Creates user table. If it already exists, it will not run
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user " + 
                            "(user_ID   STRING  PRIMARY KEY     NOT NULL," +
                            "user_name  STRING    NOT NULL, " +
                            "is_manager BOOL    NOT NULL, " +
                            "password   STRING    NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("Table created if not already");
                
                
            ResultSet rs = stmt.executeQuery("SELECT * from user");         
            //Creating a default user if no users exist
            if (rs.next() == false){
                try (Statement stmt1 = c.createStatement()) {
                    sql = "INSERT INTO user (user_ID,user_name,is_manager,password) " +
                            "VALUES ('1', 'Default', true, 'password' );";
                    System.out.println("Default user created");
                    stmt1.executeUpdate(sql);
                }
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
        
    /**
     * Adds a user to the user table
     * 
     * @param dataBaseName
     * @param user_ID
     * @param user_name
     * @param is_manager
     * @param password 
     */
    public void populate(String dataBaseName, String user_ID, String user_name, Boolean is_manager, String password){
        Connection c;
        Statement stmt;
            
        try{
            //connecting to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                
            //Insert a new user in to the user database
            stmt = c.createStatement();
            String sql = "INSERT INTO user (user_ID,user_name,is_manager,password)"
                    + "VALUES ('" + user_ID + "', '"+ user_name + "', "
                    + is_manager + ", '" + password + "' );";
            stmt.executeUpdate(sql);
                
            stmt.close();
            c.close();
        }
        catch (SQLException e) {
            //Exception catching
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
       }
    }
        
    /**
     * Removes a user from the user table
     * 
     * @param dataBaseName
     * @param ID 
     */
    public void removeUser(String dataBaseName, String ID){
        Connection c;
        Statement stmt;
            
        try{
            //connecting to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                
            //Delete a user where the ID matches the one given
            stmt = c.createStatement();
            String sql = "DELETE FROM user WHERE user_ID = " + ID;
            stmt.executeUpdate(sql);
                
            stmt.close();
            c.close();
        }
        catch (SQLException e) {
            //Exception catching
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
        
    /**
     * Verifies the credentials of the user logging in
     * 
     * @param dataBaseName
     * @param userName
     * @param passWord
     * @return Returns if the information presented is a user and if they are manager or not
     */
    public static Boolean logInCheck(String dataBaseName, String userName, String passWord){
        Connection c;
        Statement stmt;
           
        Boolean returnValue = null;
            
        try{
            //Connecting to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
               
            //Gather user information from dataBase
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT is_manager FROM user WHERE user_name = '" + userName + "' AND password = '" + passWord + "';");
             
            //If the user exists, check if they are manager
            while (rs.next()){
                returnValue = rs.getBoolean("is_manager");
            }
            stmt.close();
            c.close();
        }
        //Exception catching
        catch ( SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //Will return null if the user dose not exist. If they do, the boolean value will indicate if they are a manager
        return returnValue;
    }
        
     /**
     * Gathers all users information without their password
     * 
     * @param dataBaseName
     * @return List of user information without passwords
     */
    public List<User> getAllUsersNoPassword(String dataBaseName){
        Connection c;
        Statement stmt;
            
        List returnList = new ArrayList<User>();
            
        try{
            //Connecting to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT user_ID, user_name, is_manager FROM user");
                
            while(rs.next()){
                returnList.add(new User(rs.getString("user_ID"), rs.getString("user_name"), rs.getBoolean("is_manager")));
                System.out.println(rs.getBoolean("is_manager"));
            }
            
            stmt.close();
            c.close();
        }
        //Exception catching
        catch ( SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return returnList;
    }
    
    /**
     * Identifies if a user exists or not
     * 
     * @param dataBaseName
     * @param userID
     * @return true if user exists, false if they do not
     */    
    public boolean userExists(String dataBaseName, String userID){
        Connection c;
        Statement stmt;
        
        Boolean exists = null;

        try{
            //Connecting to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);

            //Gather user information from dataBase
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT user_ID FROM user WHERE user_ID = '" + userID + "';");

            exists = rs.next();
                
            stmt.close();
            c.close();
        }
        //Exception catching
        catch ( SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return exists;
    }
}