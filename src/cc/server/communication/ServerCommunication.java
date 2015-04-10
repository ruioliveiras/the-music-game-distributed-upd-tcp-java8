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
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            this.socket = socket;
            is = socket.getInputStream();   
            os = socket.getOutputStream();
        } catch (IOException ex) {
            throw  new RuntimeException("a");
        }
    }
    
    
    public PDU readNext() throws IOException{
        PDU pdu = new PDU();
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[1024];
        
        if(is.read(headerBuffer, 0, 8) == 8) {
            pdu.initHeaderFromBytes(headerBuffer);
            is.read(bodyBuffer, 0, pdu.getSizeBytes());
            pdu.initParametersFromBytes(bodyBuffer);
        }      
        
        return pdu;
    }
    
    public void sendPDU(PDU p){
        try {
            os.write(p.toByte());
            os.flush();
        } catch (IOException ex) {
            throw  new RuntimeException("a");
        }
    }

    public String getIp() {
       return socket.getInetAddress().toString();
    }

    public int getPort() {
        return socket.getPort();
    }

    public InetAddress getIpByte() {
        return socket.getInetAddress();
    }


    
}
