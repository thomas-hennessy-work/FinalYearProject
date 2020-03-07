package tom.sros.storageRoom;

import com.github.skjolber.packing.Box;
import com.github.skjolber.packing.BoxItem;
import com.github.skjolber.packing.BruteForcePackager;
import com.github.skjolber.packing.Container;
import com.github.skjolber.packing.LargestAreaFitFirstPackager;
import com.github.skjolber.packing.Level;
import com.github.skjolber.packing.Packager;
import com.github.skjolber.packing.Placement;
import com.github.skjolber.packing.Space;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class StorageRoomDatabase {
    String dataBaseName = ("SROSData.db");
    
    public void test3dbp(String dataBaseName){
        Connection c = null;
        Statement stmt = null;
        
        List<BoxItem> products = new ArrayList<BoxItem>();
        //products.add(new BoxItem(new Box("item 1", 99, 99, 99, 0), 1));
        
        Space testSpace = new Space();
        testSpace.setDepth(99);
        testSpace.setHeight(99);
        testSpace.setWidth(99);
        testSpace.setX(0);
        testSpace.setY(0);
        testSpace.setZ(0);

        List<Container> bins = new ArrayList<Container>() {{
            add(new Container("Bin 1", 200, 200, 200, 0));
            //add(new Container("Bin 1", 200, 200, 200, 0).add(new Placement(testSpace, new Box("item 1", 99, 99, 99, 0))));
            //Placement is the box's position in the level and the space
        }};
        Level x = new Level();
        x.add(new Placement(testSpace, new Box("item 1", 99, 99, 99, 0)));
        bins.get(0).add(x);
        
        LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bins, true, false, true);
        List<Container> fits = packager.packList(products, 1, Long.MAX_VALUE);
        //In LAFFP there is a method called get free space
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();

            for(int c1 = 0 ; c1 < fits.size() ; c1++){
                    for(int c2 = 0 ; c2 < fits.get(c1).getLevels().size() ; c2++){
                        for(int c3 = 0 ; c3 < fits.get(c1).getLevels().get(c2).size() ; c3++){
                            String boxName = fits.get(c1).getLevels().get(c2).get(c3).getBox().getName();
                            String binName = fits.get(c1).getName();
                            int xCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getX();
                            int yCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getY();
                            int zCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getZ();
                            Level lvlHeight = fits.get(c1).getLevels().get(c2);
                            Placement place = fits.get(c1).getLevels().get(c2).get(0);
                            Space area = fits.get(c1).getLevels().get(c2).get(0).getSpace().getRemainder();
                            //Level height is the amount of space the level verticaly ocupies, not the vertical position of the level

                            System.out.println("Name: " + boxName + "\nX coordinate: " + xCords + "\nY coordinate: " + yCords + "\nZ coordinate: " + zCords);
                            System.out.println("level height: " + lvlHeight);
                            System.out.println("Placement: " + place);
                            System.out.println("Space: " + area);

                            //When possible, remove this connection from the empty bin try catch
                            String sql = "INSERT INTO boxLocation (box_ID, bin_ID,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
                                                                           //Change once bin ID implemented
                                    + "Values ('" + boxName + "', '" + binName + "', '" + yCords + "', '" + xCords + "', '" + zCords + "' );";
                            stmt.executeUpdate(sql);
                                    }
                                }
                            }
            
            System.out.println(fits);
        }
            catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }   
    }
//    {
//        List<BoxItem> items = new ArrayList<BoxItem>();
//        items.add(new BoxItem(new Box("1", 10, 10, 10, 0), 50));
//        
//        List<Container> bins = new ArrayList<Container>();
//        bins.add(new Container("1", 40, 40, 30, 0));
//        bins.add(new Container("2", 40, 40, 30, 0));
//        bins.add(new Container("3", 40, 40, 30, 0));
//        
//        LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bins, true, false, true);
//        //Packager packager = LargestAreaFitFirstPackager.newBuilder().withContainers(bins).build();
//        List<Container> fits = packager.packList(items, 1, Long.MAX_VALUE);
//        System.out.println(fits.toString());
//    }
    
    public static void sortAndAddToStorageRoom(String dataBaseName, List<Pair> unsortedBoxesAndAmounts){
    System.out.println("\nStart sort and add to storage room");
        Connection c = null;
        Statement stmt = null;
        
        //Gather data about the bins from the get all bin info function
        
        //
        //
        //
        // BIN SPECIFICATIONS NO LONGER NEED TO BE A PAIR JUST NEED ID
        //
        //
        //
        
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
            
            //go through each bin type given in the list of pairs "binspecifications"
            for(int i = 0 ; i < binSpecifications.size() ; i ++){
                String binTypeID = (String) binSpecifications.get(i).getKey();
                System.out.println("adding bin ID " + binTypeID);
            
                //gather the mesurements of the bin
                ResultSet rs = stmt.executeQuery("SELECT width, length, height, bin_ID FROM binType INNER JOIN binIndividual ON binType.type_ID = binIndividual.type_ID WHERE binType.type_ID = " + binTypeID + ";");
                System.out.println("Bin data gathered");
                width = rs.getInt("width");
                length = rs.getInt("length");
                height = rs.getInt("height");
                
                while(rs.next()){
                    bins.add(new Container(rs.getString("bin_ID"), width, height, length, 0));
                    System.out.println("Individual ID = " + rs.getString("bin_ID") + ", width = " + width + ", height = " + height + ", length = " + length);
                }
            }
                
            //building the packing algorithm using the information gathered
            LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(bins, true, false, true);
            List<Container> fits = packager.packList(boxes, 1, Long.MAX_VALUE);
            System.out.println(boxes);
            System.out.println(bins);
            
            //Three for loops to dig in to the data about the boxes
            for(int c1 = 0 ; c1 < fits.size() ; c1++){
                for(int c2 = 0 ; c2 < fits.get(c1).getLevels().size() ; c2++){
                    for(int c3 = 0 ; c3 < fits.get(c1).getLevels().get(c2).size() ; c3++){
                        String boxName = fits.get(c1).getLevels().get(c2).get(c3).getBox().getName();
                        String binName = fits.get(c1).getName();
                        int xCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getX();
                        int yCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getY();
                        int zCords = fits.get(c1).getLevels().get(c2).get(c3).getSpace().getZ();
                                    
                        System.out.println("Name: " + boxName + "\nX coordinate: " + xCords + "\nY coordinate: " + yCords + "\nZ coordinate: " + zCords);
                                    
                        //When possible, remove this connection from the empty bin try catch
                        String sql = "INSERT INTO boxLocation (box_ID, bin_ID,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
                                                                       //Change once bin ID implemented
                                + "Values ('" + boxName + "', '" + binName + "', '" + yCords + "', '" + xCords + "', '" + zCords + "' );";
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
}