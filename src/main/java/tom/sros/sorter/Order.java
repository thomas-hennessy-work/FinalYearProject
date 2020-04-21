package tom.sros.sorter;

public class Order extends Box{
    
    String CustAddress;
    String CustName;
    
    //Constructor
    public Order(String id, String location, float x, float y, float z, String address, String custName) {
        super(id, location, x, y, z);
        CustAddress = address;
        CustName = custName;
    }
    
    public Order(String id, String address, String custName){
        super(id);
        CustAddress = address;
        CustName = custName;
    }
    
    public String getAddress(){
        return CustAddress;
    }
    public String getCustName(){
        return CustName;
    }
}
