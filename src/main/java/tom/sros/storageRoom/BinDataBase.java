package tom.sros.storageRoom;

import tom.sros.sorter.Bin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BinDataBase {
    
    public static void main(){
        //BinDataBAse bin = new BinDataBse();
    }
    
    public static void createDatabase(String dataBaseName){
        Connection c;
        Statement stmt;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
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
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    //gets information about each bin being stored
    public static List<Bin> getGeneralBinInfo(String dataBaseName){
        System.out.println("Starting getAllBinInfo");
        Connection c;
        Statement stmt;
        
        List<Bin> availableBins = new ArrayList<>(); 
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            //Gather the bin ID's and their quantity
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select type_ID, quantity, width, length, height FROM binType");
            
            //Using the resultset containing the bin information, link the ID and their
            //ammount to a pair and add the pair to a list
            while(rs.next()){
                for(int z = 0 ; z < rs.getInt("quantity") ; z ++){
                    availableBins.add(new Bin(rs.getString("type_ID"), rs.getFloat("width"), rs.getFloat("length"), rs.getFloat("height")));
                }
            }
            System.out.println("Database connection closed");
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        List<Bin> finalList =  convertGeneralBinToIndividual(availableBins, dataBaseName);
        return finalList;
    }
    
    
    public static List<Bin> convertGeneralBinToIndividual(List<Bin> availableBins, String dataBaseName){
        System.out.println("Starting getBinData");
        Connection c;
        Statement stmt;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            stmt = c.createStatement();
            //individual bins
            ResultSet rs = stmt.executeQuery("Select bin_ID, type_ID FROM binIndividual");
            
            while (rs.next()){
                boolean found = false;
                for(int i = 0 ; availableBins.size() > i && found == false ; i++){
                    if(availableBins.get(i).getName().equals(rs.getString("type_ID"))){
                        found = true;
                        availableBins.get(i).setName(rs.getString("type_ID"));
                    }
                }
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
        return availableBins;
    }
    
    //Inserts bins to the userses specifications. Creates a new bin type if the bin dose not exist.
    //If it dose exist, adds to that bin type.
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
    
    public void updateAmountType(int ammountAdd, String binTypeID, String dataBaseName){
        Connection c;
        Statement stmt;

        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT quantity FROM binType WHERE type_ID = " + binTypeID);
            String sql = ("UPDATE binType SET quantity = " + (rs.getInt("quantity") + ammountAdd) + " WHERE type_ID = " + binTypeID);
            stmt.execute(sql);
            
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
    
    //Creates a new bin type. Returns the ID of the new bin
    public String addNewBinType(Bin newBintype, String dataBaseName){
        Connection c;
        Statement stmt;
        
        String newTypeID = null;

        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
                
            String sql = "INSERT INTO binType (quantity, width, length, height) "
                        + "VALUES ('" + newBintype.getAmount()+ "', '" + newBintype.getWidth() + "', '" + newBintype.getLength() + "', '" + newBintype.getHeight() + "' );";
            stmt.executeUpdate(sql);
            
            ResultSet rs = stmt.executeQuery("Select type_ID FROM binType WHERE width = " + newBintype.getWidth() + " AND length = " + newBintype.getLength() + " AND height = " + newBintype.getHeight());
            newTypeID = rs.getString("type_ID");
                
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return newTypeID;
    }
    
    public void addBinIndividual(String binTypeID, String dataBaseName){
        Connection c;
        Statement stmt;

        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            String sql = "INSERT INTO binIndividual (type_ID) VALUES ('" + binTypeID + "');";
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
    
    //Used to verify if a bin type already exists, if it dose, returns that bin ID, if nopt, return null
    public String binExists(Bin binToCheck, String dataBaseName){
        Connection c;
        Statement stmt;
        
        String returnVal = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT type_ID FROM binType WHERE width = " + binToCheck.getWidth() + " AND length = " + binToCheck.getLength() + " AND height = " + binToCheck.getHeight() + ";");
            if(rs.next()){
                returnVal = rs.getString("type_ID");
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
        return returnVal;
    }
}
