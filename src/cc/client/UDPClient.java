package cc.client;

import cc.model.Question;
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

    public UDPClient(String sourceIp, int sourcePort, String dest, int port) {
        try {
            dest_ip = InetAddress.getByName(dest);
            dest_port = port;
            udp_com = new UDPClientCommunication(sourcePort, InetAddress.getByName(sourceIp));
            ptu = new PDUToUser();

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
        } catch (UnknownHostException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public UDPClientCommunication getUDPClientCom() {
        return udp_com;
    }

    public PDUToUser getPDUToUser() {
        return ptu;
    }

    public void makeDatagramHello() {
        PDU send = new PDU(PDUType.HELLO);
        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();
//        if(receive.getLabel() == current_label) {
//            
//            current_label++;
//        }        
    }

    public void makeDatagramRegister(String name, String alcunha, byte[] sec_info) {
        PDU send = new PDU(PDUType.REGISTER);

        send.addParameter(PDUType.REGISTER_NAME, name);
        send.addParameter(PDUType.REGISTER_NICK, alcunha);
        send.addParameter(PDUType.REGISTER_PASS, sec_info);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();
//        if(receive.getLabel() == current_label) {
//            
//            current_label++;
//        }   
    }

    public void makeDatagramLogin(String alcunha, byte[] sec_info) {
        PDU send = new PDU(PDUType.LOGIN);

        send.addParameter(PDUType.LOGIN_NICK, alcunha);
        send.addParameter(PDUType.LOGIN_PASS, sec_info);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processLogin(receive);
    }

    public void makeDatagramLogout() {
        PDU send = new PDU(PDUType.LOGOUT);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();

    }

    public void makeDatagramQuit() {
        PDU send = new PDU(PDUType.QUIT);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();
    }

    public void makeDatagramEnd() {
        PDU send = new PDU(PDUType.END);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processEnd(receive);
    }

    public void makeDatagramListChallenges() {
        PDU send = new PDU(PDUType.LIST_CHALLENGES);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processChallenges(receive);
    }

    public void makeDatagramMakeChallenge(String desafio, LocalDate data, LocalTime hora) {
        PDU send = new PDU(PDUType.MAKE_CHALLENGE);

        send.addParameter(PDUType.MAKE_CHALLENGE_CHALLENGE, desafio);
        send.addParameter(PDUType.MAKE_CHALLENGE_DATE, data);
        send.addParameter(PDUType.MAKE_CHALLENGE_HOUR, hora);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processChallenges(receive);
    }

    public void makeDatagramAcceptChallenge(String desafio) {
        PDU send = new PDU(PDUType.ACCEPT_CHALLENGE);

        send.addParameter(PDUType.ACCEPT_CHALLENGE_CHALLENGE, desafio);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();

        //@todo: ficar a espera de resposta do servidor com proxima questao ou erro
    }

    public void makeDatagramDeleteChallenge(String desafio) {
        PDU send = new PDU(PDUType.DELETE_CHALLENGE);

        send.addParameter(PDUType.DELETE_CHALLENGE_CHALLENGE, desafio);

        udp_com.sendPDU(dest_ip, dest_port, send);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processChallenges(receive);
    }

    //falta implementar a lógica desta
    //
    //
    //
    //
    public void makeDatagramAnswer(Byte escolha, String desafio, Byte questao) {
        PDU send = new PDU(PDUType.ANSWER);

        send.addParameter(PDUType.ANSWER_CHOOSE, escolha);
        send.addParameter(PDUType.ANSWER_CHALLENGE, desafio);
        send.addParameter(PDUType.ANSWER_NQUESTION, questao);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        //ptu.processAnswer(receive);
    }

    //
    public void makeDatagramRetransmit(String desafio, Byte questao, Byte bloco) {
        PDU send = new PDU(PDUType.RETRANSMIT);

        send.addParameter(PDUType.RETRANSMIT_CHALLENGE, desafio);
        send.addParameter(PDUType.RETRANSMIT_NQUESTION, questao);
        send.addParameter(PDUType.RETRANSMIT_NBLOCK, bloco);

        udp_com.sendPDU(dest_ip, dest_port, send);

    }

    public void makeDatagramList_Ranking() {
        PDU send = new PDU(PDUType.LIST_RANKING);

        udp_com.sendPDU(dest_ip, dest_port, send);

        PDU receive = udp_com.nextPDU();
        ptu.processRankings(receive);
    }
    
    public void closeCSocket() {
        udp_com.getC_socket().close();
    }

}
