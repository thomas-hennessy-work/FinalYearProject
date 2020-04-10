package tom.sros.item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tom.sros.sorter.Bin;
import tom.sros.sorter.Box;

public class ItemDatabase {
    
    public static void main(String dataBaseName){
        Connection c = null;
        Statement stmt = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
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
                    "bin_ID  STRING   NOT NULL, " +
                    "corner_vertical_pos   INT   NOT NULL, " +
                    "corner_horizontal_pos   INT   NOT NULL, " +
                    "corner_depth_pos   INT   NOT NULL, " + 
                    "sort_order    INT," +
                    "FOREIGN KEY(box_ID)   REFERENCES boxType(box_ID)" +
                    "FOREIGN KEY(bin_ID)   REFERENCES binIndividual(bin_ID))";
            stmt.executeUpdate(sql);
            System.out.println("Tables created if not alredy");
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void addBox(String dataBaseName, String ID, String name, String contents, float width, float length, float height, String notes){
            
            Statement stmt = null;
            Connection c = null;
            
            try{
                //connect to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
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
            catch (SQLException e) {
                //Exception catching
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    
    public void removeBox(String dataBaseName, String boxID){
            Statement stmt = null;
            Connection c = null;
            
            try{
                //connect to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
                //Insert into boxtype a new box
                stmt = c.createStatement();
                String sql = "DELETE FROM boxType WHERE box_ID = " + boxID;
                stmt.executeUpdate(sql);
                System.out.println("Box removed from database");
                
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
    
    public boolean IDCheck(String dataBaseName, String ID){
        Statement stmt = null;
        Connection c = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
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
                        return true;
                    }
                }
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        //Exception catching
        catch (SQLException e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
        }  
        //If ID's dont match, return missing
        System.out.println("ID check failed");
        return false;   
    }
    
    public Box getBoxInformation (String boxName, String dataBaseName){
        Connection c = null;
        Statement stmt = null;
        
        Box returnBox = new Box(boxName);
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxName);
            returnBox.setWidth(rs.getFloat("width"));
            returnBox.setLength(rs.getFloat("length"));
            returnBox.setHeight(rs.getFloat("height"));
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        
        return returnBox;
    }
    
    public void addBoxLocation(Box placedBox, String dataBaseName){
        
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "INSERT INTO boxLocation (box_ID, bin_ID, corner_vertical_pos, corner_horizontal_pos, corner_depth_pos, sort_order) "
                    + "VALUES ('" + placedBox.getName() + "', '" + placedBox.getBin() + "', '" + placedBox.geZ() + "', '"
                    + placedBox.getX() + "', '" + placedBox.getY() + "', '" + placedBox.getSortOrder() + "' );";
            stmt.executeUpdate(sql);
            System.out.println("Box location added to data base");
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
    }
    
    public List<Box> getBoxLocation(Bin binLocation, String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT boxType.width, boxType.length, boxType.height, boxLocation.individual_ID, boxLocation.corner_vertical_pos, boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos, boxLocation.sort_order FROM boxType INNER JOIN boxLocation ON boxType.box_ID = boxLocation.box_ID WHERE boxLocation.bin_ID = '" + binLocation + "';");
            while(rs.next()){
                // may not need all this information
                //returnList.add(new Box(rs.getString("individual_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"), rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_depth_pos"), rs.getFloat("corner_vertical_pos"), rs.getInt("sort_order")));
                returnList.add(new Box(rs.getString("individual_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"), rs.getInt("sort_order")));
            }
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        return returnList;
    }
    
    //Used with addBoxLocation so that boxes that were stored in the bin before the sort are not input repeatedly
    public boolean blockRepeatingBoxEntry(Box placedBox, String dataBaseName){
        Connection c;
        Statement stmt;
        
        Boolean returnValue = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT individual_ID FROM boxLocation WHERE bin_ID = " + placedBox.getBin() + " AND sort_order = " + placedBox.getSortOrder());
            
            returnValue = rs.next();
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        return !returnValue;
    }
}