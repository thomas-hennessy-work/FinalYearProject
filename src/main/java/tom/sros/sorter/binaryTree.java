package tom.sros.sorter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import tom.sros.item.ItemDatabase;


public class binaryTree {
    
    Node root;
    Bin currentBin;
    //Used so that if a box is not placed (due to a pre-existing one being placed instead),
    //it is sorted again.
    static Boolean unplaced = true;
    static List<Box> existingBoxes = null;
    static List<EmptySpace> unocupiedSpace = null;
   

    class Node{
        Space binArea;
        Box storedBox;
        EmptySpace unusedSpace;
        Node below;
        Node right;
       
        //Node constructors
        Node(Space binArea, Box item){
            this.binArea = binArea;
            storedBox = item;
            unusedSpace = null;
            below = null;
            right = null;
        }
        Node(Space binArea, EmptySpace emptySpace){
            this.binArea = binArea;
            storedBox = null;
            unusedSpace = emptySpace;
            below = null;
            right = null;
        }
        
        //Get methods
        private Space getBinArea(){
            return binArea;
        }
        private Box getPlacedBox(){
            return storedBox;
        }
        private EmptySpace getEmptySpace(){
            return unusedSpace;
        }
        
    }
    
    /**
     * Method used to add boxes to the binary tree. Always begins at root node
     * 
     * @param item
     * @param binSpace
     * @param dataBaseName 
     */
    private void add(Box item, Space binSpace, String dataBaseName){
        root = addBoxRecursive(root, item, binSpace, dataBaseName);
    }
    
    /**
     * 2D sorting algorithm. Takes the item that needs to be stored in bin and finds an empty node. The empty node contains a position
     * in the bin and once that position is reached, it checks for anything stored there. If a box is stored there from the database, then the
     * box from the database will be placed there and the box being placed will go through the binary tree once again. If there is nothing there,
     * the box will be placed, if it can fit in the area given. If an empty space is stored there, then the box will be placed in the empty space, if it can fit
     * and new empty space objects will be placed in the binary tree to represent any area not filled by the new box.
     * 
     * @param currentNode
     * @param item
     * @param binSpace
     * @param dataBaseName
     * @return Node with location information and whit is placed in that location, this could be either a box, an empty space or nothing.
     */
    private Node addBoxRecursive(Node currentNode, Box item, Space binSpace, String dataBaseName){      
        //The box checks if it is able to fit in the boxes bellow, even if they fit. This
        //is due to the space constantly becoming smaller as boxes are added. If it is unable
        //to fit in the space that is already taken, it will not fit in a smaller space. This
        //eliminates unnececery recursion
        //If the current node is null
        
        if (currentNode == null){
            //Checking if any pre-existing boxes are located in this position.
            Box placedBox = findPlacedBox(existingBoxes, item.getX(), item.getZ());
            EmptySpace placedSpace = findEmptySpace(unocupiedSpace, item.getX(), item.getZ());
            if(placedBox != null){
                return new Node(binSpace, placedBox);
            }
            //If an empty space is located in the current node. Tries to place the new box within the empty space
            //If it cannot fit, it places the empty space in the binary tree.
            if(placedSpace != null){
                ItemDatabase ITDB = new ItemDatabase();
                
                if(placedSpace.getArea().canFit(item.getArea())){
                    //Removes old empty space and regesteres new empty spaces. The new empty spaces are then
                    //each added to the empty space list in the binary tree
                    (ITDB.fillEmptySpace(placedSpace, item, dataBaseName)).forEach((currentEmptySpace) -> {
                        unocupiedSpace.add(currentEmptySpace);
                    });
                    unplaced = false;
                    item.setBin(currentBin.getName());
                    return new Node(binSpace, item);
                }else{
                    return new Node(binSpace, placedSpace);
                }
            }
            if(placedSpace == null && placedBox == null){
            //Provides information about where the bin is stored and also prevents it from being re-sorted
                item.setBin(currentBin.getName());
                unplaced = false;
                return new Node(binSpace, item);
            }
        }

        //If the box can fit in the node to the right, but there is no node right, move to the node right
        //The comparison to current bin is used to check if the box has been placed
        if ((currentNode.getPlacedBox() != null) && (item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            //itterativly adding to the position of the item
            item.setX(roundFloat(item.getX() + currentNode.getPlacedBox().getWidth()));
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()), dataBaseName);
            
            //If the box is not placed and it comes back through the binary tree, the itterative position is undone
            if(item.getBin() != (currentBin.getName())){
                item.setX(roundFloat(item.getX() - currentNode.getPlacedBox().getWidth()));
            }
        } 
        //Same as the initial if statement, only it executes on empty spaces rather than stored boxes
        else if((currentNode.getEmptySpace()!= null) && (item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            //itterativly adding to the position of the item
            item.setX(roundFloat(item.getX() + currentNode.getEmptySpace().getWidth()));
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getEmptySpace().getArea()), dataBaseName);
            
