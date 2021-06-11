#include "../include/BGRSMessageEncoder.h"
#include <boost/algorithm//string.hpp>
#include "boost/lexical_cast.hpp"
#include "../include/connectionHandler.h"

using namespace std;

BGRSMessageEncoder::BGRSMessageEncoder(ConnectionHandler *handler): shouldTerminate(false), handler(handler) {}

// Making things tidy
enum command {
    AdminReg,
    StudentReg,
    Login,
    Logout,
    RegCourse,
    CheckKdamCourse,
    PrintCourse,
    PrintStudent,
    CheckIfRegistered,
    UnregCourse,
    CheckCurrentCourse,

};
command hashCommand (std::string const& commandInput){
    if (commandInput == "ADMINREG"){return AdminReg;}
    if (commandInput == "STUDENTREG"){return StudentReg;}
    if (commandInput == "LOGIN"){return Login;}
    if (commandInput == "LOGOUT"){return Logout;}
    if (commandInput == "COURSEREG"){return RegCourse;}
    if (commandInput == "KDAMCHECK"){return CheckKdamCourse;}
    if (commandInput == "COURSESTAT"){return PrintCourse;}
    if (commandInput == "STUDENTSTAT"){return PrintStudent;}
    if (commandInput == "ISREGISTERED"){return CheckIfRegistered;}
    if (commandInput == "UNREGISTER"){return UnregCourse;}
    if (commandInput == "MYCOURSES"){return CheckCurrentCourse;}
}


void BGRSMessageEncoder::run() {
    while(!shouldTerminate){
        //Gets input and separates it to different commands and parameters

        char input[2048];
        vector<string> commands;
        char bytes[2];
        cin.getline(input, 2048);
        string line(input);
        boost::split(commands, line, boost::is_any_of(" "));

        //TODO: divide in to different methods instead of writing the code in the switch case blocks
        switch (hashCommand(commands[0])){
            case AdminReg:
                shortToBytes((short)1, bytes);
                handler->sendBytes(bytes, 2);
                handler->sendLine(commands[1]);
                handler->sendLine(commands[2]);
                break;

            case StudentReg:
                shortToBytes((short)2, bytes);
                handler->sendBytes(bytes, 2);
                handler->sendLine(commands[1]);
                handler->sendLine(commands[2]);
                break;

            case Login:
                shortToBytes((short)3, bytes);
                handler->sendBytes(bytes, 2);
                handler->sendLine(commands[1]);
                handler->sendLine(commands[2]);
                break;

            case Logout:
                shortToBytes((short)4, bytes);
                handler->sendBytes(bytes, 2);
		shouldTerminate = true;
                break;

            case RegCourse:
                shortToBytes((short)5, bytes);
                handler->sendBytes(bytes, 2);
                shortToBytes(boost::lexical_cast<short>(commands[1]), bytes);
                handler->sendBytes(bytes, 2);
                break;

            case CheckKdamCourse:
                shortToBytes((short)6, bytes);
                handler->sendBytes(bytes, 2);

                shortToBytes(boost::lexical_cast<short>(commands[1]), bytes);
                handler->sendBytes(bytes, 2);
                break;

            case PrintCourse:
                shortToBytes((short)7, bytes);
                handler->sendBytes(bytes, 2);
                shortToBytes(boost::lexical_cast<short>(commands[1]), bytes);
                handler->sendBytes(bytes, 2);
                break;

            case PrintStudent:
                shortToBytes((short)8, bytes);
                handler->sendBytes(bytes, 2);
                handler->sendLine(commands[1]);
                break;

            case CheckIfRegistered:
                shortToBytes((short)9, bytes);
                handler->sendBytes(bytes, 2);
                shortToBytes(boost::lexical_cast<short>(commands[1]), bytes);
                handler->sendBytes(bytes, 2);
                break;

            case UnregCourse:
                shortToBytes((short)20, bytes); //For some reason the number 10 resgisters as 0.
                handler->sendBytes(bytes, 2);
                shortToBytes(boost::lexical_cast<short>(commands[1]), bytes);
                handler->sendBytes(bytes, 2);
                break;

            case CheckCurrentCourse:
                shortToBytes((short)11, bytes);
		handler->sendBytes(bytes, 2);
                break;
        }
    }
}

void BGRSMessageEncoder::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}
