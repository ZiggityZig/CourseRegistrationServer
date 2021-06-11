
#ifndef ASSIGNMENT_3_BGRSMESSAGEENCODER_H
#define ASSIGNMENT_3_BGRSMESSAGEENCODER_H

#include <boost/algorithm/string.hpp>
#include "boost/lexical_cast.hpp"
#include "../include/connectionHandler.h"
class BGRSMessageEncoder {
    public:
    BGRSMessageEncoder(ConnectionHandler *handler);
    void run();
    void shortToBytes(short num, char* bytesArr);

    private:
    bool shouldTerminate;
    ConnectionHandler *handler;
};


#endif //ASSIGNMENT_3_BGRSMESSAGEENCODER_H



