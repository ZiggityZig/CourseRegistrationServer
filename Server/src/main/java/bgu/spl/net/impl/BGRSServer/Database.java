package bgu.spl.net.impl.BGRSServer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are
 * stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton. You must
 * not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ConcurrentHashMap<String, User> users;
	private ConcurrentHashMap<Integer, Course> courses;
	private ArrayList<Integer> inOrderCourses;

	// to prevent user from creating new Database
	private Database() {
		users = new ConcurrentHashMap<String, User>();
		courses = new ConcurrentHashMap<Integer, Course>();
		inOrderCourses = new ArrayList<Integer>();
		initialize("./Courses.txt");
	}

	private static class DatabaseHolder {
		private static Database instance = new Database();
	}

	/**
	 * Retrieves the single instance of this class.
	 */

	public boolean addUser(boolean isAdmin, String name, String password) {
		User user = new User(isAdmin, name, password);
		if (users.containsKey(user.getName())) {
			return false;
		} else {
			users.put(user.getName(), user);
			return true;
		}
	}

	public static Database getInstance() {
		return DatabaseHolder.instance;
	}

	public String displayCourseStatus(int courseNum) {
		return courses.get(courseNum).toString();
	}

	public String displayCurrentCourses (String userName){
		String coursesList = "[";
		if(!users.get(userName).getRegisteredCourses().isEmpty()){
			for (Integer course : users.get(userName).getRegisteredCourses()) {
				coursesList = coursesList + course.toString() + ", ";
			}
			coursesList = coursesList.substring(0, coursesList.length() - 2);
		}
		coursesList = coursesList + "]";
		return coursesList;
	}

	public String displayStudentStatus(String userName) {
		ArrayList<Integer> userCourses = users.get(userName).getRegisteredCourses();
		ArrayList<Integer> sortedUserCourses = new ArrayList<Integer>();
		for (Integer course : inOrderCourses){
			if(userCourses.contains(course)){
				sortedUserCourses.add(course);
			}
		}
		String answer = users.get(userName).toString() + sortedUserCourses.toString();
		return answer;
		//TODO: find a more efficient solution, not extremely urgent
	}

	public boolean doesExist(int courseNum) {
		return courses.containsKey(courseNum);
	}

	public boolean doesExist(String userName) {
		return users.containsKey(userName);
	}

	public String getKdamCoursesList(int courseNum) {
		return courses.get(courseNum).getKdamCourses().toString();
	}

	public boolean isUserAdmin(String UserName) {
		return users.get(UserName).isAdmin();
	}

	public boolean isFull (int courseNum) {
		Course course = courses.get(courseNum);
		return course.getNumOfMaxStudents() == course.getNumOfRegisteredStudents();
	}

	public boolean isRegistered(String userName, Integer courseNum) {
		return users.get(userName).getRegisteredCourses().contains(courseNum);
	}

	public boolean loginUser(String userName, String password) {
		
		boolean wasSuccessful, match;
		User user = users.get(userName);

		if (user == null || user.getLoginStatus() == true ||!password.equals(user.getPassword())) {
			wasSuccessful = false;
		} else {
			user.setLoginStatus(true);
			wasSuccessful = true;
		}
		return wasSuccessful;
	}

	public boolean logoutUser(String userName) {
		User user = users.get(userName);
		if (user.getLoginStatus() == false) {
			return false;
		} else {
			user.setLoginStatus(false);
			return true;
		}
	}

	public boolean logStatus(String userName){
		if(userName == null){return false;}
		return users.get(userName).getLoginStatus();
	}

	public void registerCourse(int courseNum, String userName) {

		users.get(userName).getRegisteredCourses().add(courseNum);
		Collections.sort(users.get(userName).getRegisteredCourses()); // Ensure courses remain in order)
		courses.get(courseNum).registered();
		courses.get(courseNum).getRegisteredStudents().add(userName);
	}

	public void unregisterCourse(Integer courseNum, String userName) {
		users.get(userName).getRegisteredCourses().remove(courseNum);
		courses.get(courseNum).unregistered();
	}

	public boolean qualifiedKdam(int courseNum, String userName) {
		
		boolean ans = true; // true by default, changes to false if one of the kdam courses is missing
		ArrayList<Integer> regCourses = users.get(userName).getRegisteredCourses();
		ArrayList<Integer> kdamCourses = courses.get(courseNum).getKdamCourses();
		for (Integer course : kdamCourses) {
			if (!regCourses.contains(course)) {
				ans = false;
			}
		}
		return ans;
	}
	

	/**
	 * loads the courses from the file path specified into the Database, returns
	 * true if successful.
	 */
	private boolean initialize(String coursesFilePath) {
		
		String[] line, kdamCourseNumbers;
		String name, kdam, data;
		int number, numOfMaxStudents;
		try {
			File coursesFile = new File(coursesFilePath);
			Scanner fileReader = new Scanner(coursesFile);
			while (fileReader.hasNextLine()) {
				ArrayList<Integer> kdamCourses = new ArrayList<Integer>();
				data = fileReader.nextLine();
				line = data.split("\\|");
				number = Integer.parseInt(line[0]);
				name = line[1];
				kdam = line[2];
				if (kdam.length() > 2){
					kdam = kdam.substring(1, kdam.length() - 1);
					kdamCourseNumbers = kdam.split(",");
					for (String num : kdamCourseNumbers) {
						kdamCourses.add(Integer.parseInt(num));
					}	
				}
				numOfMaxStudents = Integer.parseInt(line[3]);	
				courses.putIfAbsent(number, new Course(number, name, kdamCourses, numOfMaxStudents));
				inOrderCourses.add(number);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}


	

}
