package tom.sros.logIn;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UserDatabase {
    
        public static void main(String dataBaseName){
            Connection c = null;
            Statement stmt = null;
            
            try{
                //Conect to the database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
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
                                "VALUES ('1', 'Default', 'true', 'password' );";
                        System.out.println("Default user created");
                        stmt1.executeUpdate(sql);
                    }
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
        
        public void populate(String dataBaseName, String user_ID, String user_name, Boolean is_manager, String password){
            Connection c = null;
            Statement stmt = null;
            
            try{
                //connecting to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
                //Insert a new user in to the user database
                stmt = c.createStatement();
                String sql = "INSERT INTO user (user_ID,user_name,is_manager,password)"
                        + "VALUES ('" + user_ID + "', '"+ user_name + "', '"
                        + is_manager + "', '" + password + "' );";
                stmt.executeUpdate(sql);
                System.out.println("User added");
                
                stmt.close();
                c.close();
                System.out.println("Database connection closed");
            }
            catch (SQLException e) {
                //Exception catching
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        
        public void removeUser(String dataBaseName, String ID){
            System.out.println("Deleting user " + ID);
            
            Connection c = null;
            Statement stmt = null;
            
            try{
                //connecting to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
                //Insert a new user in to the user database
                stmt = c.createStatement();
                String sql = "DELETE FROM user WHERE user_ID = " + ID;
                stmt.executeUpdate(sql);
                
                stmt.close();
                c.close();
                System.out.println("Database connection closed");
            }
            catch (SQLException e) {
                //Exception catching
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        
        public static boolean logInCheck(String dataBaseName, String userName, String passWord){
            Connection c = null;
            Statement stmt = null;
            
            try{
                //Connecting to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
                //Gather user information from
                stmt = c.createStatement();
                ResultSet dBData = stmt.executeQuery("SELECT * FROM user;");
                
                //Compare the given username and password with the stored user names and passwords
                while (dBData.next()){
                    String userNameCheck = dBData.getString("user_name");
                    String passWordCheck = dBData.getString("password");
                    
                    //If they are the same, return true
                    if ((userName.equals(userNameCheck)) && (passWord.equals(passWordCheck))){
                        stmt.close();
                        c.close();
                        System.out.println("User credentials recognized + Database connection closed");
                        return true;
                    }
                }
                stmt.close();
                c.close();
                System.out.println("User credentials not recognized + Database connection closed");
            }
            //Exception catching
            catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
   }
             //If they do not match, return false
            return false;
        }
        
        public List<user> getAllUsersNoPassword(String dataBaseName){
            Connection c;
            Statement stmt;
            
            List returnList = new ArrayList<user>();
            
            try{
                //Connecting to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                stmt = c.createStatement();
                
                ResultSet rs = stmt.executeQuery("SELECT user_ID, user_name, is_manager FROM user");
                
                while(rs.next()){
                    if(rs.getBoolean("is_manager") == true)
                    returnList.add(new user(rs.getString("user_ID"), rs.getString("user_name"), rs.getBoolean("is_manager"), "True"));
                    else
                    returnList.add(new user(rs.getString("user_ID"), rs.getString("user_name"), rs.getBoolean("is_manager"), "False")); 
                }
                
                stmt.close();
                c.close();
                System.out.println("User credentials not recognized + Database connection closed");
            }
            //Exception catching
            catch ( SQLException e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            return returnList;
        }
    }