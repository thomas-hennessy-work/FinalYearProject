package tom.sros.sorter;

import java.util.List;


public class Bin {
    //bin name
    String Name;
    
    //bin dimensions
    float Width;
    float Length;
    float Height;
    
    //boxes stored in the bin
    List<Box> storedBoxes;
    
    //Amount of bins, used when adding multiple to the database
    int Amount;
    
    //Constructors
    public Bin(String name, float width, float length, float height){
        Name= name;
        Width = width;
        Length = length;
        Height = height;
    }
    
    public Bin(float width, float length, float height, int amount){
        Width = width;
        Length = length;
        Height = height;
        Amount = amount;
    }
    
    public Bin(String name){
        Name = name;
        Width = 0;
        Length = 0;
        Height = 0;
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
    public int getAmount(){
        return Amount;
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
