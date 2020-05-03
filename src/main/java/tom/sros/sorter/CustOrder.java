package tom.sros.sorter;

public class CustOrder extends Box{
    
    String CustAddress;
    String CustName;
    String OrderID;
    
    //Constructors
    public CustOrder(String id, String address, String custName){
        super(id);
        CustAddress = address;
        CustName = custName;
    }
    public CustOrder(String boxID, String orderID, String address, String custName, float X, float Y, float Z, String location){
        super(boxID, location, X, Y, Z);
        CustAddress = address;
        CustName = custName;
        OrderID = orderID;
    }
    
    //Set methods
    public void setOrderID(String orderID){
        OrderID = orderID;
    }
    
    //Get methods
    public String getCustAddress(){
        return CustAddress;
    }
    public String getCustName(){
        return CustName;
    }
    public String getOrderID(){
        return OrderID;
    }
}
