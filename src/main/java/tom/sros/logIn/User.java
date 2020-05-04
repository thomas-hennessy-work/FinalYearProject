package tom.sros.logIn;

public class User {
    
    String ID;
    String Password;
    String UserName;
    Boolean IsManager;
    
    //Constructor
    public User(String id, String userName, Boolean managerStatus){
        ID = id;
        UserName = userName;
        IsManager = managerStatus;
    }
    
    public void setPassword(String password){
        Password = password;
    }
    
    //Get methods
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
