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
    
    public static void sortAndAddToStorageRoom(String dataBaseName, List<Pair> unsortedBoxesAndAmounts){
        System.out.println("\nStart sort and add to storage room");
        Connection c = null;
        Statement stmt = null;
        
        //Gather data about the bins from the get all bin info function
        List<Pair> binSpecifications = BinDataBase.getAllBinInfo(dataBaseName);
        System.out.println(binSpecifications.size());
        
        //Is it better to make multiple small conections to the database ore one big one
        //Should I just ignore the getAllBinInfo function and gather all the information here
        
        //go through each bin given in the list of pairs "binspecifications"
        for(int i = 0 ; i < binSpecifications.size() ; i ++){
            System.out.println("adding bin ID " + ((String) binSpecifications.get(i).getKey()));
            String binID = (String) binSpecifications.get(i).getKey();
            System.out.println("Bin ID gathered: " + binID);
            int binAmount = (int) binSpecifications.get(i).getValue();
            System.out.println("Bin amount gathered: " + binAmount);
            
            try{
                //Connect to database
                c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
                System.out.println("Connected to database");
                
                //gather the mesurements of the bin
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT width, length, height FROM binType WHERE type_ID = " + binID + ";");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                int height = rs.getInt("height");
                System.out.println("Bin data gathered");
                
                //Add the bin for this for loop a number of times 
                List<Container> bins = new ArrayList<>();
                for(int x=0 ; x < binAmount ; x++){
                    bins.add(new Container(width, height, length, 0));
                }
                
                //emptying the width height and length variables
                width = 0;
                length = 0;
                height = 0;
                
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!You may need to close the connection here!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                
                //Create a list of all the boxes to be sorted
                List<BoxItem> boxes = new ArrayList<>();
                for(int y=0 ; y < unsortedBoxesAndAmounts.size() ; y++){
                    String boxID = (String) unsortedBoxesAndAmounts.get(y).getKey();
                    int boxAmount = (int) unsortedBoxesAndAmounts.get(y).getValue();
                    
                    //gather box dimensions from db
                    rs = stmt.executeQuery("Select name, width, length, height FROM boxType WHERE box_ID");
                    width = rs.getInt("width");
                    length = rs.getInt("length");
                    height = rs.getInt("height");
                    String name = rs.getString("name");
                    
                    //add the box to the list
                    boxes.add(new BoxItem(new Box(name, width, length, height, 0), boxAmount));
                }
                //building the packing algorithm
                LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bins, true, true, true);
                List<Container> fits = packager.packList(boxes, 1, Long.MAX_VALUE);
                System.out.println(fits.toString());
                
            }
            catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            }
        }
    }
   
    //May need this later, but its functionality is vurrently under sortAndAddToStorageRoom
    
//    public static List<BoxItem> IDToBoxItem(String dataBaseName, List<String> BoxID){
//        System.out.println("Start converting ID's to box items");
//        Connection c = null;
//        Statement stmt = null;
//        
//        //Create a list of the boxes
//        List<BoxItem> products = new ArrayList<>();
//        
//        try{
//            //Connect to database
//            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
//            System.out.println("Connected to database");
//            stmt = c.createStatement();
//            
//            //Go through the given list of ID's one at a time, gathering information about the boxes and putting that information in to the outgoing list of BoxItems
//            for(int i = 0; i < BoxID.size(); i++){
//                ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = " + BoxID.get(i) + ";");
//                products.add(new BoxItem(new Box(rs.getString(2), rs.getInt(4), rs.getInt(6), rs.getInt(5), 0), 1));
//            }
//            
//            //Close the connections to the database
//            stmt.close();
//            c.close();
//            System.out.println("Database connection closed");
//            
//            //Return the created list of products
//            return products;
//        }
//        catch(Exception e){
//            //Exception catching
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        //If somthing goes wrong, return null
//        return null;   
//    }
}
