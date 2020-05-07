package tom.sros.sorter;

import java.util.ArrayList;
import java.util.List;


public class Bin {
    //bin name
    String Name;
    
    //bin dimensions
    float Width;
    float Length;
    float Height;
    
    //boxes stored in the bin
    List<Box> StoredBoxes = new ArrayList<>();
    boolean Occupied;
    
    //Amount of bins, used when adding multiple to the database
    int Amount;
    
    //Constructors
    public Bin(String name, float width, float length, float height){
        Name= name;
        Width = width;
        Length = length;
        Height = height;
    }
    
    public Bin(int amount, float width, float length, float height){
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
        return StoredBoxes;
    }
    public int getAmount(){
        return Amount;
    }
    
    //Set methods
    public void setAmount(int amount){
        Amount = amount;
    }
    
    //Box methods

    public void addBox(Box item){
        StoredBoxes.add(item);
        Occupied = !StoredBoxes.isEmpty();
    }
    
    public boolean getOccupied(){
        return Occupied;
    }
    
    //gets floor space
    public Space getArea(){
        return new Space(Width, Length);
    }
}
