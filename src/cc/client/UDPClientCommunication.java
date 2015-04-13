package cc.client;

import cc.pdu.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * To fragment a pdu, assuming the existence of 'int currentLabel' and 'pdu lastPDU.'
 *  
 * on readDatagram do something like this:
 * msg_received.initHeaderFromBytes(headerBuffer);
 * if (msg_received.getLabel == currentLabel){
 *   lastPDU.initHeaderFromBytes(headerBuffer);
 *   msg_received = lastPDU;
 * } 
 * msg_received.initParametersFromBytes(bodyBuffer);
 * 
 * @author paulo
 */
public class UDPClientCommunication {

    private DatagramSocket c_socket;
    private PDU lastPDU;
    private int current_label;
    
    public UDPClientCommunication(){
        try {
            c_socket = new DatagramSocket();
            current_label = 0;
            lastPDU = null;
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }
        
    public void connection_send(InetAddress dest, int port, PDU send_pdu){
        byte[] dadosEnviar = new byte[1024];
       
        DatagramPacket send_packet = new DatagramPacket(send_pdu.toByte(), send_pdu.getSizeBytes(), dest, port);

        try {   
            this.getC_socket().send(send_packet);
        } catch (IOException ex) {
            System.out.println("Erro ao enviar o datagrama para o servidor");
        }
    }
    
    /*public void connection_receive(){
        byte[] dadosEnviar = new byte[1024];
       
        DatagramPacket send_packet = new DatagramPacket(send_pdu.toByte(), send_pdu.getSizeBytes(), dest, port);

        try {   
            this.getC_socket().send(send_packet);
        } catch (IOException ex) {
            System.out.println("Erro ao enviar o datagrama para o servidor");
        }
    }*/
    
    
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
    
    
}
