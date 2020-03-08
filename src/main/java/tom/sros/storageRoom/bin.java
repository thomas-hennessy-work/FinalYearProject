package tom.sros.storageRoom;

import java.util.List;
import tom.sros.storageRoom.box;


public class bin {
    //bin name
    String Name;
    
    //bin dimensions
    float Width;
    float Length;
    float Height;
    
    //stored bins
    List<box> storedBoxes;
    
    //Constructors
    public bin(String name, float width, float length, float height){
        Name= name;
        Width = width;
        Length = length;
        Height = height;
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
    
    public List<box> getBoxes(){
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
    public void addBox(box item){
        storedBoxes.add(item);
    }
    public void addBoxes(List<box> items){
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
}
