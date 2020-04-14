package tom.sros.logIn;

public class user {
    
    String ID;
    String Password;
    String UserName;
    Boolean IsManager;
    
    public user(String id, String userName, Boolean managerStatus){
        ID = id;
        UserName = userName;
        IsManager = managerStatus;
    }
    
    public String getID(){
        return ID;
    }
    public String getPassword(){
        return Password;
    }
    public String getUserName(){
        return UserName;
    }
    public boolean getIsManager(){
        return IsManager;
    }
}
