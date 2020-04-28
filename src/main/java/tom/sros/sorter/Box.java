package tom.sros.sorter;

public class Box {
    //Box name
    String ID;
    String Name;
    
    //Box dimensions
    float Width;
    float Length;
    float Height;
    
    //Box position
    float X;
    float Y;
    float Z;
    
    String Location;
    
    String Contents;
    String Notes;
    
    int Amount;
    
    //Constructors
    
    public Box(String id, float width, float length, float height){
        ID = id;
        Width = width;
        Length = length;
        Height = height;
    }
    
    public Box(String id, String name, float width, float length, float height, String contents, String notes){
        ID = id;
        Name = name;
        Width = width;
        Length = length;
        Height = height;
        Contents = contents;
        Notes = notes;
    }
    
    public Box(String id, String name, String location, float x, float y, float z){
        ID = id;
        Name = name;
        Location = location;
        X = x;
        Y = y;
        Z = z;
    }
    
    public Box(String id, String location, float x, float y, float z){
        ID = id;
        Location = location;
        X = x;
        Y = y;
        Z = z;
    }
    
    public Box(String id, float width, float length, float height, float x, float y, float z){
        ID = id;
        Width = width;
        Length = length;
        Height = height;
        X = x;
        Y = y;
        Z = z;
    }
    
    public Box(float width, float length, float height, float x, float y, float z, String location){
        Location = location;
        Width = width;
        Length = length;
        Height = height;
        X = x;
        Y = y;
        Z = z;
    }
    
    public Box(String id, int amount){
        ID = id;
        Amount = amount;
    }
    
    public Box(String id){
        ID = id;
        Width = 0;
        Length = 0;
        Height = 0;
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
    
    public float getX(){
        return X;
    }
    public float getY(){
        return Y;
    }
    public float getZ(){
        return Z;
    }
    
    public String getBin(){
        return Location;
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
    
    public void setX(float x){
        X = x;
    }
    public void setY(float y){
        Y = y;
    }
    public void setZ(float z){
        Z = z;
    }
    
    public void setBin(String location){
        Location = location;
    }
    
    public void setContents(String contents){
        Contents = contents;
    }
    public void setNotes(String notes){
        Notes = notes;
    }
    
    
    //geting calculations
    public float getVolume(){
        return Width*Length*Height;
    }
    
    public Space getArea(){
        return new Space(Width, Length);
    }
    
    //To string method
    @Override
    public String toString(){
        return("Name:" + ID + " Width:" + Width + " Length:" + Length + " Height:" + Height + " X position:" + X + " Y position:" + Y + " Z position:" + Z);
    }
}
