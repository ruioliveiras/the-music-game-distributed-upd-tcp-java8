/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import cc.server.facade.ServerToServer;
import cc.server.communication.ServerHandler;
import cc.server.facade.ServerToServerClient;
import cc.server.facade.ServerToServerHub;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 *
 * @author ruioliveiras
 */
public class ServerMain {

    public final static int DEFAULT_PORT = 8080;
    public static ServerState state;
    private final ServerSocket ss;
    private final ServerToServer facadeMem;
    private final ServerToServerHub facadeHub;

    //vai estar declarado static aqui um server state, e um server facade.
    //vai ter um metodo para começar o server handler 
    public ServerMain(int listingPort) throws IOException {
        this.ss = new ServerSocket(listingPort);
        this.facadeMem = new ServerToServer(state);
        this.facadeHub =  new ServerToServerHub(state);
    }

    public void init(String initIp, String initPort) throws UnknownHostException {
        facadeMem.registerServer(InetAddress.getByName(initIp).getAddress(), Integer.parseInt(initPort));
    }

    public void start() throws IOException {
        while (true) {
            Socket cn = ss.accept();
            ServerHandler handler = new ServerHandler(state, cn,facadeMem,facadeHub);
            Thread t = new Thread(handler);
            t.start();
        }
    }

    public static void main(String[] args) throws IOException {
        int port;
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        } else {
            port = DEFAULT_PORT;
        }
        ServerMain main = new ServerMain(port);
        main.start();

        // comment this is want to test
        ServerMain main2 = new ServerMain(port);
        main2.init("localhost", DEFAULT_PORT +"");
        main2.start();
        //main2.facadeHub.registerChallenge(null, LocalDate.MIN, LocalTime.MIN, null, null);
    }

}
