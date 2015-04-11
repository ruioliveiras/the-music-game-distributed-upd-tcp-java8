/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import cc.server.facade.ServerToServer;
import cc.server.communication.ServerHandler;
import cc.server.facade.ServerToServerHub;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ruioliveiras
 */
public class ServerMain {

    private final static int DEFAULT_PORT = 8080;
    private final ServerState state;
    private final ServerSocket ss;
    private final ServerToServer facadeMem;
    private final ServerToServerHub facadeHub;
    private final String name;

    //vai estar declarado static aqui um server state, e um server facade.
    //vai ter um metodo para começar o server handler 
    public ServerMain(int listingPort,InetAddress adr) throws IOException {
        this.ss = new ServerSocket(listingPort,0,adr);
        this.state = new ServerState();
        this.facadeMem = new ServerToServer(state);
        this.facadeHub =  new ServerToServerHub(state);
        name = ""+listingPort;
    }

    public void init(String initIp, String initPort) throws UnknownHostException {
        facadeMem.registerServer(InetAddress.getByName(initIp), Integer.parseInt(initPort));
        state.getNeighbor(InetAddress.getByName(initIp).toString())
                    .registerServer(this.ss.getInetAddress(), this.ss.getLocalPort());
    }

    public void start() throws IOException {
        while (true) {
            Socket cn = ss.accept();
            ServerHandler handler = new ServerHandler(name,state, cn,facadeMem,facadeHub);
            Thread t = new Thread(handler,name+"|handle:"+cn.getRemoteSocketAddress().toString());
            t.start();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port;
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        } else {
            port = DEFAULT_PORT;
        }
        ServerMain main = new ServerMain(port,InetAddress.getByName("127.0.0.1"));
        Thread t = new Thread(() -> {
            try {
                main.start();
            } catch (IOException ex) {
                throw  new RuntimeException("howad");
            }
        },"MainServer1");
        t.start();
        test();

        
        //main2.facadeHub.registerChallenge(, LocalDate.MIN, LocalTime.MIN, null, null);
    }

    
    public static void test() throws IOException, InterruptedException{
        int port = 8080;        
        Thread.sleep(400);
        System.out.println("2-------");

        // comment this is want to test
        ServerMain main2 = new ServerMain(port + 1,InetAddress.getByName("127.0.0.2"));
        main2.init("127.0.0.1", port +"");
        startInOtherThread(main2, "MainServer2");
        
                Thread.sleep(1000);
        System.out.println("3-------");

        
        ServerMain main3 = new ServerMain(port + 2,InetAddress.getByName("127.0.0.3"));
        main3.init("127.0.0.1", port +"");
        startInOtherThread(main3, "MainServer2");
        
                Thread.sleep(1000);
        System.out.println("4-------");

        ServerMain main4 = new ServerMain(port + 3,InetAddress.getByName("127.0.0.4"));
        main4.init("127.0.0.1", port +"");
        startInOtherThread(main4, "MainServer4");
    }
    
    public static void startInOtherThread(final ServerMain main,String name){
        Thread t = new Thread(() -> {
            try {
                main.start();
            } catch (IOException ex) {
                throw  new RuntimeException("howad");
            }
        },name);
        t.start();
    }
}
