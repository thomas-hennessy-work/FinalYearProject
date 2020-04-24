package tom.sros.sorter;

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
        
        Node(){
            this.binArea = null;
            storedBox = null;
            below = null;
            right = null;
        }
        
        Node(Space binArea){
            this.binArea = binArea;
            storedBox = null;
            below = null;
            right = null;
        }
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
    
        private void add(Box item, Space binSpace){
        root = addBoxRecursive(root, item, binSpace);
    }
    
    //The algorithm checks right then bellow, due to the right being more likely to have less space
    private Node addBoxRecursive(Node currentNode, Box item, Space binSpace){
        System.out.println("Values provided" + "\nNode: " + currentNode + "\nBox: " + item.toString() + "\nSpace: " + binSpace.toString() + "\nPlaced: ");        
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
                System.out.println("Current node is null, pre-existing box placed here, box being placed");
                System.out.println(placedBox.getX() + " " + placedBox.getZ());
                return new Node(binSpace, placedBox);
            }
            //If an empty space is located in the current node. Tries to place the new box within the empty space
            //If it cannot fit, it places the empty space in the binary tree.
            else if(placedSpace != null){
                return new Node(binSpace, placedSpace);
            }
            else{
            //Provides information about where the bin is stored and also prevents it from being re-sorted
                System.out.println("Current node is null, no preexisting boxes, box being placed");
                item.setBin(currentBin.getName());
                unplaced = false;
                return new Node(binSpace, item);
            }
        }

        //If the box can fit in the node to the right, but there is no node right, move to the node right
        //The comparison to current bin is used to check if the box has been placed
        if ((currentNode.getPlacedBox() != null) && (item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            System.out.println("Going right, box stored");
            //itterativly adding to the position of the item
            item.setX(item.getX() + currentNode.getPlacedBox().getWidth());
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
            
            //If the box is not placed and it comes back through the binary tree, the itterative position is undone
            if(item.getBin() != (currentBin.getName())){
                item.setX(item.getX() - currentNode.getPlacedBox().getWidth());
            }
        } 
        //Same as the initial if statement, only it executes on empty spaces rather than stored boxes
        else if((currentNode.getEmptySpace()!= null) && (item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            System.out.println("Going right, empty space");
            //itterativly adding to the position of the item
            item.setX(item.getX() + currentNode.getEmptySpace().getWidth());
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getEmptySpace().getArea()));
            
            //If the box is not placed and it comes back through the binary tree, the itterative position is undone
            if(item.getBin() != (currentBin.getName())){
                item.setX(item.getX() - currentNode.getEmptySpace().getWidth());
            }
        }
        
        //If the box can fit in the node bellow, but there is no node already bellow, move to the node bellow
        if ((currentNode.getPlacedBox() != null) && (item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            System.out.println("Going bellow, box stored");
            item.setZ(item.getZ() + currentNode.getPlacedBox().getLength());
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
            
            if(item.getBin() != (currentBin.getName())){
                item.setZ(item.getZ() - currentNode.getPlacedBox().getWidth());
            }
        }
        else if((currentNode.getEmptySpace()!= null) && (item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            System.out.println("Going bellow, empty space");
            item.setZ(item.getZ() + currentNode.getEmptySpace().getLength());
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getEmptySpace().getArea()));
            
            if(item.getBin() != (currentBin.getName())){
                item.setZ(item.getZ() - currentNode.getEmptySpace().getWidth());
            }
        }
        
        
        unplaced = false;
        return currentNode;
    }
    
    //Depth fisrt search pre-order
    private List<Node> traversePreOrder(Node node){
        List<Node> nodeList = new ArrayList<>();
        
        if (node != null){
            nodeList.addAll(traversePreOrder(node.below));
            nodeList.addAll(traversePreOrder(node.right));
            nodeList.add(node);
        }
        return nodeList;
    }
    
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
                binTree.add(curentBox, binSpace);
                System.out.println("Looping and unplaced = " + unplaced);
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
    
    //Used to see if any pre-existing boxes should be placed instead of a new box, due
    //to boxes already occupying the position
    private static Box findPlacedBox(List<Box> placedBoxes, float x, float z){
        Box returnValue = null;
        
        for(Box currentPlacedBox: placedBoxes){
            if((currentPlacedBox.getX() == x) && (currentPlacedBox.getZ() == z)){
                returnValue = currentPlacedBox;
            }
        }
        System.out.println("Box found in this position: " + returnValue);
        return returnValue;
    }
    
    //Similar to findPlacedBox, used to find empty space in a location
    private static EmptySpace findEmptySpace(List<EmptySpace> emptySpaces, float x, float z){
        EmptySpace returnValue = null;
        
        for(EmptySpace currentSpace: emptySpaces){
            if((currentSpace.getX() == x) && (currentSpace.getZ() == z)){
                returnValue = currentSpace;
            }
        }
        System.out.println("Space found in this position: " + returnValue);
        return returnValue;
    }
}
