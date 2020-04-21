package tom.sros.item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import tom.sros.sorter.Bin;
import tom.sros.sorter.Box;
import tom.sros.sorter.Order;
import tom.sros.sorter.newAlgorithm;

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
                            "(box_ID   INTEGER   PRIMARY KEY   AUTOINCREMENT, " +
                            "name   STRING   NOT NULL, " +
                            "contents   TEXT, " +
                            "width   FLOAT   NOT NULL, " +
                            "length   FLOAT   NOT NULL, " +
                            "height   FLOAT   NOT NULL, " +
                            "quantity   INT   NOT NULL, " +
                            "notes TEXT)";
            stmt.executeUpdate(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS orderList" +
                    "(order_ID   INTEGER   PRIMARY KEY   AUTOINCREMENT, " +
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
    
    public void addBox(String dataBaseName,String name, String contents, float width, float length, float height, String notes){
            
            Statement stmt = null;
            Connection c = null;
            
            try{
                //connect to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
                //Insert into boxtype a new box
                stmt = c.createStatement();
                String sql = "INSERT INTO boxType (name,contents,width,length,height,quantity,notes) "
                        + "VALUES ('" + name + "', '" + contents + "', "
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
        Statement stmt;
        Connection c;
        
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
    
    public Box getBoxInformation (String boxID, String dataBaseName){
        Connection c;
        Statement stmt;
        
        Box returnBox = new Box(boxID);
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
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
    
    public static List<Box> getDisplayBoxTypeInformation(String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnBoxList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT box_ID, name, contents, width, length, height, notes FROM boxType");
            
            while(rs.next()){
                returnBoxList.add(new Box(rs.getString("box_ID"), rs.getString("name"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"), rs.getString("contents"), rs.getString("notes")));
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
        
        return returnBoxList;
    }
    
    public static List<Box> getBoxLocationDisplay(String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT boxLocation.individual_ID, boxType.name, boxLocation.bin_ID, boxLocation.corner_vertical_pos, boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos FROM boxLocation INNER JOIN boxType ON boxLocation.box_ID = boxType.box_ID");
            while(rs.next()){
                returnList.add(new Box(rs.getString("individual_ID"), rs.getString("name"), rs.getString("bin_ID"), rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos")));
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
    
    public void addBoxLocation(Box placedBox, String dataBaseName){
        
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "INSERT INTO boxLocation (box_ID, bin_ID, corner_vertical_pos, corner_horizontal_pos, corner_depth_pos, sort_order) "
                    + "VALUES ('" + placedBox.getID() + "', '" + placedBox.getBin() + "', '" + placedBox.getZ() + "', '"
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
    
    public void addOrdersToDB(String dataBaseName, List<Order> orderList){
        orderList.forEach((currentOrder) -> {
            Box orderBox = getBoxInformation(currentOrder.getID(), dataBaseName);
            newAlgorithm NA = new newAlgorithm();
            
            List<Pair> IDAmountList = new ArrayList();
            IDAmountList.add(new Pair(currentOrder.getID(), 1));
            
            NA.sortAndAddToDB(dataBaseName, IDAmountList);
            currentOrder.setID(getMostRecentSortedBox(dataBaseName));
            
            addOrderInformation(dataBaseName, currentOrder);
        });
    }
    
    public String getMostRecentSortedBox(String dataBaseName){
        Connection c;
        Statement stmt;
        
        String returnValue = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT MAX(individual_ID) FROM boxLocation");
            returnValue = rs.getString(1);
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        System.out.println(returnValue);
        return returnValue;
    }
    
    public void addOrderInformation(String dataBaseName, Order orderToAdd){
        Connection c;
        Statement stmt;
        
        String returnValue = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "INSERT INTO orderList (box_ID, customer_name, customer_address) "
                    + "VALUES ('" + orderToAdd.getID() + "', '" + orderToAdd.getCustName() +  "', '" + orderToAdd.getAddress() + "' );";
            stmt.executeUpdate(sql);
            
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
    
    public List<Box> getExistingBox(Bin binLocation, String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT boxType.width, boxType.length, boxType.height, boxLocation.individual_ID, boxLocation.corner_vertical_pos, boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos, boxLocation.sort_order FROM boxType INNER JOIN boxLocation ON boxType.box_ID = boxLocation.box_ID WHERE boxLocation.bin_ID = '" + binLocation + "';");
            while(rs.next()){
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