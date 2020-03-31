package tom.sros.storageRoom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
import tom.sros.sorter.binaryTree;

public class newAlgorithm {
    
    public static void addBoxToDatabase(String dataBaseName, List<Pair> unsortedBoxesAndAmounts){
        Connection c = null;
        Statement stmt = null; 
        
        List<Pair> binSpecifications = BinDataBase.getAllBinInfo(dataBaseName);
        
        try{
            //Connect to database
            c = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName);
            System.out.println("Connected to database");
            
            //Create a list for all the boxes that need to be sorted and their ID's
            List<Box> boxes = new ArrayList();
            List<String> IDList = new ArrayList();
            
            for(Pair boxAndAmount: unsortedBoxesAndAmounts){
                String boxID = (String) boxAndAmount.getKey();
                int boxAmount = (int) boxAndAmount.getValue();
                
                //Gather box dimensions from the database
                ResultSet rs = stmt.executeQuery("Select width, length, height FROM boxType WHERE box_ID = " + boxID);
                float width = rs.getInt("width");
                float length = rs.getInt("length");
                float height = rs.getInt("height");
                
                //Adding the box to the list of boxes
                for(int i = 0; i< boxAmount ; i++){
                    Box newBox = new Box(boxID, width, length, height);
                    boxes.add(newBox);
                    IDList.add(boxID);
                }
            }
            
            List<Bin> bins = new ArrayList<>();
            
            for(Pair binSpecs :binSpecifications){
                String binTypeID = (String) binSpecs.getKey();
                System.out.println("adding bin ID " + binTypeID);
                
                ResultSet rs = stmt.executeQuery("SELECT width, length, height, bin_ID FROM binType INNER JOIN binIndividual ON binType.type_ID = binIndividual.type_ID WHERE binType.type_ID = " + binTypeID + ";");
                System.out.println("Bin data gathered");
                float width = rs.getInt("width");
                float length = rs.getInt("length");
                float height = rs.getInt("height");
                
                while(rs.next()){
                    bins.add(new Bin(rs.getString("bin_ID"), width, height, length));
                    System.out.println("Individual ID = " + rs.getString("bin_ID") + ", width = " + width + ", height = " + height + ", length = " + length);
                }
            }
            
            //Create tables if they do not exist
            stmt = c.createStatement();
            String sql = "INSERT INTO ";
        }
        catch (Exception e){
            //Error catching
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public List<Box> sorter(List<Box> unsortedBoxes, List<Bin> binsAvailable){
        
        List<Box> allBoxes = unsortedBoxes;
        List<Box> boxList = new ArrayList<>();
        List<Box> freshSortBoxes;
        binaryTree BT = new binaryTree();
        
        while(!allBoxes.isEmpty() && !binsAvailable.isEmpty()){
            List<Bin> tallestBins = new ArrayList<>();
            //obtaining a list of the tallest bins available
            binsAvailable.forEach((currentBin) -> {
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
                freshSortBoxes = binaryTree.sort2DBP(currentBin.getArea(), allBoxes);
                System.out.println("Size of freshSortBoxes = " + freshSortBoxes.size());
                for(Box currentBox: freshSortBoxes){
                    //Remove any boxes that have been sorted from the unsorted list
                    //System.out.println("Name: " + currentBox.getName() + "/nX: " + currentBox.getX() + "/nY: " + currentBox.getY() + "/nArea: " + currentBox.getArea().toString());
                    allBoxes.remove(currentBox);
                    boxList.add(currentBox);
                }
                binsAvailable.remove(currentBin);
            }
        }
        System.out.println(boxList.toString());
        return Collections.reverse(boxList);
    }
}
