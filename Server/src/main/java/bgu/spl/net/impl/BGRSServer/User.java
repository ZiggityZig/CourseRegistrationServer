package bgu.spl.net.impl.BGRSServer;
import java.util.ArrayList;

public class User {
    private boolean isAdmin;
    private String name;
    private String password;
    private boolean logStatus;
    private ArrayList<Integer> registeredCourses; 

    public User (boolean isAdmin, String name, String password){
        this.isAdmin = isAdmin;
        this.name = name;
        this.password = password;
        logStatus = false;
        registeredCourses = new ArrayList<Integer>();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean getLoginStatus() {
    	return logStatus;
    }
    
    public ArrayList<Integer> getRegisteredCourses(){
    	return registeredCourses;
    }
    
    public void setLoginStatus (boolean status){
    	logStatus = status;
    }
    
    public String toString () {
    	String str = "Student: " + name + " ";
    	return str;
    }
}
