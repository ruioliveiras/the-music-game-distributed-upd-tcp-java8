/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ruioliveiras
 */
public class ServerHandler implements Runnable {
            
    private final ServerCommunication comm;
    private final ServerState state;
    private final ServerFacade facade;
    
    public ServerHandler(ServerState state, ServerFacade facade, Socket socket) throws IOException {
        this.state = state;
        this.facade = facade;
        comm = new ServerCommunication(socket);
    }
    //contrutor disto vai receber a porta a atuar e dados inciiais
    
    
    @Override
    public void run() {
        comm.init();
        
        ServerPDU pdu;
        
        while((pdu = comm.readNext()) != null){
            foward01(pdu);
        }
    }
    
    protected void foward01(ServerPDU p){
        // the foward for eacth type will parse the p and call the currect facade function, and after send response back if need.
    }


    }
