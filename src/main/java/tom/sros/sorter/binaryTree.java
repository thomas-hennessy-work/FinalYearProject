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
    }
    
        private void add(Box item, Space binSpace){
//        if (root == null){
//            root = new Node(binSpace);
//        }
            
        root = addBoxRecursive(root, item, binSpace);
    }
    
    private Node addBoxRecursive(Node currentNode, Box item, Space binSpace){
        
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
        
        item.setX(0);
        item.setY(0);
        currentNode.addBinArea(binSpace);
        
        //If there is no box in the current node AND the box fits, place it here
        if (currentNode.getPlacedBox() == null && currentNode.getBinArea().canFit(item.getArea())){
            //Creating nodes bellow
//            currentNode.below = new Node(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()));
//            currentNode.right = new Node(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()));
            
            //placing box in current node
            currentNode.placeBox(item);
            System.out.println("Name: " + currentNode.getPlacedBox().getName() + "\nX: " + currentNode.getPlacedBox().getX() + "\nY: " + currentNode.getPlacedBox().getY() + "\nArea: " + currentNode.getPlacedBox().getArea().toString());
            
            return currentNode;
        }
        //If the box can fit in the node to the right, move to the node right
        //else if (currentNode.right.getBinArea().canFit(item.getArea())){
        else if (item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))){
            //Setting up the space in the node to the right
            //currentNode.right = new Node ((currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea())));
            //itterativly adding to the position of the item
            System.out.println("Curent placed box: " + currentNode.getPlacedBox());
            item.setX(item.getX() + currentNode.getPlacedBox().getWidth());
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()));
        }
        //If the box can fit in the node bellow, move to the node bellow
        else if (item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))){
            //currentNode.below = new Node(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()));
            //itterativly adding to the position of the item
            item.setY(item.getY() + currentNode.getPlacedBox().getLength());
            //continuing recursion
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()));
        }
        
        return null;
    }
    
    //Depth fisrt search pre-order
    private List<Node> traversePreOrder(Node node){
        List<Node> nodeList = new ArrayList<>();
        
        if (node != null && node.getPlacedBox() != null){
            System.out.println("Found node: " + node.getPlacedBox().getName());
            traversePreOrder(node.below);
            traversePreOrder(node.right);
            nodeList.add(node);
        }
        return nodeList;
    }
    
    public List<Box> sort2DBP(Space binSpace, List<Box> boxes){
        binaryTree binTree = new binaryTree();
        List<Box> boxList = new ArrayList<>();
        
        boxes.forEach((curentBox) -> {
            System.out.println("Adding box: " + curentBox.getName());
            binTree.add(curentBox, binSpace);
        });
        
        List<Node> nodeList = binTree.traversePreOrder(root);
        System.out.println(root);
        System.out.println("Node list size: " + nodeList.size());
        
        nodeList.forEach((currentNode) -> {
            System.out.println("Node list: " + currentNode.getPlacedBox().getName());
            boxList.add(currentNode.getPlacedBox());
        });
        
        return boxList;
        
    }
}
