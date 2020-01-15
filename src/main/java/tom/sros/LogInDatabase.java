package tom.sros;

import java.sql.*;

public class LogInDatabase {
    
        public static void main(){
            Connection c = null;
            Statement stmt = null;
            
            try{
                //Class.forName("tom.sros.logInDatabase"); //Not sure if the class name is correct
                c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
                System.out.println("Database opened succesfully");
                
                //Creates user table. If it already exists, it will not run
                stmt = c.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS user " + 
                                "(user_ID   STRING  PRIMARY KEY     NOT NULL," +
                                "user_name  STRING    NOT NULL, " +
                                "is_manager BOOL    NOT NULL, " +
                                "password   STRING    NOT NULL)";
                stmt.executeUpdate(sql);
                
                //Creating a default user if no users exist
                ResultSet rs = stmt.executeQuery("SELECT * from user");
                
                
                if (rs.next() == false){
                    try (Statement stmt1 = c.createStatement()) {
                        sql = "INSERT INTO user (user_ID,user_name,is_manager,password) " +
                                "VALUES ('1', 'Default', True, 'password' );";
                        stmt1.executeUpdate(sql);
                    }
                }
                stmt.close();
                c.close();
                System.out.println(c.isClosed());
            }
            catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        
        public void populate(String user_ID, String user_name, Boolean is_manager, String password){
            Connection c = null;
            Statement stmt = null;
            
            try{
                //Class.forName("tom.sros.logInDatabase");
                c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
                c.setAutoCommit(false);
                System.out.println("Database opened succesfully");
                
                stmt = c.createStatement();
                String sql = "INSERT INTO user (user_ID,user_name,is_manager,password)"
                        + "VALUES ('" + user_ID + "', '"+ user_name + "', '"
                        + is_manager + "', '" + password + "' );";
                stmt.executeUpdate(sql);
                
                stmt.close();
                c.commit();
                c.close();
                System.out.println(c.isClosed());
            }
            catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Records succesfully created");
        }
        
        public static boolean logInCheck(String userName, String passWord){
            Connection c = null;
            Statement stmt = null;
            
            try{
                //Class.forName("tom.sros.logInDatabase");
                c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
                c.setAutoCommit(false);
                System.out.println("Database opened succesfully");
                
                stmt = c.createStatement();
                ResultSet dBData = stmt.executeQuery("SELECT * FROM user;");
                
                while (dBData.next()){
                    String userNameCheck = dBData.getString("user_name");
                    String passWordCheck = dBData.getString("password");
                    
                    if ((userName.equals(userNameCheck)) && (passWord.equals(passWordCheck))){
                        stmt.close();
                        c.close();
                        return true;
                    }
                }
                stmt.close();
                c.close();
                return false;
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
   }
                System.out.println("Operation done successfully");
                return false;
        }
    }