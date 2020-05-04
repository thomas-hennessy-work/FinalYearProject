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
import tom.sros.sorter.CustOrder;
import tom.sros.sorter.Space;
import tom.sros.sorter.newAlgorithm;

public class ItemDatabase {
    
    /**
     * Creates the tables relating to boxes and box types.
     * @param dataBaseName 
     */
    
    public static void main(String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            //Create tables if they do not exist
            //Box type table
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
            
            //orderlist table
            sql = "CREATE TABLE IF NOT EXISTS orderList" +
                    "(order_ID   INTEGER   PRIMARY KEY   AUTOINCREMENT, " +
                    "box_ID   STRING   NOT NULL, " +
                    "customer_name   STRING   NOT NULL, " +
                    "customer_address   STRING   NOT NULL, " + 
                    "FOREIGN KEY(box_ID)   REFERENCES boxLocation(individual_ID))";
            stmt.executeUpdate(sql);
            
            //box location table
            sql = "CREATE TABLE IF NOT EXISTS boxLocation" +
                    "(individual_ID   INTEGER   PRIMARY KEY  AUTOINCREMENT, " +
                    "box_ID   STRING   NOT NULL, " +
                    "bin_ID  STRING   NOT NULL, " +
                    "corner_vertical_pos   FLOAT   NOT NULL, " +
                    "corner_horizontal_pos   FLOAT   NOT NULL, " +
                    "corner_depth_pos   FLOAT   NOT NULL, " + 
                    "FOREIGN KEY(box_ID)   REFERENCES boxType(box_ID)" +
                    "FOREIGN KEY(bin_ID)   REFERENCES binIndividual(bin_ID))";
            stmt.executeUpdate(sql);
            
            //empty spaces table
            sql = "CREATE TABLE IF NOT EXISTS emptySpace" + 
                    "(space_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "bin_ID     STRING   NOT NULL, " + 
                    "corner_vertical_pos   FLOAT   NOT NULL, " +
                    "corner_horizontal_pos   FLOAT   NOT NULL, " +
                    "corner_depth_pos   FLOAT   NOT NULL, " +
                    "width   FLOAT   NOT NULL, " +
                    "length   FLOAT   NOT NULL, " +
                    "height   FLOAT   NOT NULL, " +
                    "FOREIGN KEY(bin_ID)    REFERENCES binIndividual(bin_ID))";
            stmt.executeUpdate(sql);
            
            //Unsorted boxes table
            sql = "CREATE TABLE IF NOT EXISTS unSortedBoxes" + 
                    "(unsorted_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "box_ID     STRING    NOT NULL, " + 
                    "FOREIGN KEY(box_ID)    REFERENCES  boxType(box_ID))";
            stmt.executeUpdate(sql);
            System.out.println("Tables created if not alredy");
            
            stmt.close();
            c.close();
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
                
                //Insert into boxtype a new box type
                stmt = c.createStatement();
                String sql = "INSERT INTO boxType (name,contents,width,length,height,quantity,notes) "
                        + "VALUES ('" + name + "', '" + contents + "', "
                        + width + ", " + length + ", " + height + ", 1, '"
                        + notes + "' );";
                stmt.executeUpdate(sql);
                System.out.println("Box added to database");
                
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
     * Removes box type for the table
     * 
     * @param dataBaseName
     * @param boxID 
     */
    public void removeBoxType(String dataBaseName, String boxID){
        Statement stmt;
        Connection c;
            
        try{
            //connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                
            //Insert into boxtype a new box
            stmt = c.createStatement();
            String sql = "DELETE FROM boxType WHERE box_ID = " + boxID;
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
     * Checks if a box type exists
     * 
     * @param dataBaseName
     * @param boxID
     * @return if the box exists
     */
    public boolean IDCheckBoxType(String dataBaseName, String boxID){
        Statement stmt;
        Connection c;
        
        Boolean IDExists = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            //Gather the boxes ID if it exists
            ResultSet rs = stmt.executeQuery("SELECT box_ID FROM boxType WHERE box_ID = " + boxID + ";");
            IDExists = rs.next();
            
            stmt.close();
            c.close();
        }
        //Exception catching
        catch (SQLException e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
        }  
        
        //If ID's dont match, return missing
        System.out.println("ID exists = " + IDExists);
        return IDExists;   
    }
    
    /**
     * Takes an ID for a box type and gathers its dimensions
     * 
     * @param boxID
     * @param dataBaseName
     * @return The boxes dimensions
     */
    public Box getBoxTypeInformation (String boxID, String dataBaseName){
        Connection c;
        Statement stmt;
        
        Box returnBox = new Box(boxID);
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
            returnBox.setID(boxID);
            returnBox.setWidth(rs.getFloat("width"));
            returnBox.setLength(rs.getFloat("length"));
            returnBox.setHeight(rs.getFloat("height"));
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
        //Exception catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        } 
        return returnBox;
    }
    
    /**
     * Gathers the information required for the box type table view 
     * 
     * @param dataBaseName
     * @return a list of box types and information about the box types
     */
    public static List<Box> getDisplayBoxTypeInformation(String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnBoxList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT box_ID, name, contents, width, length, height, notes FROM boxType");
            
            while(rs.next()){
                returnBoxList.add(new Box(rs.getString("box_ID"), rs.getString("name"), rs.getFloat("width"), rs.getFloat("length"), 
                rs.getFloat("height"), rs.getString("contents"), rs.getString("notes")));
            }
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        return returnBoxList;
    }
    
    /**
     * Gathers information about the location of indvidual boxes
     * 
     * @param dataBaseName
     * @return list of information about box locations
     */
    public static List<Box> getBoxLocationDisplay(String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT boxLocation.individual_ID, boxType.name, boxLocation.bin_ID, boxLocation.corner_vertical_pos, "
                    + "boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos FROM boxLocation INNER JOIN boxType ON boxLocation.box_ID = boxType.box_ID");
            while(rs.next()){
                returnList.add(new Box(rs.getString("individual_ID"), rs.getString("name"), rs.getString("bin_ID"), 
                rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos")));
            }
            stmt.close();
            c.close();
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        return returnList;
    }
    
    /**
     * Inserts a new box in to the box location table
     * 
     * @param placedBox
     * @param dataBaseName 
     */
    public void addBoxLocation(Box placedBox, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "INSERT INTO boxLocation (box_ID, bin_ID, corner_vertical_pos, corner_horizontal_pos, corner_depth_pos) "
                    + "VALUES ('" + placedBox.getID() + "', '" + placedBox.getBin() + "', '" + placedBox.getY() + "', '"
                    + placedBox.getX() + "', '" + placedBox.getZ() + "' );";
            stmt.executeUpdate(sql);
            System.out.println("Box location added to data base");
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
    }
    
    /**
     * Takes a list of orders, runs them through the sorting algorithm and adds
     * them to the storage room as well as the order list
     * 
     * @param dataBaseName
     * @param orderList 
     */
    public void addOrdersToDB(String dataBaseName, List<CustOrder> orderList){
        newAlgorithm NA = new newAlgorithm();

        //Go through each order
        orderList.forEach((currentOrder) -> {
            
            List<Box> IDAmountList = new ArrayList();
            IDAmountList.add(new Box(currentOrder.getID(), 1));
            
            List<Bin> emptyBin = null;
            //Runs algorithm
            NA.sortAndAddToDB(dataBaseName, IDAmountList, emptyBin);
            //Gathers information about the box that has been placed. Used to link it
            //to the order table
            currentOrder.setID(getMostRecentSortedBox(dataBaseName));
            addOrderInformation(dataBaseName, currentOrder);
        });
    }
    
    /**
     * Gathers the ID of the most recently added box to the box location table
     * 
     * @param dataBaseName
     * @return Box most recently added to storage room
     */
    public String getMostRecentSortedBox(String dataBaseName){
        Connection c;
        Statement stmt;
        
        String returnValue = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT MAX(individual_ID) FROM boxLocation");
            returnValue = rs.getString(1);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnValue;
    }
    
    /**
     * Adds order information to the order table
     * 
     * @param dataBaseName
     * @param orderToAdd 
     */
    public void addOrderInformation(String dataBaseName, CustOrder orderToAdd){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "INSERT INTO orderList (box_ID, customer_name, customer_address) "
                    + "VALUES ('" + orderToAdd.getID() + "', '" + orderToAdd.getCustName() +  "', '" + orderToAdd.getCustAddress() + "' );";
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close(); 
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Gathers a list of orders stored in the database used for display
     * 
     * @param dataBaseName
     * @return List of orders stored in the database
     */
    public List<CustOrder> getOrderInformationDisplay(String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<CustOrder> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT orderList.order_ID, orderList.box_ID, orderList.customer_name, orderList.customer_address, "
                    + "boxLocation.bin_ID, boxLocation.corner_vertical_pos, boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos "
                    + "FROM orderList INNER JOIN boxLocation ON orderList.box_ID = boxLocation.individual_ID;");
            
            while(rs.next()){
                returnList.add(new CustOrder(rs.getString("box_ID"), rs.getString("order_ID"), rs.getString("customer_address"), rs.getString("customer_name"),
                rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos"), rs.getString("bin_ID")));
            }
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnList;
    }
    
    /**
     * Gathers the locations and sizes of boxes that are already stored in a specified bin
     * 
     * @param binLocation
     * @param dataBaseName
     * @return list of boxes and their locations
     */
    public List<Box> getExistingBoxes(Bin binLocation, String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT boxType.width, boxType.length, boxType.height, boxLocation.individual_ID, "
                    + "boxLocation.corner_vertical_pos, boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos "
                    + "FROM boxType INNER JOIN boxLocation ON boxType.box_ID = boxLocation.box_ID WHERE boxLocation.bin_ID = '" + binLocation.getName() + "';");
            
            while(rs.next()){
                returnList.add(new Box(rs.getString("individual_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"),
                        rs.getFloat("corner_horizontal_pos"), rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos")));
            }
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnList;
    }
    
    /**
     * Gathers the dimensions of a box in a specified location
     * 
     * @param boxPosition
     * @param dataBaseName
     * @return box dimensions
     */
    public Box getSpecificExistingBoxDimensions(Box boxPosition, String dataBaseName){
        Connection c;
        Statement stmt;
        
        Box returnBox = boxPosition;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT boxType.width, boxType.length, boxType.height FROM boxType "
                    + "INNER JOIN boxLocation ON boxType.box_ID = boxLocation.box_ID WHERE boxLocation.bin_ID = '" + boxPosition.getBin() 
                    + "' AND boxLocation.corner_vertical_pos = '" + boxPosition.getY() + "' AND boxLocation.corner_horizontal_pos = '" +  boxPosition.getX() 
                    + "' AND boxLocation.corner_depth_pos = '" + boxPosition.getZ() + "';");
            
            while(rs.next()){
                returnBox.setWidth(rs.getFloat("width"));
                returnBox.setLength(rs.getFloat("length"));
                returnBox.setHeight(rs.getFloat("height"));
            }
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnBox;
    }
    
    /**
     * Checks if a box already exists within the box location table
     * 
     * @param placedBox
     * @param dataBaseName
     * @return true if there is a box in that location, false if there is not
     */
    public boolean blockRepeatingBoxEntry(Box placedBox, String dataBaseName){
        Connection c;
        Statement stmt;
        
        Boolean returnValue = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT individual_ID FROM boxLocation WHERE bin_ID = " + placedBox.getBin() 
                    + " AND corner_vertical_pos = " + placedBox.getY() + " AND corner_horizontal_pos = " + placedBox.getX() 
                    + " AND corner_depth_pos = " + placedBox.getZ());
            
            returnValue = rs.next();
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        return !returnValue;
    }

    /**
     * Removes a box from the bin and replaces it with an empty space
     * 
     * @param boxToRemoved
     * @param dataBaseName 
     */
    public void removeStoredBox(Box boxToRemoved, String dataBaseName){
        //Creating an empty space object with the same dimensions and possion as the box and adding it
        Box removedBox = getSpecificExistingBoxDimensions(boxToRemoved, dataBaseName);
        EmptySpace newSpace = new EmptySpace(removedBox.getWidth(), removedBox.getLength(), removedBox.getHeight(),
                removedBox.getX(), removedBox.getY(), removedBox.getZ(), removedBox.getBin());
        
        //Adds the empty space, removes the box
        addEmptySpace(newSpace, dataBaseName);
        deleteBoxLocation(boxToRemoved, dataBaseName);
    }
    
    public void removeStoredOrder(CustOrder orderToRemove, String dataBasename){
        Box linkedBox = getOrderBox(orderToRemove.getOrderID(), dataBasename);
        deleteStoredOrder(orderToRemove, dataBasename);
        removeStoredBox(linkedBox, dataBasename);
    }
    
    /**
     * Deletes a specified order from the order table 
     * 
     * @param orderToRemove
     * @param dataBasename 
     */
    public void deleteStoredOrder(CustOrder orderToRemove, String dataBasename){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBasename);
            stmt = c.createStatement();
            
            String sql = "DELETE FROM orderList WHERE order_ID = " + orderToRemove.getOrderID();
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Gathers information regarding boxes linked to a specified order
     * 
     * @param orderID
     * @param dataBasename
     * @return Box and its associated information that is linked to an order
     */
    public Box getOrderBox(String orderID, String dataBasename){
        Connection c;
        Statement stmt;
        
        Box returnBox = new Box();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBasename);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT boxLocation.individual_ID, boxLocation.bin_ID, boxLocation.corner_vertical_pos, "
                    + "boxLocation.corner_horizontal_pos, boxLocation.corner_depth_pos FROM boxLocation INNER JOIN orderList ON boxLocation.individual_ID = orderList.box_ID "
                    + "WHERE orderList.order_ID = '" + orderID + "';");
            
            System.out.println(rs.next());
            
            returnBox.setBin(rs.getString("bin_ID"));
            returnBox.setX(rs.getFloat("corner_horizontal_pos"));
            returnBox.setY(rs.getFloat("corner_vertical_pos"));
            returnBox.setZ(rs.getFloat("corner_depth_pos"));
            returnBox.setID(rs.getString("individual_ID"));
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        
        return returnBox;
    }
    
    /**
     * Adds an empty space object to the empty space table
     * 
     * @param newSpace
     * @param dataBaseName 
     */
    public void addEmptySpace(EmptySpace newSpace, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "INSERT INTO emptySpace (bin_ID, corner_vertical_pos, corner_horizontal_pos, corner_depth_pos, width, length, height) "
                    + "VALUES ('" + newSpace.getBin() + "', '" + newSpace.getY() +  "', '" + newSpace.getX() + "', '" + newSpace.getZ() +
                    "', '" + newSpace.getWidth() + "', '" + newSpace.getLength() + "', '" + newSpace.getHeight() + "' );";
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Retreves the empty spaces in a specified bin
     * 
     * @param binID
     * @param dataBaseName
     * @return list of empty spaces
     */
    public List<EmptySpace> getEmptySpaces(String binID, String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<EmptySpace> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT corner_vertical_pos, corner_horizontal_pos, corner_depth_pos, width, length, "
                    + "height FROM emptySpace WHERE bin_ID = " + binID);
            while(rs.next()){
                returnList.add(new EmptySpace(rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height"), rs.getFloat("corner_horizontal_pos"), 
                        rs.getFloat("corner_vertical_pos"), rs.getFloat("corner_depth_pos"), binID));
            }
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnList;
    }
    
    /**
     * Removes a specified empty space from the data base
     * 
     * @param spaceToRemove
     * @param dataBaseName 
     */
    public void removeEmptySpace(EmptySpace spaceToRemove, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "DELETE FROM emptySpace WHERE bin_ID = " + spaceToRemove.getBin() + " AND corner_horizontal_pos = " 
                    + spaceToRemove.getX() + " AND corner_vertical_pos = " + spaceToRemove.getY() + " AND corner_depth_pos = " + spaceToRemove.getZ();
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Removes a specific box from the box location table
     * 
     * @param boxToRemove
     * @param dataBaseName 
     */
    public void deleteBoxLocation(Box boxToRemove, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "DELETE FROM boxLocation WHERE individual_ID = " + boxToRemove.getID();
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**Takes the new box and the old empty space, places the box in the storage room, removes
     * the original empty space object and creates two new empty spaces for the new item. The new empty space object
     * are inserted in to the empty space table
     * 
     * @param spaceFilled
     * @param itemFillingSpace
     * @param dataBaseName
     * @return list of the new empty space objects
     */
    public List<EmptySpace> fillEmptySpace(EmptySpace spaceFilled, Box itemFillingSpace, String dataBaseName){
        List<EmptySpace> newEmptySpaces = new ArrayList<>();
        
        //Calculate the area of the new empty spaces
        Space bellowArea = spaceFilled.getArea().areaBellow(spaceFilled.getArea(), itemFillingSpace.getArea());
        Space rightArea = spaceFilled.getArea().areaRight(spaceFilled.getArea(), itemFillingSpace.getArea());
        
        float BellowX = spaceFilled.getX();
        float BellowZ = spaceFilled.getLength() + spaceFilled.getZ();
        float BellowY = spaceFilled.getY();
        
        float RightX = spaceFilled.getWidth() + spaceFilled.getX();
        float RightZ = spaceFilled.getZ();
        float RightY = spaceFilled.getY();
        
        //Creating new space objects using the calculations above
        EmptySpace bellowEmpty = new EmptySpace(bellowArea.getWidth(), bellowArea.getLength(), spaceFilled.getHeight(), BellowX, BellowY, BellowZ, spaceFilled.getBin());
        EmptySpace rightEmpty = new EmptySpace(rightArea.getWidth(), rightArea.getLength(), spaceFilled.getHeight(), RightX, RightY, RightZ, spaceFilled.getBin());

        //Adding new empty spaces to the database and to the return value
        //Only creates the empty space if they have an area (e.g. no width or length of 0)
        if((bellowEmpty.getWidth() > 0) && (bellowEmpty.getLength() > 0)){
            addEmptySpace(bellowEmpty, dataBaseName);
            newEmptySpaces.add(bellowEmpty);
        }
        if((rightEmpty.getWidth() > 0) && (rightEmpty.getLength() > 0)){
            addEmptySpace(rightEmpty, dataBaseName);
            newEmptySpaces.add(rightEmpty);
        }
        
        //Removing the empty space that has been filled
        removeEmptySpace(spaceFilled, dataBaseName);
                
        return newEmptySpaces;
    }
    
    /**
     * Adds unsorted boxes to the unsorted table
     * 
     * @param unsortedBoxes
     * @param dataBaseName 
     */
    public void addUnsorted(List<Box> unsortedBoxes, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            for(Box currentBox: unsortedBoxes){
                String sql = "INSERT INTO unSortedBoxes (box_ID) "
                            + "VALUES ('" + currentBox.getID()  + "' );";
                stmt.executeUpdate(sql);
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
    
    /**
     * Deletes all stored information in the box location and empty space tables
     * 
     * @param dataBaseName 
     */
    public void clearBoxLocationData(String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            //drops both tables relating to box information
            String sql = ("DELETE FROM boxLocation");
            stmt.executeUpdate(sql);
            sql = ("DELETE FROM emptySpace");
            stmt.executeUpdate(sql);
            
            //runs the main method, recreating the tables
            main(dataBaseName);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Gathers the display information about unsorted boxes
     * 
     * @param dataBaseName
     * @return amount and type of unsorted boxes
     */
    public List<Box> getUnsortedBoxDisplay(String dataBaseName){
        Connection c;
        Statement stmt;
        
        List<Box> returnList = new ArrayList<>();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM unSortedBoxes");
            while(rs.next()){
                boolean found = false;
                for(Box currentBox : returnList){
                    if(currentBox.getID().equals(rs.getString("box_ID"))){
                        currentBox.setAmount(currentBox.getAmount() + 1);
                        found = true;
                    }
                }
                if(found == false){
                    returnList.add(new Box(rs.getString("box_ID"), 1));
                }
            }
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnList;
    }
    
    /**
     * Empties all boxes stored in the unsorted boxes table
     * 
     * @param dataBasename 
     */
    public void emptyUnsortedTable(String dataBasename){
        Connection c;
        Statement stmt;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBasename);
            stmt = c.createStatement();
            
            String sql = "DELETE FROM unSortedBoxes;";
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}