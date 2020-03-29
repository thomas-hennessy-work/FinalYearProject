package tom.sros.storageRoom;

import java.util.List;
import tom.sros.storageRoom.Box;


public class Bin {
    //bin name
    String Name;
    
    //bin dimensions
    float Width;
    float Length;
    float Height;
    
    //stored bins
    List<Box> storedBoxes;
    
    //Constructors
    public Bin(String name, float width, float length, float height){
        Name= name;
        Width = width;
        Length = length;
        Height = height;
    }
    
    public Bin(){
        Width = 0;
        Length = 0;
        Height = 0;
    }
    
    //Get methods
    public String getName(){
        return Name;
    }
    
    public float getWidth(){
        return Width;
    }
    public float getLength(){
        return Length;
    }
    public float getHeight(){
        return Width;
    }
    
    public List<Box> getBoxes(){
        return storedBoxes;
    }
    
    //Set methods
    public void setName(String name){
        Name = name;
    }
    
    public void setWidth(float width){
        Width = width;
    }
    public void setLength(float length){
        Length = length;
    }
    public void setHeight(float height){
        Height = height;
    }
    
    //Box methods
    public void addBox(Box item){
        storedBoxes.add(item);
    }
    public void addBoxes(List<Box> items){
        items.forEach((boxes) -> {
            storedBoxes.add(boxes);
        });
    }
    
    //Removes a specified box from the bin
    public void removeBox(String name){
        storedBoxes.forEach((item) -> {
            if (item.getName().equals(name)){
                storedBoxes.remove(item);
            }
        });
    }
    
    //gets floor space
    public Space getArea(){
        return new Space(Width, Length);
    }
}
