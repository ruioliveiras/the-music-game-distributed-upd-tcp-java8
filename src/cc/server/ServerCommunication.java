/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author ruioliveiras
 */
public class ServerCommunication {
    private static int bufferSize = 1024;
    
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    
    public ServerCommunication() throws IOException {
    }
    
    public void init() throws IOException {

        is = socket.getInputStream();
        
        os = socket.getOutputStream();
    }
    
    //this class is a capsutation the the socket to our PDU.
    // ths read bytes from socket and return ServerPDU.
    // This receive a ServerPDU and sent it.
    // ? also will has some static functions to be more easy to contruct some PDU like OK

    public ServerCommunication(Socket socket) {
        this.socket = socket;
    }
    
    public ServerPDU readNext() throws IOException{
        ServerPDU pdu = new ServerPDU();
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[255];
        
        while(is.read(headerBuffer, 0, 8) == 8) {
            pdu.appendHeader(headerBuffer);
            is.read(bodyBuffer, 0, pdu.getSizeBytes());
            pdu.appendArgsBytes(bodyBuffer);
        }
        
        return pdu;
    }
    
    public void sendPDU(ServerPDU p){
        
    }

    
}
