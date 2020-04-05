/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.sros.sorter;

import java.util.ArrayList;
import java.util.List;


public class binaryTree {
    
    Node root;
    Bin currentBin;
    
    //When you add a box to a top left corner, there will be two quadrilaterals left over,
    //one bello and one to the right. Recursivly doing this allows you to fit boxes in to 
    //these spaces. The tree stores the space available.
    
    
    //https://www.baeldung.com/java-binary-tree
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
            System.out.println("Current node is null, box being placed");
            //Provides information about where the bin is stored and also prevents it from being re-sorted
            item.setBin(currentBin);
            return new Node(binSpace, item);
        }
        
        System.out.println("can go right: " + item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea())));
        System.out.println("can go bellow: " + item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea())) + "\n");
        //If the box can fit in the node to the right, but there is no node right, move to the node right
        if ((item.getArea().canFit(currentNode.getBinArea().areaRight(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != currentBin)){
            System.out.println("Going right");
            //itterativly adding to the position of the item
            item.setX(item.getX() + currentNode.getPlacedBox().getWidth());
            //continuing recursion
            currentNode.right = addBoxRecursive(currentNode.right, item, currentNode.getBinArea().areaRight(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
            
            if((item.getBin() != currentBin)){
                item.setX(item.getX() - currentNode.getPlacedBox().getWidth());
            }
        }
        
        //If the box can fit in the node bellow, but there is no node already bellow, move to the node bellow
        if ((item.getArea().canFit(currentNode.getBinArea().areaBellow(currentNode.getBinArea(), item.getArea()))) && (item.getBin() != currentBin)){
            System.out.println("Going bellow");
            item.setY(item.getY() + currentNode.getPlacedBox().getLength());
            currentNode.below = addBoxRecursive(currentNode.below, item, currentNode.getBinArea().areaBellow(currentNode.getBinArea(), currentNode.getPlacedBox().getArea()));
            
            if((item.getBin() != currentBin)){
                item.setY(item.getY() - currentNode.getPlacedBox().getWidth());
            }
        }
        return currentNode;
    }
    
    //Depth fisrt search pre-order
    private List<Node> traversePreOrder(Node node){
        List<Node> nodeList = new ArrayList<>();
        
        if (node != null){
            System.out.println("Found box in node: " + node.getPlacedBox().getName());
            nodeList.addAll(traversePreOrder(node.below));
            nodeList.addAll(traversePreOrder(node.right));
            nodeList.add(node);
        }
        return nodeList;
    }
    
    public static List<Box> sort2DBP(Bin storingBin, List<Box> boxes){
        //Creating instances of the sorter
        binaryTree binTree = new binaryTree();
        binTree.currentBin = storingBin;
        Space binSpace = storingBin.getArea();
        List<Box> boxList = new ArrayList<>();
        
        //Adding each box provided to the binary tree
        boxes.forEach((curentBox) -> {
            binTree.add(curentBox, binSpace);
        });
        
        //Obtaining a list of the nodes
        List<Node> nodeList = binTree.traversePreOrder(binTree.root);
        
        //Extracting box information about the sorted boxes from the nodes
        nodeList.forEach((currentNode) -> {
            boxList.add(currentNode.getPlacedBox());
        });
        
        return boxList;
    }
}
