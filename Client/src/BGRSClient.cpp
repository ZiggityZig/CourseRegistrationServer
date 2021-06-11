#include "../include/BGRSClient.h"
#include "../include/BGRSMessageEncoder.h"
#include "../include/BGRSMessageDecoder.h"
#include <connectionHandler.h>
#include <thread>

using namespace std;

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler handler(host, port);
    if (!handler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    bool* shouldTerminate = new bool(false); //Stored on the heap because it needs to be easily and globally accessible
    BGRSMessageEncoder encoder = BGRSMessageEncoder(&handler);
    BGRSMessageDecoder decoder = BGRSMessageDecoder(shouldTerminate, &handler);

    thread encoderThread(&BGRSMessageEncoder::run, &encoder);
    decoder.run();
    encoderThread.join();


    delete shouldTerminate;
    return 0;
}
