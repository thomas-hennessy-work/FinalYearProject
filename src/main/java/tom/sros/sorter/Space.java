package tom.sros.sorter;

import java.text.DecimalFormat;

public class Space {
    
    float Width;
    float Length;
    
    //Constructor
    public Space(float width, float length){
        this.Width = width;
        Length = length;
    }
    
    //Geth mothods
    public float getLength(){
        return Length;
    }
    public float getWidth(){
        return Width;
    }
    
    public float getArea(){
        float returnValue = roundFloat(Width*Length);
        return returnValue;
    }
    
    //Checked area = the area this space object is trying to be fit in to
    public boolean canFit(Space checkedArea){
        return (checkedArea.getWidth() >= Width && checkedArea.getLength() >= Length);
    }
    
    
    //Methods relating to calculating the space to the right and bellow a box once it
    //has been placed
    public Space areaBellow(Space binArea, Space boxArea){
        float bellowWidth = (float)binArea.getWidth();
        float bellowLength = ((float)(binArea.getLength()) - ((float)boxArea.getLength()));
        
        float returnWidth = roundFloat(bellowWidth);
        float returnLength = roundFloat(bellowLength);
        
        Space bellowArea = new Space(returnWidth, returnLength);
        return bellowArea;
    }
    
    public Space areaRight(Space binArea, Space boxArea){
        float rightWidth = (((float)binArea.getWidth()) - ((float)boxArea.getWidth()));
        float rightLength = ((float)boxArea.getLength());
        
        //Rounding
        float returnWidth = roundFloat(rightWidth);
        float returnLength = roundFloat(rightLength);

        Space rightArea = new Space(returnWidth, returnLength);
        return rightArea;
    }
    
    private static float roundFloat(float floatToReound){
        DecimalFormat DF = new DecimalFormat("0000000000.0");
        String formattedFloat = DF.format(floatToReound);
        Float returnFloat = Float.parseFloat(formattedFloat);
        return returnFloat;
    }
    
}
