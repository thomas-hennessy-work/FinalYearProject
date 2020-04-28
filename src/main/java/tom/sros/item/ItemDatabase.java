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
import tom.sros.sorter.EmptySpace;
import tom.sros.sorter.Order;
import tom.sros.sorter.Space;
import tom.sros.sorter.newAlgorithm;

public class ItemDatabase {
    
    /**
     * Takes the database name and creates the tables relating to boxes and box types
     * in the database.
     * @param dataBaseName 
     */
    
    public static void main(String dataBaseName){
        Connection c;
        Statement stmt;
        
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
                    "corner_vertical_pos   FLOAT   NOT NULL, " +
                    "corner_horizontal_pos   FLOAT   NOT NULL, " +
                    "corner_depth_pos   FLOAT   NULL, " + 
                    "FOREIGN KEY(box_ID)   REFERENCES boxType(box_ID)" +
                    "FOREIGN KEY(bin_ID)   REFERENCES binIndividual(bin_ID))";
            stmt.executeUpdate(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS emptySpace" + 
                    "(space_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "bin_ID     STRING NOT   NOT NULL, " + 
                    "corner_vertical_pos   FLOAT   NOT NULL, " +
                    "corner_horizontal_pos   FLOAT   NOT NULL, " +
                    "corner_depth_pos   FLOAT   NOT NULL, " +
                    "width   FLOAT   NOT NULL, " +
                    "length   FLOAT   NOT NULL, " +
                    "height   FLOAT   NOT NULL, " +
                    "FOREIGN KEY(bin_ID)    REFERENCES binIndividual(bin_ID))";
            stmt.executeUpdate(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS unSortedBoxes" + 
                    "(unsorted_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "box_ID     STRING    NOT NULL, " + 
                    "FOREIGN KEY(box_ID)    REFERENCES  boxType(box_ID))";
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
    
    /**
     * Creates a new box type and adds it to the box type table
     * 
     * @param dataBaseName
     * @param name
     * @param contents
     * @param width
     * @param length
     * @param height
     * @param notes 
     */
    public void addBoxType(String dataBaseName,String name, String contents, float width, float length, float height, String notes){
            
            Statement stmt;
            Connection c;
            
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
    
    public void removeBoxType(String dataBaseName, String boxID){
            Statement stmt;
            Connection c;
            
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
            
            String sql = "INSERT INTO boxLocation (box_ID, bin_ID, corner_vertical_pos, corner_horizontal_pos, corner_depth_pos) "
                    + "VALUES ('" + placedBox.getID() + "', '" + placedBox.getBin() + "', '" + placedBox.getY() + "', '"
                    + placedBox.getX() + "', '" + placedBox.getZ() + "' );";
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
            
            List<Box> IDAmountList = new ArrayList();
            IDAmountList.add(new Box(currentOrder.getID(), 1));
            
            List<Bin> emptyBin = null;
            NA.sortAndAddToDB(dataBaseName, IDAmountList, emptyBin);
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
    
    public List<Box> getExistingBoxes(Bin binLocation, String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT boxType.width, boxType.length, boxType.height, boxLocation.individual_ID, boxLocation.corner_vertical_pos, boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos FROM boxType INNER JOIN boxLocation ON boxType.box_ID = boxLocation.box_ID WHERE boxLocation.bin_ID = '" + binLocation.getName() + "';");
            while(rs.next()){
                returnList.add(new Box(rs.getString("individual_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"), rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos")));
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
    
    public Box getSpecificExistingBoxDimensions(Box boxPosition, String dataBaseName){
        Connection c;
        Statement stmt;
        
        Box returnBox = boxPosition;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT boxType.width, boxType.length, boxType.height FROM boxType INNER JOIN boxLocation ON boxType.box_ID = boxLocation.box_ID WHERE boxLocation.bin_ID = '" + boxPosition.getBin() + "' AND boxLocation.corner_vertical_pos = '" + boxPosition.getY() + "' AND boxLocation.corner_horizontal_pos = '" +  boxPosition.getX() + "' AND boxLocation.corner_depth_pos = '" + boxPosition.getZ() + "';");
            while(rs.next()){
                returnBox.setWidth(rs.getFloat("width"));
                returnBox.setLength(rs.getFloat("length"));
                returnBox.setHeight(rs.getFloat("height"));
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
        return returnBox;
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
            
            ResultSet rs = stmt.executeQuery("SELECT individual_ID FROM boxLocation WHERE bin_ID = " + placedBox.getBin() + " AND corner_vertical_pos = " + placedBox.getY() + " AND corner_horizontal_pos = " + placedBox.getX() + " AND corner_depth_pos = " + placedBox.getZ());
            
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

    
    public void removeStoredBox(Box boxToRemoved, String dataBaseName){
        //Creating an empty space, similar to the box being removed
        Box removedBox = getSpecificExistingBoxDimensions(boxToRemoved, dataBaseName);
        EmptySpace newSpace = new EmptySpace(removedBox.getWidth(), removedBox.getLength(), removedBox.getHeight(), removedBox.getX(), removedBox.getY(), removedBox.getZ(), removedBox.getBin());
        
        addEmptySpace(newSpace, dataBaseName);
        deleteBoxLocation(boxToRemoved, dataBaseName);
    }
    
    //Adds the empty space object to the database
    public void addEmptySpace(EmptySpace newSpace, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "INSERT INTO emptySpace (bin_ID, corner_vertical_pos, corner_horizontal_pos, corner_depth_pos, width, length, height) "
                    + "VALUES ('" + newSpace.getBin() + "', '" + newSpace.getY() +  "', '" + newSpace.getX() + "', '" + newSpace.getZ() +
                    "', '" + newSpace.getWidth() + "', '" + newSpace.getLength() + "', '" + newSpace.getHeight() + "' );";
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
    
    //Retreves the empty spaces in a specified bin
    public List<EmptySpace> getEmptySpaces(String binID, String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<EmptySpace> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT corner_vertical_pos, corner_horizontal_pos, corner_depth_pos, width, length, height FROM emptySpace WHERE bin_ID = " + binID);
            while(rs.next()){
                returnList.add(new EmptySpace(rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"), rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos"), binID));
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
    
    //Removes a specified empty space from the data base
    public void removeEmptySpace(EmptySpace spaceToRemove, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "DELETE FROM emptySpace WHERE bin_ID = " + spaceToRemove.getBin() + " AND corner_horizontal_pos = " + spaceToRemove.getX() + " AND corner_vertical_pos = " + spaceToRemove.getY() + " AND corner_depth_pos = " + spaceToRemove.getZ();
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
    
    //removes a specific box from the storage room
    public void deleteBoxLocation(Box boxToRemove, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "DELETE FROM boxLocation WHERE individual_ID = " + boxToRemove.getID();
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
    
    //Takes the new box and the old empty space, places the box in the storage room, roomoves
    //the original empty space object and creates two new ones for the new item. The new empty
    //space object
    public List<EmptySpace> fillEmptySpace(EmptySpace spaceFilled, Box itemFillingSpace, String dataBaseName){
        List<EmptySpace> newEmptySpaces = new ArrayList<>();
        
        //Calculate the area of the new spaces
        Space bellowArea = spaceFilled.getArea().areaBellow(spaceFilled.getArea(), itemFillingSpace.getArea());
        Space rightArea = spaceFilled.getArea().areaRight(spaceFilled.getArea(), itemFillingSpace.getArea());
        
        float BellowX = spaceFilled.getX();
        float BellowZ = spaceFilled.getLength() + spaceFilled.getZ();
        float BellowY = spaceFilled.getY();
        
        float RightX = spaceFilled.getWidth() + spaceFilled.getX();
        float RightZ = spaceFilled.getZ();
        float RightY = spaceFilled.getY();
        
        //Creating new space objects using calculations
        EmptySpace bellowEmpty = new EmptySpace(bellowArea.getWidth(), bellowArea.getLength(), spaceFilled.getHeight(), BellowX, BellowY, BellowZ, spaceFilled.getBin());
        EmptySpace rightEmpty = new EmptySpace(rightArea.getWidth(), rightArea.getLength(), spaceFilled.getHeight(), RightX, RightY, RightZ, spaceFilled.getBin());

        //Adding new empty spaces to the database and to the return value
        //Only creates the empty space if they have an area (e.g. no width of 0)
        if((bellowEmpty.getWidth() > 0) && (bellowEmpty.getLength() > 0)){
            addEmptySpace(bellowEmpty, dataBaseName);
            newEmptySpaces.add(bellowEmpty);
        }
        if((rightEmpty.getWidth() > 0) && (rightEmpty.getLength() > 0)){
            addEmptySpace(rightEmpty, dataBaseName);
            newEmptySpaces.add(rightEmpty);
        }
        
        //Deleting filled empty space
        removeEmptySpace(spaceFilled, dataBaseName);
                
        return newEmptySpaces;
    }
    
    public void addUnsorted(List<Box> unsortedBoxes, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            for(Box currentBox: unsortedBoxes){
                String sql = "INSERT INTO unSortedBoxes (box_ID) "
                            + "VALUES ('" + currentBox.getID()  + "' );";
                stmt.executeUpdate(sql);
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
    }
    
    public void clearBoxLocationData(String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            //drops both tables relating to box information
            String sql = ("DROP TABLE IF EXISTS boxLocation");
            stmt.executeUpdate(sql);
            sql = ("DROP TABLE IF EXISTS emptySpace");
            stmt.executeUpdate(sql);
            
            //runs the main method, recreating the tables
            main(dataBaseName);
            
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
}