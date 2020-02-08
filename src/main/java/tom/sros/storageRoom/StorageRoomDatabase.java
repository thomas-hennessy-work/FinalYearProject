package tom.sros.storageRoom;

import com.github.skjolber.packing.Box;
import com.github.skjolber.packing.BoxItem;
import com.github.skjolber.packing.Container;
import com.github.skjolber.packing.LargestAreaFitFirstPackager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class StorageRoomDatabase {
    String dataBaseName = ("SROSData.db");
    
//    public static void addToStorageRoom(String dataBaseName, String storedBoxID){
//        Connection c = null;
//        Statement stmt = null;
//        
//        try{
//            //Connect to database
//            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
//            System.out.println("Connected to database");
//            stmt = c.createStatement();
//            
//            //Gather the information from the database about the given box
//            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = " + storedBoxID + ";");
//            String boxID = rs.getString(1);
//            System.out.println("Gathered informaton about box");
//            
//            BinData BD = new BinData();
//            int height = BD.givePosition(dataBaseName, storedBoxID);
//            
//            //Insert the box in to the storage room
//            String sql = "INSERT INTO boxLocation (box_ID,bin_number,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
//                    + "VALUES ('" + boxID + "', '1', '" + height + "', '0', '0');";
//            stmt.executeUpdate(sql);
//            System.out.println("Insert completed");
//            
//            stmt.close();
//            c.close();
//            System.out.println("Database connection closed");
//        }
//        catch(Exception e){
//            //Exception catching
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//    }
    
    public static void sortAndAddToStorageRoom(String dataBaseName, List<Pair> storedBoxID){
        System.out.println("Start sort and add to storage room");
        //Gather data about the bins from the get bin data function and the box information from the sortBoxesFromList function
        List<Container> bin = null;
        bin = BinDataBase.getBinData(dataBaseName);
        LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bin, true, true, true);
        
        //List<BoxItem> products = IDToBoxItem(dataBaseName, storedBoxID);
        List<BoxItem> products = new ArrayList<>();
        products.add(new BoxItem(new Box("Arm", 11, 11, 11, 0), 1));
        products.add(new BoxItem(new Box("Leg", 10, 10, 10, 0), 1));
        
        List<Container> fits = packager.packList(products, 1, Long.MAX_VALUE);
        System.out.println(fits.toString());
    }
    
    public static List<BoxItem> IDToBoxItem(String dataBaseName, List<String> BoxID){
        System.out.println("Start converting ID's to box items");
        Connection c = null;
        Statement stmt = null;
        
        //Create a list of the boxes
        List<BoxItem> products = new ArrayList<>();
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            //Go through the given list of ID's one at a time, gathering information about the boxes and putting that information in to the outgoing list of BoxItems
            for(int i = 0; i < BoxID.size(); i++){
                ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = " + BoxID.get(i) + ";");
                products.add(new BoxItem(new Box(rs.getString(2), rs.getInt(4), rs.getInt(6), rs.getInt(5), 0), 1));
            }
            
            //Close the connections to the database
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
            
            //Return the created list of products
            return products;
        }
        catch(Exception e){
            //Exception catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //If somthing goes wrong, return null
        return null;   
    }
}
