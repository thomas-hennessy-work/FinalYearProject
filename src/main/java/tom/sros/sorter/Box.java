package tom.sros.sorter;

public class Box {
    //Box name
    String Name;
    
    //Box dimensions
    float Width;
    float Length;
    float Height;
    
    //Box position
    float X;
    float Y;
    float Z;
    
    Bin Location;
    
    //Constructors
    public Box(String name, float width, float length, float height){
        Name = name;
        Width = width;
        Length = length;
        Height = height;
    }
    
    public Box(String name){
        Name = name;
        Width = 0;
        Length = 0;
        Height = 0;
    }
    
    //get methods
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
    public float geZ(){
        return Z;
    }
    
    public Bin getBin(){
        return Location;
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
    
    public void setX(float x){
        X = x;
    }
    public void setY(float y){
        Y = y;
    }
    public void setZ(float z){
        Z = z;
    }
    
    public void setBin(Bin location){
        Location = location;
    }
    
    //geting calculations
    public float getVolume(){
        return Width*Length*Height;
    }
    
    public Space getArea(){
        return new Space(Width, Length);
    }
    
    @Override
    public String toString(){
        return("Name:" + Name + " Width:" + Width + " Length:" + Length + " Height:" + Height + " X position:" + X + " Y position:" + Y + " Z position:" + Z);
    }
}
