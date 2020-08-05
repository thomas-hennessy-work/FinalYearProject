package tom.sros.sorter;

public class BoxType {
    //Box name
    String ID;
    String Name;
    
    //Box dimensions
    float Width;
    float Length;
    float Height;
    
    String Contents;
    String Notes;
    
    int Amount;
    
    //Constructors
    public BoxType(){
    }
    
    public BoxType(String id, float width, float length, float height){
        ID = id;
        Width = width;
        Length = length;
        Height = height;
    }
    
    public BoxType(String id, String name, float width, float length, float height, String contents, String notes){
        ID = id;
        Name = name;
        Width = width;
        Length = length;
        Height = height;
        Contents = contents;
        Notes = notes;
    }
    
    public BoxType(String id, int amount){
        ID = id;
        Amount = amount;
    }
    
    public BoxType(String id){
        ID = id;
        Width = 0;
        Length = 0;
        Height = 0;
    }
    
    //Constructors intended for individual boxes
    public BoxType(String id, String name){
        ID = id;
        Name = name;
    }
    
    public BoxType(float width, float length, float height){
        Width = width;
        Length = length;
        Height = height;
    }
    
    //get methods
    public String getID(){
        return ID;
    } 
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
        return Height;
    }
    
    public String getContents(){
        return Contents;
    }
    public String getNotes(){
        return Notes;
    }
    
    public int getAmount(){
        return Amount;
    }
    
    
    //Set methods
    public void setID(String id){
        ID = id;
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
    
    public void setAmount(int amount){
        Amount = amount;
    }
    
    
    //geting calculations
    
    public Space getArea(){
        return new Space(Width, Length);
    }
}
