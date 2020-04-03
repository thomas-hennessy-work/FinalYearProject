package tom.sros.storageRoom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
import tom.sros.sorter.binaryTree;

public class newAlgorithm {
    
//    public static void sortAndAddToDB(String dataBaseName, List<Pair> unsortedBoxesAndAmounts){
//        Connection c = null;
//        Statement stmt = null; 
//        
//        List<Pair> binSpecifications = BinDataBase.getAllBinInfo(dataBaseName);
//        
//        try{
//            //Connect to database
//            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
//            stmt = c.createStatement();
//            System.out.println("Connected to database");
//            
//            //Create a list for all the boxes that need to be sorted and their ID's
//            List<Box> boxes = new ArrayList();
//            List<String> IDList = new ArrayList();
//            
//            for(Pair boxAndAmount: unsortedBoxesAndAmounts){
//                String boxID = (String) boxAndAmount.getKey();
//                int boxAmount = (int) boxAndAmount.getValue();
//                
//                //Gather box dimensions from the database
//                ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
//                float width = rs.getInt("width");
//                float length = rs.getInt("length");
//                float height = rs.getInt("height");
//                
//                //Adding the box to the list of boxes
//                for(int i = 0; i< boxAmount ; i++){
//                    Box newBox = new Box(boxID, width, length, height);
//                    boxes.add(newBox);
//                    IDList.add(boxID);
//                }
//            }
//            
//            List<Bin> bins = new ArrayList<>();
//            
//            for(Pair binSpecs :binSpecifications){
//                String binTypeID = (String) binSpecs.getKey();
//                System.out.println("adding bin ID " + binTypeID);
//                
//                ResultSet rs = stmt.executeQuery("SELECT width, length, height, bin_ID FROM binType INNER JOIN binIndividual ON binType.type_ID = binIndividual.type_ID WHERE binType.type_ID = " + binTypeID + ";");
//                System.out.println("Bin data gathered");
//                float width = rs.getInt("width");
//                float length = rs.getInt("length");
//                float height = rs.getInt("height");
//                
//                while(rs.next()){
//                    bins.add(new Bin(rs.getString("bin_ID"), width, height, length));
//                    System.out.println("Individual ID = " + rs.getString("bin_ID") + ", width = " + width + ", height = " + height + ", length = " + length);
//                }
//            }
//            
//        }
//        catch (Exception e){
//            //Error catching
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        
//        
//        
//    }
    
   public List<Box> gatherBoxInformation(String dataBaseName, List<Pair> IDAmountList){
       
        Connection c = null;
        Statement stmt = null;
        
        float width = 0;
        float length = 0;
        float height = 0;
        
        List<Box> unsortedBoxes = new ArrayList<>();
       
        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            stmt = c.createStatement();
            System.out.println("Connected to database");
            
            for(int y=0 ; y < IDAmountList.size() ; y++){
                String boxID = (String) IDAmountList.get(y).getKey();
                int boxAmount = (int) IDAmountList.get(y).getValue();

                //gather box dimensions from db
                ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
                width = rs.getInt("width");
                length = rs.getInt("length");
                height = rs.getInt("height");

                //add the box to the list
                for(int i = 0; i <= boxAmount ; i++){
                    unsortedBoxes.add(new Box(boxID, width, length, height));
                    //Adding the ID to a list for later identification
                }
            }
            stmt.close();
            c.close();
        }
        catch (SQLException e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return unsortedBoxes;
   }
    
