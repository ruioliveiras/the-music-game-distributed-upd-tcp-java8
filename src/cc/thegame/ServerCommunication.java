/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.thegame;

import cc.server.ServerPDU;

/**
 *
 * @author ruioliveiras
 */
public class ServerCommunication {
    //this class is a capsutation the the socket to our PDU.
    // ths read bytes from socket and return ServerPDU.
    // This receive a ServerPDU and sent it.
    // ? also will has some static functions to be more easy to contruct some PDU like OK
    
    public ServerPDU readNext(){
        //for a ler byte to byte
        //a a medida que le byte usar appdendByte(Byte )
        return null;
    }
    
    public void sendPDU(ServerPDU p){
        
    }
}
