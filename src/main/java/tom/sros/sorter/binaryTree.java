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
   

    class Node{
        Space binArea;
        Box storedBox;
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
            below = null;
            right = null;
        }
        
        private void addBinArea(Space Area){
            binArea = Area;
        }
        
        private Space getBinArea(){
            return binArea;
        }
        private Box getPlacedBox(){
            return storedBox;
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
            Box placedBox = findPlacedBox(existingBoxes, item.getX(), item.getY());
            if(placedBox != null){
                System.out.println("Current node is null, pre-existing box placed here, box being placed");
                System.out.println(placedBox.getX() + " " + placedBox.getY());
                return new Node(binSpace, placedBox);
            }else{
            //Provides information about where the bin is stored and also prevents it from being re-sorted
                System.out.println("Current node is null, no preexisting boxes, box being placed");
                item.setBin(currentBin.getName());
                unplaced = false;
                return new Node(binSpace, item);
            }
        }

        //If the box can fit in the node to the right, but there is no node right, move to the node right
        //The comparison to current bin is used to check if the box has been placed
        if ((item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            System.out.println("Going right");
            //itterativly adding to the position of the item
            item.setX(item.getX() + currentNode.getPlacedBox().getWidth());
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
            
            //If the box is not placed and it comes back through the binary tree, the itterative position is undone
            if(item.getBin() != (currentBin.getName())){
                item.setX(item.getX() - currentNode.getPlacedBox().getWidth());
            }
        }
        
        //If the box can fit in the node bellow, but there is no node already bellow, move to the node bellow
        if ((item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != (currentBin.getName()))){
            System.out.println("Going bellow");
            item.setY(item.getY() + currentNode.getPlacedBox().getLength());
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
            
            if(item.getBin() != (currentBin.getName())){
                item.setY(item.getY() - currentNode.getPlacedBox().getWidth());
            }
        }
        unplaced = false;
        return currentNode;
    }
    
    //Depth fisrt search pre-order
    private List<Node> traversePreOrder(Node node){
        List<Node> nodeList = new ArrayList<>();
        
        if (node != null){
            System.out.println("Found box in node: " + node.getPlacedBox().getID());
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
        System.out.println("existingBoxes size(): " + existingBoxes.size());
        
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
            currentNode.getPlacedBox().setBin(storingBin.getName());
            boxList.add(currentNode.getPlacedBox());
        });
        
        return boxList;
    }
    
    //Used to see if any pre-existing boxes should be placed instead of a new box, due
    //to boxes already occupying the position
    private static Box findPlacedBox(List<Box> placedBoxes, float x, float y){
        Box returnValue = null;
        
        for(Box currentPlacedBox: placedBoxes){
            if((currentPlacedBox.getX() == x) && (currentPlacedBox.getY() == y)){
                returnValue = currentPlacedBox;
            }
        }
        System.out.println("Box found in this position: " + returnValue);
        return returnValue;
    }
}
