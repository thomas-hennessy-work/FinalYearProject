/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.sros.storageRoom;

import static java.util.Collections.list;
import java.util.List;

/**
 *
 * @author thoma
 */
public class box {
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
    
    //Constructors
    public box(String name, float width, float length, float height){
        Name = name;
        Width = width;
        Length = length;
        Height = height;
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
}

