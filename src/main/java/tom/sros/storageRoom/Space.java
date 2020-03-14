
package tom.sros.storageRoom;

public class Space {
    
    float Width;
    float Length;
    
    public Space(float width, float length){
        this.Width = width;
        Length = length;
    }
    
    public float getLength(){
        return Length;
    }
    public float getWidth(){
        return Width;
    }
    
    public boolean canFit(Space checkedArea){
        if(checkedArea.getWidth() > Width || checkedArea.getLength() > Length)
            return false;
        else
            return true;
    }
    
    
    //Methods relating to bin calculations
    public Space areaBellow(Space binArea, Space boxArea){
        float bellowWidth = binArea.getWidth();
        float bellowLength = (binArea.getLength() - boxArea.getLength());
        
        Space bellowArea = new Space(bellowWidth, bellowLength);
        return bellowArea;
    }
    
    public Space areaRight(Space binArea, Space boxArea){
        float rightWidth = (binArea.getWidth() - binArea.getWidth());
        float rightLength = (boxArea.getLength());
        
        Space rightArea = new Space(rightWidth, rightLength);
        return rightArea;
    }
    
}
