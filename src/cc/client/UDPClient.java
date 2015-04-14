package cc.client;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author paulo
 */
public class UDPClient {

    private InetAddress dest_ip;
    private int dest_port;
    private UDPClientCommunication udp_com;
    private PDUToUser ptu;
    private PDU lastPDU;
    private int current_label;
    
    public UDPClient(String sourceIp, int sourcePort, String dest, int port) {
        try {
            dest_ip = InetAddress.getByName(dest);
            dest_port = port;
            udp_com = new UDPClientCommunication(sourcePort,InetAddress.getByName(sourceIp));
            ptu = new PDUToUser();
            lastPDU = null;
            current_label=0;
        } catch (UnknownHostException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public UDPClient(String dest, int port) {
        try {
            dest_ip = InetAddress.getByName(dest);
            dest_port = port;
            udp_com = new UDPClientCommunication();
            ptu = new PDUToUser();
            lastPDU = null;
            current_label=0;
        } catch (UnknownHostException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public UDPClientCommunication getUDPClientCom() {
        return udp_com;
    }
    
    public PDUToUser getPDUToUser(){
        return ptu;
    }
    
    public int getCurrentLabel(){
        return current_label;
    }
    
    public PDU getLastPDU(){
        return lastPDU;
    }
    
    public void makeDatagramHello(){
        PDU send = new PDU(PDUType.HELLO);        
        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processOk();
            current_label++;
        }        
    }

    public void makeDatagramRegister(String name, String alcunha, byte[] sec_info) {
        PDU send = new PDU(PDUType.REGISTER);

        send.addParameter(PDUType.REGISTER_NAME, name);
        send.addParameter(PDUType.REGISTER_NICK, alcunha);
        send.addParameter(PDUType.REGISTER_PASS, sec_info);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processOk();
            current_label++;
        }   
    }

    public void makeDatagramLogin(String alcunha, byte[] sec_info) {
        PDU send = new PDU(PDUType.LOGIN);

        send.addParameter(PDUType.LOGIN_NICK, alcunha);
        send.addParameter(PDUType.LOGIN_PASS, sec_info);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processLogin(receive);
            current_label++;
        }   
    }

    public void makeDatagramLogout() {
        PDU send = new PDU(PDUType.LOGOUT);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processOk();
            current_label++;
        }   
    }

    public void makeDatagramQuit() {
        PDU send = new PDU(PDUType.QUIT);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processOk();
            current_label++;
        }   
    }

    public void makeDatagramEnd() {
        PDU send = new PDU(PDUType.END);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processEnd(receive);
            current_label++;
        }   
    }
    
    public void makeDatagramListChallenges(){
        PDU send = new PDU(PDUType.LIST_CHALLENGES);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processChallenges(receive);
            current_label++;
        }   
    }
    
    public void makeDatagramMakeChallenge(String desafio, LocalDate data, LocalTime hora){
        PDU send = new PDU(PDUType.MAKE_CHALLENGE);

        send.addParameter(PDUType.MAKE_CHALLENGE_CHALLENGE, desafio);
        send.addParameter(PDUType.MAKE_CHALLENGE_DATE, data);
        send.addParameter(PDUType.MAKE_CHALLENGE_HOUR, hora);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processChallenges(receive);
            current_label++;
        }   
    }

    public void makeDatagramAcceptChallenge(String desafio) {
        PDU send = new PDU(PDUType.ACCEPT_CHALLENGE);

        send.addParameter(PDUType.ACCEPT_CHALLENGE_CHALLENGE, desafio);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processOk();
            current_label++;
        } 
    }

    public void makeDatagramDeleteChallenge(String desafio) {
        PDU send = new PDU(PDUType.DELETE_CHALLENGE);

        send.addParameter(PDUType.DELETE_CHALLENGE_CHALLENGE, desafio);

        udp_com.connection_send(dest_ip, dest_port, send);

        
        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processChallenges(receive);
            current_label++;
        } 
    }

    
    //falta implementar a lógica desta
    //
    //
    //
    //
    public void makeDatagramAnswer(Byte escolha, String desafio, Byte questao){
        PDU send = new PDU(PDUType.ANSWER);

        send.addParameter(PDUType.ANSWER_CHOOSE, escolha);
        send.addParameter(PDUType.ANSWER_CHALLENGE, desafio);
        send.addParameter(PDUType.ANSWER_NQUESTION, questao);

        udp_com.connection_send(dest_ip, dest_port, send);
        
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            //ptu.processAnswer(receive);
            current_label++;
        } 
    }
    //
    public void makeDatagramRetransmit(String desafio, Byte questao, Byte bloco){
        PDU send = new PDU(PDUType.RETRANSMIT);

        send.addParameter(PDUType.RETRANSMIT_CHALLENGE, desafio);
        send.addParameter(PDUType.RETRANSMIT_NQUESTION, questao);
        send.addParameter(PDUType.RETRANSMIT_NBLOCK, bloco);

        udp_com.connection_send(dest_ip, dest_port, send);
        
    }

    public void makeDatagramList_Ranking() {
        PDU send = new PDU(PDUType.LIST_RANKING);

        udp_com.connection_send(dest_ip, dest_port, send);
    
        PDU receive = receivePacket();
        if(receive.getLabel() == current_label) {
            ptu.processRankings(receive);
            current_label++;
        }    
    }
    
    public void closeCSocket(){
        udp_com.getC_socket().close();
    }
   
    
    public PDU receivePacket(){
        byte[] datagram_packet = udp_com.connectionReceive();
        
        PDU pdu_packet = readDatagram(datagram_packet);
        
        return pdu_packet;
    }

    public PDU readDatagram(byte[] pData) {
        PDU msg_received = new PDU();
        int i, j;

        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[1024];

        for (i = 0; i < 8; i++) {
            headerBuffer[i] = pData[i];
        }
        for (j = 0; i < pData.length; i++, j++) {
            bodyBuffer[j] = pData[i];
        }

        msg_received.initHeaderFromBytes(headerBuffer);
        msg_received.initParametersFromBytes(bodyBuffer);

        return msg_received;
    }

}
