package tom.sros.storageRoom;

import com.github.skjolber.packing.Container;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.Node;
import javafx.util.Pair;

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
    
    public static List<Box> sorter(List<Box> unsortedBoxes, List<Bin> binsAvailable){
        
        while(unsortedBoxes.size() > 1 && binsAvailable.size() > 1){

            List<Bin> tallestBins = new ArrayList<Bin>();
            //obtaining a list of the tallest bins available
            for (Bin currentBin: binsAvailable){
                if (tallestBins.get(0).getHeight() <= currentBin.getHeight()){
                    tallestBins.clear();
                    tallestBins.add(currentBin);
                }
                else if(tallestBins.get(0).getHeight() == currentBin.getHeight())
                    tallestBins.add(currentBin);     
            }
            
            Node root;
        }
        //Error blocking
        return unsortedBoxes;
    }
    //When you add a box to a top left corner, there will be two quadrilaterals left over,
    //one bello and one to the right. Recursivly doing this allows you to fit boxes in to 
    //these spaces. The tree stores the space available.
    
    
    //https://www.baeldung.com/java-binary-tree
    class Node{
        Space binArea;
        Box storedBox;
        Node below;
        Node right;
        
        Node(Space binArea){
            this.binArea = binArea;
            storedBox = null;
            below = null;
            right = null;
        }
        Node(Space binArea, Box item){
            this.binArea = binArea;
            storedBox = null;
            below = null;
            right = null;
        }
        
        public void setStoredBox(Box item){
            storedBox = item;
        }
        
        public Space getBinArea(){
            return binArea;
        }
        public Box getPlacedBox(){
            return storedBox;
        }
    }
    
    private Node setFirstNode(Node firstNode, Space binArea){
        return new Node(binArea);
    }
    
    private Node addBoxRecursive(Node currentNode, Box item){
        
        //
        //
        //
        //Turn this in to a switch statement
        //
        //
        //
        
        //The algorithm checks right then bellow, due to the right being more likely to have less space
        
        //The box checks if it is able to fit in the boxes bellow, even if they fit. This
        //is due to the space constantly becoming smaller as boxes are added. If it is unable
        //to fit in the space that is already taken, it will not fit in a smaller space. This
        //eliminates unnececery recursion
        
        //If there is no box in the current node AND the box fits, place it here
        if (currentNode.getPlacedBox() == null && currentNode.getBinArea().canFit(item.getArea())){
            //Creating nodes bellow
            currentNode.below = new Node(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()));
            currentNode.right = new Node(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()));
            //placing box in current node
            return new Node(item.getArea());
        }
        //If the box can fit in the node bellow, move to the node right
        else if (currentNode.right.getBinArea().canFit(item.getArea())){
            currentNode.right = addBoxRecursive(currentNode.right, item);
        }
        //If the box can fit in the node bellow, move to the node bellow
        else if (currentNode.below.getBinArea().canFit(item.getArea())){
            currentNode.below = addBoxRecursive(currentNode.below, item);
        }
        
        return null;
    }
    
}
