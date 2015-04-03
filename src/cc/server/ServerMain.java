/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import cc.server.facade.ServerToServer;
import cc.server.communication.ServerHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author ruioliveiras
 */
public class ServerMain {
    private final static int DEFAULT_PORT = 1010;
    public static ServerState state;
    public static ServerToServer facade;
    private final ServerSocket ss;
    //vai estar declarado static aqui um server state, e um server facade.
    //vai ter um metodo para começar o server handler 

    public ServerMain(int listingPort) throws IOException {
        this.ss = new ServerSocket(listingPort);
    }
    
    
    public void init( String initIp, String initPort){
        
    }

    
    public void start() throws IOException{
         while(true){
            Socket cn = ss.accept();
            ServerHandler handler = new ServerHandler(state, facade, cn);
            Thread t = new Thread(handler);           
            t.start();
        }
    }
    

    public static void main(String[] args) throws IOException {
        int port;
        if(args.length >= 2){
            port = Integer.parseInt(args[1]);
        } else {
            port = DEFAULT_PORT;
        }
        ServerMain main = new ServerMain(port);
        main.start();
    }
    
}