   public List<Box> asignBoxInformation(String dataBaseName, List<Box> Boxes){
       Connection c = null;
       Statement stmt = null;
       
       try{
           c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
           System.out.println("Connected to database");
           
           stmt = c.createStatement();
           
           for(Box currentBox: Boxes){
               String boxName = currentBox.getName();
               ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxName);
               currentBox.setWidth(rs.getFloat("width"));
               currentBox.setLength(rs.getFloat("length"));
               currentBox.setHeight(rs.getFloat("height"));
           }
           stmt.close();
           c.close();
       }
       catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
        }
        return Boxes;
   }
   
   
    public List<Box> sortAndAddToDB(String dataBaseName, List<Pair> IDAmountList){
    
    List<Box> returnBoxList = new ArrayList<>();
    List<Box> freshSortBoxes;
    
    Connection c = null;
    Statement stmt = null;
        
    //obtaining bin information
    List<Bin> binSpecifications = BinDataBase.getGeneralBinInfo(dataBaseName);
        
//    try{
//        c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
//        stmt = c.createStatement();
//        System.out.println("Connected to database");
//            
//        int width = 0;
//        int length = 0;
//        int height =0;
//        
//        //Create a list of all the boxes to be sorted
//        List<Box> unsortedBoxes = new ArrayList<>();
//        List<String> IDList = new ArrayList<>();
//            
//        for(int y=0 ; y < IDAmountList.size() ; y++){
//            String boxID = (String) IDAmountList.get(y).getKey();
//            int boxAmount = (int) IDAmountList.get(y).getValue();
//                    
//            //gather box dimensions from db
//            ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
//            width = rs.getInt("width");
//            length = rs.getInt("length");
//            height = rs.getInt("height");
//                    
//            //add the box to the list
//            for(int i = 0; i <= boxAmount ; i++){
//                unsortedBoxes.add(new Box(boxID, width, length, height));
//                //Adding the ID to a list for later identification
//                IDList.add(boxID);
//            }
//        }
//            
//        //go through each bin type given in the list of pairs "binspecifications"
//            for(int i = 0 ; i < binSpecifications.size() ; i ++){
//                String binTypeID = (String) binSpecifications.get(i).getKey();
//                System.out.println("adding bin ID " + binTypeID);
//            
//                //gather the mesurements of the bin
//                ResultSet rs = stmt.executeQuery("SELECT width, length, height, bin_ID FROM binType INNER JOIN binIndividual ON binType.type_ID = binIndividual.type_ID WHERE binType.type_ID = " + binTypeID + ";");
//                System.out.println("Bin data gathered");
//                width = rs.getInt("width");
//                length = rs.getInt("length");
//                height = rs.getInt("height");
//                
//                while(rs.next()){
//                    binsAvailable.add(new Bin(binTypeID, width, length, height));
//                    System.out.println("Individual ID = " + rs.getString("bin_ID") + ", width = " + width + ", height = " + height + ", length = " + length);
//                }
//            }
            
        while(!unsortedBoxes.isEmpty() && !binSpecifications.isEmpty()){
            List<Bin> tallestBins = new ArrayList<>();
            //obtaining a list of the tallest bins available
            binSpecifications.forEach((currentBin) -> {
                if (tallestBins.isEmpty()){
                    tallestBins.add(currentBin);
                }
                else if (tallestBins.get(0).getHeight() < currentBin.getHeight()){
                    tallestBins.clear();
                    tallestBins.add(currentBin);
                }
                else if(tallestBins.get(0).getHeight() == currentBin.getHeight())
                    tallestBins.add(currentBin);
            });

            //Sort the boxes available in to the curent talest bin
            for (Bin currentBin: tallestBins){
                freshSortBoxes = binaryTree.sort2DBP(currentBin, unsortedBoxes);
                System.out.println("Size of freshSortBoxes = " + freshSortBoxes.size());
                for(Box currentBox: freshSortBoxes){
                    //Remove any boxes that have been sorted from the unsorted list
                    //System.out.println("Name: " + currentBox.getName() + "/nX: " + currentBox.getX() + "/nY: " + currentBox.getY() + "/nArea: " + currentBox.getArea().toString());
                   unsortedBoxes.remove(currentBox);
                   returnBoxList.add(currentBox);
                }
                binSpecifications.remove(currentBin);
            }
//        }
//        stmt.close();
//        c.close();
//    }
    catch (SQLException e){
        //Error catching
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }
        System.out.println(returnBoxList.toString());
        return returnBoxList;
    }
}
