#include "../include/BGRSMessageDecoder.h"
#include <string>
#include <iostream>
#include "../include/connectionHandler.h"

using namespace std;

BGRSMessageDecoder::BGRSMessageDecoder(bool *shouldTerminate, ConnectionHandler *handler): shouldTerminate(shouldTerminate), handler(handler) {}


void BGRSMessageDecoder::run() {

    short opcode;
    short MsgOpcode;
    char *bytes = new char[2];
    string reply;
    while(! *shouldTerminate){

        handler->getBytes(bytes, 2);
        opcode = bytesToShort(bytes);
        if (opcode == (short)13) {
            handler->getBytes(bytes, 2);
            MsgOpcode = bytesToShort(bytes);
            cout << "ERROR " << MsgOpcode << endl;
        }
        else {
            handler->getBytes(bytes, 2);
            MsgOpcode = bytesToShort(bytes);
	    reply = "";
            switch ((int)MsgOpcode){
                case 1:
                    cout << "ACK " << MsgOpcode << endl;
                    break;

                case 2:
                    cout << "ACK " << MsgOpcode << endl;
                    break;

                case 3:
                    cout << "ACK " << MsgOpcode << endl;
                    break;

                case 4:
                    cout << "ACK " << MsgOpcode << endl;
                    *shouldTerminate = true;
                    break;

                case 5:
                    cout << "ACK " << MsgOpcode << endl;
                    break;

                case 6:
                    cout << "ACK " << MsgOpcode << endl;
		    handler->getLine(reply);
                    cout << reply << endl;
                    break;

  		case 7:
                    cout << "ACK " << MsgOpcode << endl;
                    handler->getLine(reply);
                    cout << reply << endl;
                    break;

                case 8:
                    cout << "ACK " << MsgOpcode << endl;
                    handler->getLine(reply);
                    cout << reply << endl;
                    break;

                case 9:
                    cout << "ACK " << MsgOpcode << endl;
                    handler->getLine(reply);
                    cout << reply << endl;
                    break;

                case 20:
                    cout << "ACK " << "10" << endl;
                    break;

                case 11:
                    cout << "ACK " << MsgOpcode << endl;
                    handler->getLine(reply);
                    cout << reply << endl;
                    break;
            }
        }
    }
    delete bytes;
}

short BGRSMessageDecoder::bytesToShort(char* bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}
