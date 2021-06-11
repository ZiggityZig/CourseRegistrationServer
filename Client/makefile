CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
# LDFLAGS:=-lWs2_32
LDFLAGS:=-lboost_system -pthread

all: BGRSclient
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/BGRSMessageDecoder.o bin/BGRSClient.o bin/BGRSMessageEncoder.o $(LDFLAGS)

BGRSclient: bin/connectionHandler.o bin/BGRSMessageDecoder.o bin/BGRSClient.o bin/BGRSMessageEncoder.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BGRSMessageDecoder.o: src/BGRSMessageDecoder.cpp
	g++ $(CFLAGS) -o bin/BGRSMessageDecoder.o src/BGRSMessageDecoder.cpp

bin/BGRSClient.o: src/BGRSClient.cpp
	g++ $(CFLAGS) -o bin/BGRSClient.o src/BGRSClient.cpp

bin/BGRSMessageEncoder.o: src/BGRSMessageEncoder.cpp
	g++ $(CFLAGS) -o bin/BGRSMessageEncoder.o src/BGRSMessageEncoder.cpp

.PHONY: clean
clean:
	rm -f bin/*
