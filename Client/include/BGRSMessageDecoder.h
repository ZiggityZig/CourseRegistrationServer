
#ifndef ASSIGNMENT_3_BGRSMESSAGEDECODER_H
#define ASSIGNMENT_3_BGRSMESSAGEDECODER_H

#include <string>
#include <iostream>
#include "../include/connectionHandler.h"

using namespace std;
class BGRSMessageDecoder {
        public:
        BGRSMessageDecoder(bool *shouldTerminate, ConnectionHandler *handler);
        void run();
        short bytesToShort(char* bytesArr);

        private:
        bool *shouldTerminate;
        ConnectionHandler *handler;

};


#endif //ASSIGNMENT_3_BGRSMESSAGEDECODER_H
