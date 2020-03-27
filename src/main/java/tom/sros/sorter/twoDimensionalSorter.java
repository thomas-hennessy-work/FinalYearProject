/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.sros.sorter;

import tom.sros.storageRoom.Box;
import tom.sros.storageRoom.Space;

/**
 *
 * @author thoma
 */
public class twoDimensionalSorter {
    
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
    
    public void add(Box item){
        root = addBoxRecursive(root, item);
    }
    
}
