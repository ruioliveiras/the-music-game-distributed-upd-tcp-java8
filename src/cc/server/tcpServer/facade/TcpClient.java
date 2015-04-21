/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.tcpServer.facade;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.ServerToServerFacade;
import cc.server.tcpServer.communication.TCPCommunication;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ruioliveiras
 */
public class TcpClient implements ServerToServerFacade {

    TCPCommunication comm;

    public TcpClient(InetAddress ip, int port) {
        try {
            comm = new TCPCommunication(new Socket(ip, port));
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    @Override
    synchronized public void registerServer(InetAddress ip, int port) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_IPSERVER, ip);
        pdu.addParameter(PDUType.INFO_PORT, port);
        comm.sendPDU(pdu);
    }

    @Override
    synchronized public void registerChallenge(String challeName, LocalDate d, LocalTime time, String user, String nick) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_CERT, challeName);
        pdu.addParameter(PDUType.INFO_DATE, d);
        pdu.addParameter(PDUType.INFO_HOUR, time);
        pdu.addParameter(PDUType.INFO_NICK, user);
        pdu.addParameter(PDUType.INFO_NAME, nick);

        comm.sendPDU(pdu);
    }

    @Override
    synchronized public void registerAcceptChallenge(String challeName, String nick) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_CERT, challeName);
        pdu.addParameter(PDUType.INFO_NAME, nick);
        comm.sendPDU(pdu);
    }

    @Override
    synchronized public void registerScore(String nick, int score) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_NAME, nick);
        pdu.addParameter(PDUType.INFO_SCORE, score);
        comm.sendPDU(pdu);
    }

    /**
     * Get this port where this Client are connected
     * @return the port number
     */
    synchronized public int getServerPort(){
        return comm.getPort();
    } 

    /**
     * Get Ip as String
     * @return 
     */
    synchronized public String getServerIp(){
        return comm.getIp();
    } 
    
    /**
     * Get ip As 
     * @return 
     */
    synchronized public InetAddress getServerIpByte(){
        return comm.getIpByte();
    } 
}
