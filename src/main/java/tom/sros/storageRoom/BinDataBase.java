package tom.sros.storageRoom;

import com.github.skjolber.packing.Container;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class BinDataBase {
    
    public static void main(){
        //BinDataBAse bin = new BinDataBse();
    }
    
    public static void createDatabase(String dataBaseName){
        Connection c = null;
        Statement stmt = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            //Create tables if they do not exist
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS binType" + 
                            "(type_ID   STRING   PRIMARY KEY   NOT NULL, " +
                            "quantity INT  NOT NULL," +
                            "width   INT   NOT NULL, " +
                            "length   INT   NOT NULL, " +
                            "height   INT   NOT NULL)";
            stmt.executeUpdate(sql);
            
             sql = "CREATE TABLE IF NOT EXISTS binIndividual" +
                    "(bin_ID   STRING   PRIMARY KEY   NOT NULL, " +
                    "type_ID   STRING   NOT NULL, " +
                    "FOREIGN KEY(type_ID)   REFERENCES binType(type_ID))";
            stmt.executeUpdate(sql);
            
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
    
    public static List<Pair> getAllBinInfo(String dataBaseName){
        System.out.println("Starting getAllBinInfo");
        Connection c = null;
        Statement stmt = null;
        
        List<Pair> binSpecifications = new ArrayList<>();
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            //Gather the bin ID's and their quantity
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select type_ID, quantity FROM binType");
            
            //Using the resultset containing the bin information, link the ID and their
            //ammount to a pair and add the pair to a list
            while(rs.next()){
                Pair bin = new Pair(rs.getString("type_ID"),rs.getInt("quantity"));
                System.out.println(bin.toString());
                binSpecifications.add(bin);
            }
            return binSpecifications;
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return binSpecifications;
    }
    
    //move this to item database
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public int givePosition(String dataBaseName, String box_ID){
        Statement stmt = null;
        Connection c = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");

            //Gather location data of that item
            stmt = c.createStatement();
            ResultSet LocationData = stmt.executeQuery("SELECT width, length, height FROM boxType WHERE box_Id = " + box_ID + ";");
            int width = LocationData.getInt(1);
            int length = LocationData.getInt(2);
            int height = LocationData.getInt(3);
            System.out.println("Position information gathered");
            
            //Gather rear corner positions of other boxes
            ResultSet TotalSize = stmt.executeQuery("SELECT corner_vertical_pos FROM boxLocation;");
            int counter = 0;
            int Total = 0;
            int RSSize = TotalSize.getFetchSize();
            
            while (counter < RSSize){
                int addtition = TotalSize.getInt(counter);
                Total = Total + addtition;
                counter++;
            }
            
            stmt.close();
            c.close();
            System.out.println("database connection closed");
            return Total;
        }
        //Exception catching
        catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
        }
        return 0;
    }
    
    //Unfinished, need to obtain data about bins from the database
    public static List<Container> getBinData(String dataBaseName){
        List<Container> containers = new ArrayList<>();
        Statement stmt = null;
        Connection c = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            //Gather the available bin types and their quantity
            ResultSet rs = stmt.executeQuery("SELECT type_ID, quantity FROM binType;");
            
            for(int i = 0; i < rs.getFetchSize(); i++ ){
                rs.next();
                rs.getString(i);
                int quantity = rs.getInt(i);
                for(int f = 0; f < quantity ; f++ ){
                    //containers.add(new Container())
                }
            }
            
        }
        catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
        }
        return containers;
    }
    
    
    
    
}
