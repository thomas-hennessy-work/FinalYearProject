package tom.sros.item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ItemDatabase {
    
    public static void main(){
        Connection c = null;
        Statement stmt = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
            System.out.println("Connected to database");
            
            //Create tables if they do not exist
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS boxType" + 
                            "(box_ID   STRING   PRIMARY KEY   NOT NULL, " +
                            "name   STRING   NOT NULL, " +
                            "contents   TEXT, " +
                            "width   FLOAT   NOT NULL, " +
                            "length   FLOAT   NOT NULL, " +
                            "height   FLOAT   NOT NULL, " +
                            "quantity   INT   NOT NULL, " +
                            "notes TEXT)";
            stmt.executeUpdate(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS orderList" +
                    "(order_ID   STRING   PRIMARY KEY   NOT NULL, " +
                    "box_ID   STRING   NOT NULL, " +
                    "customer_name   STRING   NOT NULL, " +
                    "customer_address   STRING   NOT NULL, " + 
                    "FOREIGN KEY(box_ID)   REFERENCES boxType(box_ID))";
            stmt.executeUpdate(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS boxLocation" +
                    "(individual_ID   INTEGER   PRIMARY KEY  AUTOINCREMENT, " +
                    "box_ID   STRING   NOT NULL, " +
                    "bin_number   INT   NOT NULL, " +
                    "corner_vertical_pos   INT   NOT NULL, " +
                    "corner_horizontal_pos   INT   NOT NULL, " +
                    "corner_depth_pos   INT   NOT NULL, " + 
                    "FOREIGN KEY(box_ID)   REFERENCES boxType(box_ID))";
            stmt.executeUpdate(sql);
            System.out.println("Tables created if not alredy");
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void addBox(String ID, String name, String contents, float width, float length, float height, String notes){
            
            Statement stmt = null;
            Connection c = null;
            
            try{
                //connect to database
                c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
                System.out.println("Connected to database");
                
                //Insert into boxtype a new box
                stmt = c.createStatement();
                String sql = "INSERT INTO boxType (box_ID,name,contents,width,length,height,quantity,notes) "
                        + "VALUES ('" + ID + "', '" + name + "', '" + contents + "', "
                        + width + ", " + length + ", " + height + ", 1, '"
                        + notes + "' );";
                stmt.executeUpdate(sql);
                System.out.println("Box added to database");
                
                stmt.close();
                c.close();
                System.out.println("Database connection closed");
            }
            catch (Exception e) {
                //Exception catching
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    
    public String IDCheck(String ID){
        Statement stmt = null;
        Connection c = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            //Gather box ID's
            ResultSet IDData = stmt.executeQuery("SELECT box_ID FROM boxType;");
            
            //Check all box ID's against the ID given.
            while (IDData.next()){
                    String IDCheck = IDData.getString("box_ID");
                    
                    //If ID's match, return success
                    if (ID.equals(IDCheck)){
                        stmt.close();
                        c.close();
                        System.out.println("IDCheck succesfull + Database connection closed");
                        return IDCheck;
                    }
                }
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        //Exception catching
        catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
        }  
        //If ID's dont match, return missing
        System.out.println("ID check failed");
        return "missing";   
    }
}