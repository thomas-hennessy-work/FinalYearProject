package tom.sros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StorageRoomDatabase {
    
    public static void addToStorageRoom(String storedBoxID){
        
        Connection c = null;
        Statement stmt = null;
        //StorageRoomDatabase SRDB = new StorageRoomDatabase();
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
            stmt = c.createStatement();
            
            c.setAutoCommit(false);
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM boxType WHERE box_ID = " + storedBoxID + ";");
            String boxID= rs.getString(1);
            System.out.println(rs.isClosed());
            System.out.println("no problems");
            
            //c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
            //stmt = c.createStatement();
            
            BinData BD = new BinData();
            int height = BD.givePosition(storedBoxID);
            

            String sql = "INSERT INTO boxLocation (box_ID,bin_number,corner_vertical_pos,corner_horizontal_pos,corner_depth_pos)"
                    + "VALUES ('" + boxID + "', '1', '" + height + "', '0', '0');";
            stmt.executeUpdate(sql);
            System.out.println("Insert completed");
            c.commit();
            
            stmt.close();
            c.close();
        }
        catch(Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    /*
    private static int generateID(){
        Connection c = null;
        Statement stmt = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
            stmt = c.createStatement();
            
            ResultSet results = stmt.executeQuery("SELECT individual_ID FROM boxLocation ORDER BY individual_ID DESC");
            int generatedID = results.getInt(1) + 1;
            
           //     if(generatedID != null){
           //         generatedID = results.getInt(1) + 1;
            //        System.out.println("no problems");
           //     }else{
           //         generatedID = 1;
           //         System.out.println("no problems");
           //     }
           return generatedID;
        }
        catch(Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return 1;
    }*/
}
