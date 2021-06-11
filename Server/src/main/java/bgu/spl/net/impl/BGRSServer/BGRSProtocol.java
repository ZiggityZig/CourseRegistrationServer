package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;


public class BGRSProtocol implements MessagingProtocol<BGRSMessage> {
	
	private Database database;
	private String userName;
	private Boolean isAdmin; // Used to keep track of the user currently logged in
	private boolean shouldTerminate;
	private BGRSMessage request;
	
	public BGRSProtocol(){
		database = Database.getInstance();
		userName = null;
		isAdmin = null;
		shouldTerminate = false;
	}
	
	public BGRSMessage process (BGRSMessage msg) { //Checks for operation type and executes accordingly

		request = msg;
		BGRSMessage response = null;
		
		switch(msg.getOpcode()) {
		case 1:
			response = adminRegister();
			break;
		case 2:
			response = studentRegister();
			break;
		case 3:
			response = login();
			break;
		case 4:
			response = logout();
			break;
		case 5:
			response = courseRegister();
			break;
		case 6:
			response = kdamCheck();
			break;
		case 7:
			response = printCourse();
			break;
		case 8:
			response = printStudentStatus();
			break;
		case 9:
			response = checkIfRegistered();
			break;
		case 20:
			response = courseUnRegister();
			break;
		case 11:
			response = checkCurrentCourses();
			break;
		}

		return response;
	}

	private BGRSMessage adminRegister(){
		return register(true, request); // True, user is registering as administrator
	}

	private BGRSMessage studentRegister() {
		return register(false, request); // false, user is not registering as administrator
	}

	private BGRSMessage login() {

		BGRSMessage response;

		if (!(database.loginUser(request.getName(), request.getPassword()))) { // Checks if login was unsuccessful
			response = error(request.getOpcode());
		} else {
			userName = request.getName();
			isAdmin = database.isUserAdmin(request.getName());
			response = acknowledgement(request.getOpcode(), null);
		}
		return response;
	}

	private BGRSMessage logout() {
		BGRSMessage response;

		if (userName == null  || !database.logoutUser(userName)) { // Checks if logout was unsuccessful
			response = error(request.getOpcode());
		} else {
			userName = null;
			isAdmin = null;
			shouldTerminate = true;
			response = acknowledgement(request.getOpcode(), null);
		}
		return response;
	}

	private BGRSMessage courseRegister() {
		BGRSMessage response;
		if(!database.logStatus(userName) || isAdmin // User is not logged in or user is an admin
				|| !database.doesExist(request.getCourseNum())  // Course does not exist
				|| database.isFull(request.getCourseNum()) // No sits available
				|| !database.qualifiedKdam(request.getCourseNum(), userName) // Checks if has all kdamCourses
				) {
			response = error(request.getOpcode());
		} else {
			database.registerCourse(request.getCourseNum(), userName);
			response = acknowledgement(request.getOpcode(), null);
		}
		
		return response;
	}

	private BGRSMessage kdamCheck() {
		return acknowledgement(request.getOpcode(), database.getKdamCoursesList(request.getCourseNum()));
	}

	private BGRSMessage printCourse() {
		
		BGRSMessage response;
		if (!database.doesExist(request.getCourseNum()) || !isAdmin) {
			response = error(request.getOpcode()); // Returns an error if user isn't an Admin or if course doesn't exist
		} else {
			response = acknowledgement(request.getOpcode(), database.displayCourseStatus(request.getCourseNum()));
		}
		return response;
	}

	private BGRSMessage printStudentStatus() {
		BGRSMessage response;
		if (userName == null||!isAdmin) {
			response = error(request.getOpcode()); // Returns an error if user isn't an Admin or if course doesn't exist
		} else {
			response =  acknowledgement(request.getOpcode(), database.displayStudentStatus(request.getName()));
		}
		return response;
	}

	private BGRSMessage checkIfRegistered() {

		BGRSMessage response;
		String answer;
		if(!database.logStatus(userName) || isAdmin) {
			response = error(request.getOpcode());
		} else {
			if(database.isRegistered(userName, request.getCourseNum())) {
				answer = "REGISTERED";
			} else {
				answer ="NOT REGISTERED";
			}
			response = acknowledgement(request.getOpcode(), answer);
		}
		return response;
	}

	private BGRSMessage courseUnRegister() {

		BGRSMessage response;
		if(!database.logStatus(userName) || isAdmin // User is not logged in or is admin
				|| !database.doesExist(request.getCourseNum())  // Course does not exists
				|| !database.isRegistered(userName, request.getCourseNum()) // Student not registered
		) {
			response = error(request.getOpcode());
		} else {
			database.unregisterCourse(request.getCourseNum(), userName);
			response = acknowledgement(request.getOpcode(), null);
		}
		return response;
	}

	private BGRSMessage checkCurrentCourses() {
		return acknowledgement((short)11, database.displayCurrentCourses(userName));
	}

	private BGRSMessage acknowledgement(Short opcode, String answer) {
		return new BGRSMessage((short)12, null, null, null, answer, opcode);
	}

	private BGRSMessage error(Short opcode) {
		return new BGRSMessage((short) 13, null, null, null, null, opcode);
	}

	private BGRSMessage register(boolean isAdmin, BGRSMessage msg){
		
		boolean wasAdded = database.addUser(isAdmin, msg.getName(), msg.getPassword());
		if(wasAdded) {
			return acknowledgement(msg.getOpcode(), null);
		} else {
			return error(msg.getOpcode());
		}
	}
	
	public boolean shouldTerminate() {
		return shouldTerminate;
	}
}
