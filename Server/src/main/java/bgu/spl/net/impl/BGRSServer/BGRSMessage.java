package bgu.spl.net.impl.BGRSServer;
public class BGRSMessage { // A class that holds every parameter necessary for the server client communication
						   // For the record I have considered implementing it using an interface and multiple message
	private short opcode;  // types but decided there wasn't enough to gain from that. One Message class keeps it nice and simple
	private String name;
	private String password;
	private Integer courseNum;
	private String answer;
	private Short messageOpcode; // For acknowledgements and errors
	
	// Any parameter that isn't used for a specific message can just be set to null, an opcode will always exist.
	
	BGRSMessage (short opcode, String name, String password, Integer courseNum, String answer, Short messageOpcode){
		this.opcode = opcode;
		this.name = name;
		this.password = password;
		this.courseNum = courseNum;
		this.answer = answer;
		this.messageOpcode = messageOpcode;
	}

	BGRSMessage(short opcode){
		this.opcode = opcode;
		name = null;
		password = null;
		courseNum = null;
		answer = null;
		messageOpcode = null;
	}

	BGRSMessage (BGRSMessage other){
		this.opcode = other.opcode;
		this.name = other.name;
		this.password = other.password;
		this.courseNum = other.courseNum;
		this.answer = other.answer;
		this.messageOpcode = other.messageOpcode;
	}

	public short getOpcode() {
		return opcode;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getAnswer(){
		return answer;
	}

	public Integer getCourseNum () {
		return courseNum;
	}

	public Short getMessageOpcode () {
		return messageOpcode;
	}
}
