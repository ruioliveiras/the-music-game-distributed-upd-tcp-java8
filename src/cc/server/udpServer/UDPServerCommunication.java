/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.udpServer;

import cc.pdu.PDU;

/**
 *
 * @author paulo
 */
public class UDPServerCommunication {
 
    public PDU readDatagram(byte[] pData){
        PDU msg_received = new PDU();
        int i, j;
                
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[1024];
        
        for(i=0;i<8;i++) headerBuffer[i] = pData[i];
        for(j=0;i<pData.length;i++,j++) bodyBuffer[j] = pData[i];
        
        msg_received.initHeaderFromBytes(headerBuffer, 0);
        msg_received.initParametersFromBytes(bodyBuffer, 0);
    
        return msg_received;
    }
    
    
    
}
