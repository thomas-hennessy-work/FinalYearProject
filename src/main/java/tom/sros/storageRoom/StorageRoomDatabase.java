package tom.sros.storageRoom;

import com.github.skjolber.packing.Box;
import com.github.skjolber.packing.BoxItem;
import com.github.skjolber.packing.Container;
import com.github.skjolber.packing.LargestAreaFitFirstPackager;
import com.github.skjolber.packing.Packager;
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
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            System.out.println("Connected to database");
            
            int width = 0;
            int length = 0;
            int height =0;
        
            //Create a list of all the boxes to be sorted
            List<BoxItem> boxes = new ArrayList<>();
            List<String> IDList = new ArrayList<>();
            for(int y=0 ; y < unsortedBoxesAndAmounts.size() ; y++){
                String boxID = (String) unsortedBoxesAndAmounts.get(y).getKey();
                int boxAmount = (int) unsortedBoxesAndAmounts.get(y).getValue();
                    
                //gather box dimensions from db
                ResultSet rs = stmt.executeQuery("Select name, width, length, height FROM boxType WHERE box_ID = " + boxID);
                width = rs.getInt("width");
                length = rs.getInt("length");
                height = rs.getInt("height");
                String name = rs.getString("name");
                System.out.println(name);
                    
                //add the box to the list
                boxes.add(new BoxItem(new Box(name, width, length, height, 0), boxAmount));
                //Adding the ID to a list for later identification
                IDList.add(boxID);
            }
        
            //emptying the width height and length variables
            width = 0;
            length = 0;
            height = 0;

            
            //go through each bin given in the list of pairs "binspecifications"
            for(int i = 0 ; i < binSpecifications.size() ; i ++){
                String binID = (String) binSpecifications.get(i).getKey();
                System.out.println("adding bin ID " + binID);
                int binAmount = (int) binSpecifications.get(i).getValue();
            
                //gather the mesurements of the bin
                ResultSet rs = stmt.executeQuery("SELECT width, length, height FROM binType WHERE type_ID = " + binID + ";");
                System.out.println("Bin data gathered");
                width = rs.getInt("width");
                length = rs.getInt("length");
                height = rs.getInt("height");
                
                //Add the bin for this for loop a number of times 
                List<Container> bins = new ArrayList<>();
                for(int x=0 ; x < binAmount ; x++){
                    bins.add(new Container(width, height, length, 0));
                }
                
                //a boolean to track if the bins being calcualted are full
                boolean full = false;
                
                //Sorting boxes individualy
                for(int f = 0 ; (f < boxes.size()) && (full == false); f++){
                    String curentID = IDList.get(f);
                    
                    List<BoxItem> indiBox = new ArrayList<>();
                    indiBox.add(boxes.get(f));
                    
                    //Try used to catch the null pointer that indicates the bins are full
                    try{
                        //building the packing algorithm using the information gathered
                        LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bins, true, true, true);
                        List<Container> fits = packager.packList(indiBox, 1, Long.MAX_VALUE);
                        
                        for(int c1 = 0 ; c1 < fits.size() ; c1++){
                            for(int c2 = 0 ; c2 < fits.get(c1).getLevels().size() ; c2++){
                                for(int c3 = 0 ; c3 < fits.get(c1).getLevels().get(c2).size() ; c3++){
                                    String name = fits.get(c1).getLevels().get(c2).get(c3).getBox().getName();
                                    int xCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getX();
                                    int yCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getY();
                                    int zCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getZ();
                                    
                                    System.out.println("Name: " + name + "\nX coordinate: " + xCords + "\nY coordinate: " + yCords + "\nZ coordinate: " + zCords);
                                    
                                    //When possible, remove this connection from the empty bin try catch
                                    String sql = "INSERT INTO boxLocation (box_ID, bin_number,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
                                                                       //Change once bin ID implemented
                                            + "Values ('" + curentID + "', '1', '" + yCords + "', '" + xCords + "', '" + zCords + "' );";
                                    stmt.executeUpdate(sql);
                                }
                            }
                        }
                        //Once the box has been sorted, remove it from the list
                        boxes.remove(indiBox.get(0));
                        
                        System.out.println(fits.toString());
                    }
                    catch (Exception e){
                        System.out.println("Bin full");
                        full = true;
                    }
                    
                    //Use a for each loop in a for each loop in a for each loop, till you get the box
                    //for(int p = 0 ; p < fits.get(0).getLevels().get(0).size() ; p++){
                        //Get container from list then levels then get placement then one to one relationship to get box then get specific value
                        //System.out.println(fits.get(0).getLevels().get(0).get(p).getBox().getDepth());
                        //System.out.println(fits.get(0).getLevels().get(0).get(p).getBox().getWidth());
                        //System.out.println(fits.get(0).getLevels().get(0).get(p).getBox().getHeight());
                        //System.out.println(fits.get(0).getLevels().get(0).get(p).setBox(box));
                    //}
                }
            }
            stmt.close();
            c.close();
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }   
    }
    
    
    
    
    
    
    
    public static void reDone3DBP(String dataBaseName, List<Pair> unsortedBoxesAndAmounts){
    System.out.println("\nStart sort and add to storage room");
        Connection c = null;
        Statement stmt = null;
        
        //Gather data about the bins from the get all bin info function
        List<Pair> binSpecifications = BinDataBase.getAllBinInfo(dataBaseName);
        System.out.println(binSpecifications.size());
        
        //Is it better to make multiple small conections to the database ore one big one
        //Should I just ignore the getAllBinInfo function and gather all the information here
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            System.out.println("Connected to database");
            
            int width = 0;
            int length = 0;
            int height =0;
        
            //Create a list of all the boxes to be sorted
            List<BoxItem> boxes = new ArrayList<>();
            List<String> IDList = new ArrayList<>();
            for(int y=0 ; y < unsortedBoxesAndAmounts.size() ; y++){
                String boxID = (String) unsortedBoxesAndAmounts.get(y).getKey();
                int boxAmount = (int) unsortedBoxesAndAmounts.get(y).getValue();
                    
                //gather box dimensions from db
                ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
                width = rs.getInt("width");
                length = rs.getInt("length");
                height = rs.getInt("height");
                    
                //add the box to the list
                boxes.add(new BoxItem(new Box(boxID, width, length, height, 0), boxAmount));
                //Adding the ID to a list for later identification
                IDList.add(boxID);
            }
        
            //emptying the width height and length variables
            width = 0;
            length = 0;
            height = 0;
            
            List<Container> bins = new ArrayList<>();
            
            //go through each bin given in the list of pairs "binspecifications"
            for(int i = 0 ; i < binSpecifications.size() ; i ++){
                String binID = (String) binSpecifications.get(i).getKey();
                System.out.println("adding bin ID " + binID);
                int binAmount = (int) binSpecifications.get(i).getValue();
            
                //gather the mesurements of the bin
                ResultSet rs = stmt.executeQuery("SELECT width, length, height FROM binType WHERE type_ID = " + binID + ";");
                System.out.println("Bin data gathered");
                width = rs.getInt("width");
                length = rs.getInt("length");
                height = rs.getInt("height");
                
                //Add the bin for this for loop a number of times 
                
                for(int x=0 ; x < binAmount ; x++){
                    bins.add(new Container(binID, width, height, length, 0));
                }
            }
                
            //building the packing algorithm using the information gathered
            LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bins, true, true, true);
            List<Container> fits = packager.packList(boxes, 1, Long.MAX_VALUE);
            
            for(int c1 = 0 ; c1 < fits.size() ; c1++){
                for(int c2 = 0 ; c2 < fits.get(c1).getLevels().size() ; c2++){
                    for(int c3 = 0 ; c3 < fits.get(c1).getLevels().get(c2).size() ; c3++){
                        String name = fits.get(c1).getLevels().get(c2).get(c3).getBox().getName();
                        int xCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getX();
                        int yCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getY();
                        int zCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getZ();
                                    
                        System.out.println("Name: " + name + "\nX coordinate: " + xCords + "\nY coordinate: " + yCords + "\nZ coordinate: " + zCords);
                                    
                        //When possible, remove this connection from the empty bin try catch
                        String sql = "INSERT INTO boxLocation (box_ID, bin_number,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
                                                                       //Change once bin ID implemented
                                + "Values ('" + name + "', '1', '" + yCords + "', '" + xCords + "', '" + zCords + "' );";
                        stmt.executeUpdate(sql);
                                }
                            }
                        }
                
            System.out.println(fits);

            stmt.close();
            c.close();
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }   
    }
   
    //May need this later, but its functionality is currently under sortAndAddToStorageRoom
    
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