package bgu.spl.net.impl.BGRSServer;
import java.util.ArrayList;
import java.util.Vector;

public class Course {
    private int num;
    private String courseName;
    private ArrayList<Integer> kdamCourses; //TODO: If there is time left, change Integer to short
    private int numOfMaxStudents;
    private int numOfRegisteredStudents;
    private Vector<String> registeredStudents;

    Course(int num, String courseName, ArrayList<Integer> kdamCourses, int numOfMaxStudents){
        this.num = num;
        this.courseName = courseName;
        this.kdamCourses = kdamCourses;
        this.numOfMaxStudents = numOfMaxStudents;
        numOfRegisteredStudents = 0;
        registeredStudents = new Vector<String>();
    }

    public int getNum() {
        return num;
    }

    public String getCourseName() {
        return courseName;
    }

    public ArrayList<Integer> getKdamCourses() {
        return kdamCourses;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }
    
    public int getNumOfRegisteredStudents() {
    	return numOfRegisteredStudents;
    }
    
    public Vector<String> getRegisteredStudents(){
	   return registeredStudents;
   }
    // Each course keeps track of the amount of registered students.
    // Whenever a Student registers/unregisters, the following methods are invoked
    public void registered() {
    	numOfRegisteredStudents ++;
    }
    
    public void unregistered() {
    	numOfRegisteredStudents --;
    }
    
	public String toString() {

		String str = "(" + String.valueOf(num) + ")" + " " + courseName + " " + "Seats available: "
				+ String.valueOf(numOfRegisteredStudents) + "/" + String.valueOf(numOfMaxStudents) + " "
				+ "Students Registered: [";

		if (!registeredStudents.isEmpty()) {
			for (String name : registeredStudents) {
				str = str + name + ", ";
			}
			str = str.substring(0, str.length() - 2);
		}
		str = str + "]";
		return str;
    }
}

