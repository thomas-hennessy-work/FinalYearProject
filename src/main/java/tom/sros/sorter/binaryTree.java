/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.sros.sorter;

import java.util.ArrayList;
import java.util.List;
import tom.sros.storageRoom.Box;
import tom.sros.storageRoom.Space;


public class binaryTree {
    
    Node root;
    Node fail;
    
    //When you add a box to a top left corner, there will be two quadrilaterals left over,
    //one bello and one to the right. Recursivly doing this allows you to fit boxes in to 
    //these spaces. The tree stores the space available.
    
    
    //https://www.baeldung.com/java-binary-tree
    class Node{
        Space binArea;
        Box storedBox;
        Node below;
        Node right;
        Boolean unsortable = false;
        
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
        private void placeBox(Box item){
            storedBox = item;
        }
        
        private Space getBinArea(){
            return binArea;
        }
        private Box getPlacedBox(){
            return storedBox;
        }
        
        private void unsortable(){
            unsortable = true;
        }
        private boolean isSortable(){
            return unsortable;
        }
    }
    
        private void add(Box item, Space binSpace){
        System.out.println("Testing root value: " + root);
        root = addBoxRecursive(root, item, binSpace);
    }
    
    private Node addBoxRecursive(Node currentNode, Box item, Space binSpace){
        System.out.println("Values provided" + "\nNode: " + currentNode + "\nBox: " + item.toString() + "\nSpace: " + binSpace.toString());
        //The algorithm checks right then bellow, due to the right being more likely to have less space
        
        //The box checks if it is able to fit in the boxes bellow, even if they fit. This
        //is due to the space constantly becoming smaller as boxes are added. If it is unable
        //to fit in the space that is already taken, it will not fit in a smaller space. This
        //eliminates unnececery recursion
        
//        System.out.println("value of current node: " + currentNode);
//        System.out.println(currentNode);
//        currentNode.addBinArea(binSpace);
        
        //If there is no box in the current node AND the box fits, place it here
//        System.out.println("Node can fit in this area is " + item.getArea().canFit(currentNode.getBinArea()));//currentNode.getBinArea().canFit(item.getArea()));
//        System.out.println("box stored in this location is: " + currentNode.getPlacedBox());
        
        if (currentNode == null){
            System.out.println("Current node is null");
            return new Node(binSpace, item);
        }
//        else if (currentNode.getPlacedBox() == null && item.getArea().canFit(currentNode.getBinArea())){
//            //placing box in current node
//            System.out.println("node not null, box can fit in it");
//            //System.out.println("Name: " + currentNode.getPlacedBox().getName() + "\nX: " + currentNode.getPlacedBox().getX() + "\nY: " + currentNode.getPlacedBox().getY() + "\nArea: " + currentNode.getPlacedBox().getArea().toString());
//            return new Node(binSpace, item);
//        }
//        
        //If the box can fit in the node to the right, but there is no node right, move to the node right
        else if (item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))){
            System.out.println("Going right");
            //itterativly adding to the position of the item
            item.setX(item.getX() + currentNode.getPlacedBox().getWidth());
            System.out.println(item.getX());
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
        }
        //If the box can fit in the node bellow, but there is no node already bellow, move to the node bellow
        else if (item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))){
            System.out.println("Going bellow");
            //itterativly adding to the position of the item
            item.setY(item.getY() + currentNode.getPlacedBox().getLength());
            System.out.println(item.getY());
            //continuing recursion
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
        }
        System.out.println("Sorter returning null");
        currentNode.unsortable();
        return currentNode;
    }
    
    //Depth fisrt search pre-order
    private List<Node> traversePreOrder(Node node){
        List<Node> nodeList = new ArrayList<>();
        
        if (node != null && node.isSortable()){
            System.out.println("Found box in node: " + node.getPlacedBox().getName());
            nodeList.addAll(traversePreOrder(node.below));
            nodeList.addAll(traversePreOrder(node.right));
            nodeList.add(node);
        }
        return nodeList;
    }
    
    public static List<Box> sort2DBP(Space binSpace, List<Box> boxes){
        binaryTree binTree = new binaryTree();
        List<Box> boxList = new ArrayList<>();
        
        System.out.println("Initial value of root: " + binTree.root);
        
        boxes.forEach((curentBox) -> {
            System.out.println("Adding box: " + curentBox.getName());
            binTree.add(curentBox, binSpace);
        });
        
        List<Node> nodeList = binTree.traversePreOrder(binTree.root);
        System.out.println("Value of root: " + binTree.root);
        System.out.println("Node list size: " + nodeList.size());
        
        nodeList.forEach((currentNode) -> {
            System.out.println("current Node: " + currentNode.getPlacedBox().getName());
            boxList.add(currentNode.getPlacedBox());
        });
        
        return boxList;
        
    }
}
