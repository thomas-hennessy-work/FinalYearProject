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

public class StorageRoomDatabase {
    
    public static void addToStorageRoom(String dataBaseName, String storedBoxID){
        
        Connection c = null;
        Statement stmt = null;
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            stmt = c.createStatement();
            
            //Gather the information from the database about the given box
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = " + storedBoxID + ";");
            String boxID = rs.getString(1);
            System.out.println("Gathered informaton about box");
            
            BinData BD = new BinData();
            int height = BD.givePosition(dataBaseName, storedBoxID);
            
            //Insert the box in to the storage room
            String sql = "INSERT INTO boxLocation (box_ID,bin_number,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
                    + "VALUES ('" + boxID + "', '1', '" + height + "', '0', '0');";
            stmt.executeUpdate(sql);
            System.out.println("Insert completed");
            
            stmt.close();
            c.close();
            System.out.println("Database connection closed");
        }
        catch(Exception e){
            //Exception catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
