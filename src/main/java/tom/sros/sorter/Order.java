package tom.sros.sorter;

public class Order extends Box{
    
    String CustAddress;
    String CustName;
    
    //Constructor
    public Order(String id, String address, String custName){
        super(id);
        CustAddress = address;
        CustName = custName;
    }
    
    //Get methods
    public String getAddress(){
        return CustAddress;
    }
    public String getCustName(){
        return CustName;
    }
}
