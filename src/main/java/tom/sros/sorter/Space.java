
package tom.sros.sorter;

public class Space {
    
    float Width;
    float Length;
    
    //Constructor
    public Space(float width, float length){
        this.Width = width;
        Length = length;
    }
    
    @Override
    public String toString(){
        return("Width: " + Width + ", Length: " + Length);
    }
    
    //Geth mothods
    public float getLength(){
        return Length;
    }
    public float getWidth(){
        return Width;
    }
    
    //Checked area = the area this space object is trying to be fit in to
    public boolean canFit(Space checkedArea){
        return (checkedArea.getWidth() >= Width && checkedArea.getLength() >= Length);
    }
    
    
    //Methods relating to calculating the space to the right and bellow a box once it
    //has been placed
    public Space areaBellow(Space binArea, Space boxArea){
        float bellowWidth = binArea.getWidth();
        float bellowLength = ((binArea.getLength()) - (boxArea.getLength()));
        
        Space bellowArea = new Space(bellowWidth, bellowLength);
        return bellowArea;
    }
    
    public Space areaRight(Space binArea, Space boxArea){
        float rightWidth = ((binArea.getWidth()) - (boxArea.getWidth()));
        float rightLength = (boxArea.getLength());
        
        Space rightArea = new Space(rightWidth, rightLength);
        return rightArea;
    }
    
}
