package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.api.MessageEncoderDecoder;


import java.nio.charset.StandardCharsets;
import java.util.LinkedList;


public class BGRSEncoderDecoder implements MessageEncoderDecoder<BGRSMessage> {

    private LinkedList<Byte> bytes;
    private int messageIndex;
    private short opCode;
    private String userName;
    private String password;
    private Integer courseNum;
    private Byte incomingByte;

    BGRSMessage request;


    public BGRSEncoderDecoder(){
        bytes = new LinkedList<Byte>();
        messageIndex = 0;
        userName = null;
        password = null;
        request = null;
        courseNum = null;
    }

    @Override
    public BGRSMessage decodeNextByte(byte nextByte) {
	
        incomingByte = nextByte;
        request = null;

        //TODO: not the original plan but this works, change if possible
	if (incomingByte != '\n'){ 
	     bytes.add(incomingByte);
	}
	  if(messageIndex == 0 && bytes.size() == 2) {// 2 bytes were read, decode opCode
            updateOpCode();
        }

        // Considered merging some of the methods since some of the perform similar actions but decided
        // against it. This makes things more consistent and leaves room for future expansion of functionality
        switch(opCode) {
            case 1:
                adminRegisterDecode();
                break;
            case 2:
              studentRegisterDecode();
                break;
            case 3:
               loginDecode();
                break;
            case 4:
                logoutDecode();
                break;
            case 5:
                courseRegisterDecode();
                break;
            case 6:
                kdamCheckDecode();
                break;
            case 7:
                printCourseDecode();
                break;
            case 8:
                printStudentDecode();
                break;
            case 9:
                checkIfRegisteredDecode();
                break;
            case 20:
                courseUnRegisterDecode();
                break;
            case 11:
                checkCurrentCoursesDecode();
                break;
        }


        return request;
    }


    @Override
    public byte[] encode(BGRSMessage message) {
        byte[] encodedMessage;
        if(message.getOpcode() == 12) { //Acknowledgement Message
            encodedMessage = acknowledgementEncode(message);
        } else { // Error Message
            encodedMessage = errorEncode(message);
        }
        return encodedMessage;
    }

    private void adminRegisterDecode (){
       if (incomingByte == '\n') {
           if (messageIndex == 1){
               userName = decodeNextString();
               messageIndex++ ;
           }
           else if (messageIndex == 2){
               password = decodeNextString();
               request = new BGRSMessage(opCode, userName, password, null, null, null );
               newMessage();
           }
       }
    }

    private void studentRegisterDecode () {
        if (incomingByte == '\n') {
            if (messageIndex == 1){
                userName = decodeNextString();
                messageIndex++ ;
            }
            else if (messageIndex == 2){
                password = decodeNextString();
                request = new BGRSMessage(opCode, userName, password, null, null, null );
                newMessage();
            }
        }
    }

    private void loginDecode (){
		
        if (incomingByte == '\n') {
            if (messageIndex == 1){
                userName = decodeNextString();
                messageIndex++ ;
            }
            else if (messageIndex == 2){
                password = decodeNextString();
                request = new BGRSMessage(opCode, userName, password, null, null, null );
                newMessage();
            }
        }
    }

    private void logoutDecode (){
        request = new BGRSMessage(opCode);
        newMessage();
    }

    private void courseRegisterDecode (){
        if (bytes.size() == 2){
            courseNum = decodeNextCourseNum();
            request = new BGRSMessage(opCode, null, null, courseNum, null, null);
            newMessage();
        }
    }

    private void kdamCheckDecode (){
        if (bytes.size() == 2){
            courseNum = decodeNextCourseNum();
            request = new BGRSMessage(opCode, null, null, courseNum, null, null);
            newMessage();
        }
    }

    private void printCourseDecode (){
        if (bytes.size() == 2){
            courseNum = decodeNextCourseNum();
            request = new BGRSMessage(opCode, null, null, courseNum, null, null);
            newMessage();
        }
    }

    private void printStudentDecode (){
        if (incomingByte == '\n') {
            userName = decodeNextString();
            request = new BGRSMessage(opCode, userName, null, null, null, null);
            newMessage();
        }
    }

    private void checkIfRegisteredDecode (){
        if (bytes.size() == 2){
            courseNum = decodeNextCourseNum();
            request = new BGRSMessage(opCode, null, null, courseNum, null, null);
            newMessage();
        }
    }

    private void courseUnRegisterDecode (){
        if (bytes.size() == 2){
            courseNum = decodeNextCourseNum();
            request = new BGRSMessage(opCode, null, null, courseNum, null, null);
            newMessage();
        }
    }

    private void checkCurrentCoursesDecode (){
        request = new BGRSMessage(opCode);
        newMessage();
    }



    private byte[] acknowledgementEncode (BGRSMessage message){


        byte[] encodedMessage;
        byte[] opCode = shortToBytes((short)12);
        byte[] messageOpcode = shortToBytes(message.getMessageOpcode());

        if (message.getAnswer() != null){
            byte[] responseBytes = message.getAnswer().getBytes(StandardCharsets.US_ASCII);
            encodedMessage = new byte[5 + responseBytes.length];
            encodedMessage[0] = opCode[0];
            encodedMessage[1] = opCode[1];
            encodedMessage[2] = messageOpcode[0];
            encodedMessage[3] = messageOpcode[1];
            System.arraycopy(responseBytes,0, encodedMessage, 4, responseBytes.length);
            encodedMessage[encodedMessage.length - 1]  = '\n';
            // Joins together the opcode bytes and the response bytes
        } else {
            encodedMessage = new byte[4];
            encodedMessage[0] = opCode[0];
            encodedMessage[1] = opCode[1];
            encodedMessage[2] = messageOpcode[0];
            encodedMessage[3] = messageOpcode[1];
        }
        return encodedMessage;
    }

    private byte[] errorEncode (BGRSMessage message){

        byte[] opCode = shortToBytes((short)13);
        byte[] messageOpcode = shortToBytes(message.getMessageOpcode());
        byte[] encodedMessage = new byte[4];
        encodedMessage[0] = opCode[0];
        encodedMessage[1] = opCode[1];
        encodedMessage[2] = messageOpcode[0];
        encodedMessage[3] = messageOpcode[1];

        return encodedMessage;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private void updateOpCode(){
        byte[] opBytes = new byte[2];
        opBytes[0] = bytes.get(0);
        opBytes[1] = bytes.get(1);
        opCode = bytesToShort(opBytes);
        messageIndex++;
        bytes.clear();
    }

    private String decodeNextString(){
        byte[] byteArray = new byte[bytes.size()];
        for (int i = 0; i < byteArray.length; i++){
            byteArray[i] = bytes.get(i);
        }
        bytes.clear();
        return new String(byteArray, StandardCharsets.US_ASCII);
    }

    private int decodeNextCourseNum(){
        byte[] byteArray  = new byte[2];
        byteArray[0] = bytes.getFirst();
        byteArray[1] = bytes.getLast();
        bytes.clear();
        return (int)(bytesToShort(byteArray));
    }

    private void newMessage(){ //"resets" the fields and empties the bytes
        messageIndex = 0;
        bytes.clear();
        opCode = 0;
        userName = null;
        password = null;
        courseNum = null;
    }

}
