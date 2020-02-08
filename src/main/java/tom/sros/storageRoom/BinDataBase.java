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

public class BinData {
    
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
    
//    public List<Container> givePosition3dbp(String dataBaseName, List<String> box_IDs){
//        
//        //The section that defines the containers in the algorithm.
//        //ADD A WAY OF MODIFYING THIS
//        List<Container> containers = new ArrayList<>();
//        
//    }
    
    
    public static void test3DBP(){
        System.out.println("Testing the 3D Bin Packing library");
        
        List<Container> containers = new ArrayList<>();
        containers.add(new Container(10, 10, 3, 0));
        LargestAreaFitFirstPackager packager = new LargestAreaFitFirstPackager(containers, true, true, true);

        List<BoxItem> products = new ArrayList<>();	
        products.add(new BoxItem(new Box("Foot", 6, 10, 2, 0), 2));
        products.add(new BoxItem(new Box("Leg", 4, 10, 1, 0), 2)); 
        products.add(new BoxItem(new Box("Arm", 4 , 10, 2, 0), 1));
        
        //when you don't care about the number of bins you need, use Integer.MAX_VALUE
        List<Container> fits = packager.packList(products, 2, Long.MAX_VALUE);
        System.out.println(fits);
    }
    
}
