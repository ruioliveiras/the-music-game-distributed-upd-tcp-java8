/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.facade;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.ServerToServerFacade;
import cc.server.communication.ServerCommunication;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruioliveiras
 */
public class ServerToServerClient implements ServerToServerFacade {

    ServerCommunication comm;

    public ServerToServerClient(String ip, int port) {
        try {
            comm = new ServerCommunication(new Socket(ip, port));
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    /**
     * in the case of the ServerToServerClient, this method is equal to
     * regiserServer, the difference of comportment is in implementation
     *
     * @param ip
     * @param port
     * @return
     */
    @Override
    public boolean registerMySelfServer(byte[] ip, int port) {
        return registerServer(ip, port);
    }

    @Override
    public boolean registerServer(byte[] ip, int port) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_IPSERVER, ip);
        pdu.addParameter(PDUType.INFO_PORT, port);
        comm.sendPDU(pdu);
        return true;
    }

    @Override
    public boolean registerChallenge(String challeName, LocalDate d, LocalTime time, String user, String nick) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_CERT, challeName);
        pdu.addParameter(PDUType.INFO_DATE, d);
        pdu.addParameter(PDUType.INFO_HOUR, time);
        pdu.addParameter(PDUType.INFO_NICK, user);
        pdu.addParameter(PDUType.INFO_NAME, nick);

        comm.sendPDU(pdu);
        return true;

    }

    @Override
    public boolean registerAcceptChallenge(String challeName, String nick) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_CERT, challeName);
        pdu.addParameter(PDUType.INFO_NAME, nick);
        comm.sendPDU(pdu);
        return true;
    }

    @Override
    public boolean registerScore(String nick, int score) {
        PDU pdu = new PDU(PDUType.INFO);
        pdu.addParameter(PDUType.INFO_NAME, nick);
        pdu.addParameter(PDUType.INFO_SCORE, score);
        comm.sendPDU(pdu);
        return true;
    }

}
