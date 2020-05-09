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
        float bellowWidth = (float)binArea.getWidth();
        float bellowLength = ((float)(binArea.getLength()) - ((float)boxArea.getLength()));
        
        //Rounding
        DecimalFormat DFW = new DecimalFormat("0000.0");
        String formattedWidth = DFW.format(bellowWidth);
        Float returnWidth = Float.parseFloat(formattedWidth);
        
        DecimalFormat DFL = new DecimalFormat("0000.0");
        String formattedLength = DFL.format(bellowLength);
        Float returnLength = Float.parseFloat(formattedLength);
        
        Space bellowArea = new Space(returnWidth, returnLength);
        return bellowArea;
    }
    
    public Space areaRight(Space binArea, Space boxArea){
        float rightWidth = (((float)binArea.getWidth()) - ((float)boxArea.getWidth()));
        float rightLength = ((float)boxArea.getLength());
        
        //Rounding
        DecimalFormat DFW = new DecimalFormat("0000.0");
        String formattedWidth = DFW.format(rightWidth);
        Float returnWidth = Float.parseFloat(formattedWidth);
        
        DecimalFormat DFL = new DecimalFormat("0000.0");
        String formattedLength = DFL.format(rightLength);
        Float returnLength = Float.parseFloat(formattedLength);

        Space rightArea = new Space(returnWidth, returnLength);
        return rightArea;
    }
    
}