            //If the box is not placed and it comes back through the binary tree, the itterative position is undone
            if(item.getBin() != (currentBin.getName())){
                item.setX(roundFloat(item.getX() - currentNode.getEmptySpace().getWidth()));
            }
        }
        
        //If the box can fit in the node bellow, but there is no node already bellow, move to the node bellow
        if ((currentNode.getPlacedBox() != null) && (item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            item.setZ(roundFloat(item.getZ() + currentNode.getPlacedBox().getLength()));
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()), dataBaseName);
            
            if(item.getBin() != (currentBin.getName())){
                item.setZ(roundFloat(item.getZ() - currentNode.getPlacedBox().getLength()));
            }
        }
        else if((currentNode.getEmptySpace()!= null) && (item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            item.setZ(roundFloat(item.getZ() + currentNode.getEmptySpace().getLength()));
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getEmptySpace().getArea()), dataBaseName);
            
            if(item.getBin() != (currentBin.getName())){
                item.setZ(roundFloat(item.getZ() - currentNode.getEmptySpace().getLength()));
            }
        }
        
        unplaced = false;
        return currentNode;
    }
    
    /**
     * Depth fisrt search pre-order. Searches through all the nodes from root for the placed items
     * 
     * @param node
     * @return list of nodes with boxes stored in them
     */
    private List<Node> traversePreOrder(Node node){
        List<Node> nodeList = new ArrayList<>();
        
        if (node != null){
            nodeList.addAll(traversePreOrder(node.below));
            nodeList.addAll(traversePreOrder(node.right));
            nodeList.add(node);
        }
        return nodeList;
    }
    
    /**
     * Gathers a list of boxes to store and the bin it would like to
     * store those boxes in. 
     * 
     * @param storingBin
     * @param boxes
     * @param dataBaseName
     * @return List of boxes with their bin locations and positions
     */
    public static List<Box> sort2DBP(Bin storingBin, List<Box> boxes, String dataBaseName){
        //Creating the binary tree
        binaryTree binTree = new binaryTree();
        binTree.currentBin = storingBin;
        
        //Gathering pre-existing boxes from the item database
        ItemDatabase ITDB = new ItemDatabase();
        existingBoxes = ITDB.getExistingBoxes(storingBin, dataBaseName);
        
        //Gathering empty spaces in the bin
        unocupiedSpace = ITDB.getEmptySpaces(storingBin.getName(), dataBaseName);
        
        //Gathering information about the bin that will be storing the boxes and the boxes that will be placed
        Space binSpace = storingBin.getArea();
        List<Box> boxList = new ArrayList<>();
        
        //Adding each box provided to the binary tree
        boxes.forEach((curentBox) -> {
            while(unplaced == true){
                binTree.add(curentBox, binSpace, dataBaseName);
            }
            unplaced = true;
        });
        
        //Obtaining a list of the nodes
        List<Node> nodeList = binTree.traversePreOrder(binTree.root);
        
        //Extracting box information about the sorted boxes from the nodes
        nodeList.forEach((currentNode) -> {
            if(currentNode.getPlacedBox() != null){
                currentNode.getPlacedBox().setBin(storingBin.getName());
                boxList.add(currentNode.getPlacedBox());
            }
        });
        
        return boxList;
    }
    
    /**
     * Finds if any preexisting boxes stored in the location specified
     * 
     * @param placedBoxes
     * @param x
     * @param z
     * @return The box (if there is one) stored in that location
     */
    private static Box findPlacedBox(List<Box> placedBoxes, float x, float z){
        Box returnValue = null;
        
        for(Box currentPlacedBox: placedBoxes){
            if((currentPlacedBox.getX() == x) && (currentPlacedBox.getZ() == z)){
                returnValue = currentPlacedBox;
            }
        }
        return returnValue;
    }
    
    /**
     * Similar to findPlacedBox, finds the location of empty space objects in the bin
     * 
     * @param emptySpaces
     * @param x
     * @param z
     * @return The empty space (if there is one) stored in that location
     */
    private static EmptySpace findEmptySpace(List<EmptySpace> emptySpaces, float x, float z){
        EmptySpace returnValue = null;
        
        for(EmptySpace currentSpace: emptySpaces){
            if((currentSpace.getX() == x) && (currentSpace.getZ() == z)){
                returnValue = currentSpace;
            }
        }
        return returnValue;
    }
    
    private static float roundFloat(float floatToReound){
        DecimalFormat DF = new DecimalFormat("0000.0");
        String formattedWidth = DF.format(floatToReound);
        Float returnFloat = Float.parseFloat(formattedWidth);
        return returnFloat;
    }
}
