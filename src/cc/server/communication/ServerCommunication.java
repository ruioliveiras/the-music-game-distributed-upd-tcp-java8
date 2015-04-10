/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.communication;

import cc.pdu.PDU;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author ruioliveiras
 */
public class ServerCommunication {
    private final Socket socket;
    private InputStream is;
    private OutputStream os;
    
    
    //this class is a capsutation the the socket to our PDU.
    // ths read bytes from socket and return PDU.
    // This receive a PDU and sent it.
    // ? also will has some static functions to be more easy to contruct some PDU like OK

    public ServerCommunication(Socket socket) {
        this.socket = socket;
    }
        
    public void init() throws IOException {

        is = socket.getInputStream();
        
        os = socket.getOutputStream();
    }
    
    
    public PDU readNext() throws IOException{
        PDU pdu = new PDU();
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[255];
        
        if(is.read(headerBuffer, 0, 8) == 8) {
            pdu.initHeaderFromBytes(headerBuffer);
            is.read(bodyBuffer, 0, pdu.getSizeBytes());
            pdu.initParametersFromBytes(bodyBuffer);
        }      
        
        return pdu;
    }
    
    public void sendPDU(PDU p) throws IOException{
       os.write(p.toByte());
       os.flush();
    }

    public String who() {
       return socket.getInetAddress().toString();
    }

    
}
