package tom.sros.sorter;

public class BoxIndividual extends BoxType {
    //Box position
    float X;
    float Y;
    float Z;
    
    String Location;

    //Constructors
    public BoxIndividual(String id, String name, String location, float x, float y, float z){
        super(id, name);
        Location = location;
        X = x;
        Y = y;
        Z = z;
    }

    public BoxIndividual(String id, String location, float x, float y, float z){
        super(id);
        Location = location;
        X = x;
        Y = y;
        Z = z;
    }

    public BoxIndividual(String id, float width, float length, float height, float x, float y, float z){
        super(id, width, length, height);
        X = x;
        Y = y;
        Z = z;
    }

    public BoxIndividual(float width, float length, float height, float x, float y, float z, String location){
        super(width, length, height);
        Location = location;
        X = x;
        Y = y;
        Z = z;
    }
    
    public BoxIndividual(String id, float width, float length, float height){
        super(id, width, length, height);
    }

    public BoxIndividual(String id){
        super(id);
    }
    
    public BoxIndividual(){
    }

    //get methods
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

    //set methods
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
}
