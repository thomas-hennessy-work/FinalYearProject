package tom.sros.storageRoom;

import tom.sros.sorter.Bin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tom.sros.sorter.Box;

public class BinDataBase {
    
    /**
     * Creates the tables relating to bins if they do not exist in the specified database
     * 
     * @param dataBaseName 
     */
    public static void main(String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            //Create tables if they do not exist
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS binType" + 
                            "(type_ID   INTEGER   PRIMARY KEY  AUTOINCREMENT, " +
                            "quantity INT  NOT NULL," +
                            "width   INT   NOT NULL, " +
                            "length   INT   NOT NULL, " +
                            "height   INT   NOT NULL)";
            stmt.executeUpdate(sql);
            
             sql = "CREATE TABLE IF NOT EXISTS binIndividual" +
                    "(bin_ID   INTEGER   PRIMARY KEY  AUTOINCREMENT, " +
                    "type_ID   STRING   NOT NULL, " +
                    "FOREIGN KEY(type_ID)   REFERENCES binType(type_ID))";
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
     * Verifies if an ID provided matches a bin in the database
     * 
     * @param dataBaseName
     * @param binID
     * @return If the provided ID is linked to an existing bin
     */
    public static boolean binIDCheck(String dataBaseName, String binID){
        System.out.println("Starting getAllBinInfo");
        Connection c;
        Statement stmt;
        
        boolean returnValue = false;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            //Gather the bin ID's and their quantity
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT bin_ID FROM binIndividual WHERE bin_ID = '" + binID +"';");
            returnValue = rs.next();
            
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
     * Gets the information of each bin stored in the database
     * 
     * @param dataBaseName
     * @return List of bins and their information
     */
    public static List<Bin> getBinInfo(String dataBaseName){
        System.out.println("Starting getAllBinInfo");
        Connection c;
        Statement stmt;
        
        List<Bin> availableBins = new ArrayList<>(); 
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            //Gather the bin ID's and their quantity
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select type_ID, quantity, width, length, height FROM binType");
            
            //Using the resultset containing the bin information, link the ID and their
            //ammount to a pair and add the pair to a list
            while(rs.next()){
                availableBins.add(new Bin(rs.getString("type_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height")));
            }

            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        List<Bin> finalList = convertGeneralBinToIndividual(availableBins, dataBaseName);
        return finalList;
    }
    
    /**
     * Converts the general bin types in to individual bins and inserts all boxes stored in that bin,
     * in to the bin
     * 
     * @param availableBins
     * @param dataBaseName
     * @return List of bins with their size information and stored bins information
     */
    public static List<Bin> convertGeneralBinToIndividual(List<Bin> availableBins, String dataBaseName){
        System.out.println("Starting getBinData");
        Connection c;
        Statement stmt;
        
        List<Bin> specificBinList = new ArrayList<>();
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            stmt = c.createStatement();
            //individual bins
            ResultSet rs = stmt.executeQuery("Select bin_ID, type_ID FROM binIndividual");
            
            //assigning bin dimensions to the bin
            while(rs.next()){
                for(Bin currentAvailableBin : availableBins){
                    if(currentAvailableBin.getName().equals(rs.getString("type_ID"))){
                        specificBinList.add(new Bin(rs.getString("bin_ID"), currentAvailableBin.getWidth(), currentAvailableBin.getLength(), currentAvailableBin.getHeight()));
                        break;
                    }
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
        
        //Assigning boxes stored in the bin to the bin
        List<Bin> returnBinList = new ArrayList<>();
        specificBinList.forEach((currentBin) -> {
            returnBinList.add(getStoredBoxesAndIndividualize(dataBaseName, currentBin));
        });
        
        return returnBinList;
    }
    
    /**
     * Gathers a bin individual ID and obtains the mesurements of the bin
     * 
     * @param dataBaseName
     * @param bin
     * @return Bin with dimensions
     */
    public static Bin getStoredBoxesAndIndividualize(String dataBaseName, Bin bin){
        Connection c;
        Statement stmt;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT boxLocation.box_ID, boxType.width, boxType.length, boxType.height FROM boxLocation "
                    + "INNER JOIN boxType ON boxLocation.box_ID = boxType.box_ID WHERE boxLocation.bin_ID = '" + bin.getName() + "';");
            while(rs.next()){
                bin.addBox(new Box(rs.getString("box_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height")));
            }
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return bin;
    }
    
    //
    
    /**
     * Inserts bins to the users specifications. Creates a new bin type if the bin dose not exist.
     * If it dose exist, adds to that bin type.
     * 
     * @param newBins
     * @param dataBaseName 
     */
    public void insertBins(List<Bin> newBins,String dataBaseName){
        newBins.forEach((currentBin) -> {
            String existing = binExists(currentBin, dataBaseName);
            
            if(existing == null){
                String newIndividualID = addNewBinType(currentBin, dataBaseName);
                for(int i = 0; i < currentBin.getAmount() ; i++){
                    addBinIndividual(newIndividualID, dataBaseName);
                }
            }else{
                for(int i = 0; i < currentBin.getAmount() ; i++){
                    addBinIndividual(existing, dataBaseName);
                }
            }
        });
    }
    
    /**
     * Creates a new bin type
     * 
     * @param newBintype
     * @param dataBaseName
     * @return The type ID of the new bin
     */
    public String addNewBinType(Bin newBintype, String dataBaseName){
        Connection c;
        Statement stmt;
        
        String newTypeID = null;

        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
                
            String sql = "INSERT INTO binType (quantity, width, length, height) "
                        + "VALUES ('" + newBintype.getAmount()+ "', '" + newBintype.getWidth() + "', '" + newBintype.getLength() + "', '" + newBintype.getHeight() + "' );";
            stmt.executeUpdate(sql);
            
            ResultSet rs = stmt.executeQuery("Select type_ID FROM binType WHERE width = " + newBintype.getWidth() + " AND length = " 
                    + newBintype.getLength() + " AND height = " + newBintype.getHeight());
            newTypeID = rs.getString("type_ID");
                
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return newTypeID;
    }
    
    /**
     * Adds a bin in to the bin individual table
     * 
     * @param binTypeID
     * @param dataBaseName 
     */
    public void addBinIndividual(String binTypeID, String dataBaseName){
        Connection c;
        Statement stmt;

        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "INSERT INTO binIndividual (type_ID) VALUES ('" + binTypeID + "');";
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
     * Verifies if a bin already exists
     * 
     * @param binToCheck
     * @param dataBaseName
     * @return bin ID, if the bin is found
     */
    public String binExists(Bin binToCheck, String dataBaseName){
        Connection c;
        Statement stmt;
        
        String returnVal = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT type_ID FROM binType WHERE width = " + binToCheck.getWidth() 
                    + " AND length = " + binToCheck.getLength() + " AND height = " + binToCheck.getHeight() + ";");
            if(rs.next()){
                returnVal = rs.getString("type_ID");
            }
            
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnVal;
    }
    
    /**
     * Deletes a bin
     * 
     * @param binID
     * @param dataBaseName 
     */
    public static void deleteBin (String binID, String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            
            String sql = "DELETE FROM binIndividual WHERE bin_ID = " + binID;
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
