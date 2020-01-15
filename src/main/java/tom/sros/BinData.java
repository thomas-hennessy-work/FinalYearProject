package tom.sros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BinData {
    
    public int givePosition(String box_ID){
        Statement stmt = null;
        Connection c = null;
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite:SROSData.db");
            System.out.println("Database opened succesfully");
            stmt = c.createStatement();
            
            ResultSet LocationData = stmt.executeQuery("SELECT width, length, height FROM boxType WHERE box_Id = " + box_ID + ";");
            int width = LocationData.getInt(1);
            int length = LocationData.getInt(2);
            int height = LocationData.getInt(3);
            
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
            return Total;
        }
        catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
        }
        return 0;
    }
}
