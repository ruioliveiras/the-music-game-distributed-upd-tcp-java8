package cc.client;

import cc.pdu.PDU;

/**
 *
 * @author paulo
 */
public class UDPCommunication {

    //receber PDU -> ler -> construir objeto
    //construir PDU -> enviar
    
    public PDU readDatagram(byte[] pData){
        PDU msg_received = new PDU();
        int i, j;
                
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[1024];
        
        for(i=0;i<8;i++) headerBuffer[i] = pData[i];
        for(j=0;i<pData.length;i++,j++) bodyBuffer[j] = pData[i];
        
        msg_received.initHeaderFromBytes(headerBuffer);
        msg_received.initParametersFromBytes(bodyBuffer);
    
        return msg_received;
    }
    
    
    public void makeDatagram(int request_type){
    
    
    
    }
        
    
    
    
    
}
