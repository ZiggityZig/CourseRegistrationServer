package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        Database.getInstance(); //Database is initialized
        int port = Integer.parseInt(args[0]);
        int threads = Integer.parseInt(args[1]);

        Server.reactor(threads, port, () ->  new BGRSProtocol(), () -> new BGRSEncoderDecoder()).serve();

    }
}
