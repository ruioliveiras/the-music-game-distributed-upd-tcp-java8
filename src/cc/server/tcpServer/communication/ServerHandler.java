/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.tcpServer.communication;

import cc.server.tcpServer.ServerState;
import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.tcpServer.facade.ServerToServerLocal;
import cc.server.tcpServer.facade.ServerToServerClient;
import cc.server.tcpServer.facade.ServerToServerHub;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class meets a client and call the correct functions has need.
 *
 * @author ruioliveiras
 */
public class ServerHandler implements Runnable {

    private final ServerCommunication comm;
    private final ServerState state;
    private final ServerToServerLocal facadeMem;
    private final ServerToServerHub facadeHub;
    private final String name;

    /**
     * Main Constructor of ServerHandler
     * 
     * @param name
     * @param state
     * @param socket
     * @param facadeMem
     * @param facadeHub
     * @throws IOException 
     */
    public ServerHandler(String name, ServerState state, Socket socket,
            ServerToServerLocal facadeMem, ServerToServerHub facadeHub
    ) throws IOException {
        this.name = name;
        this.state = state;
        this.facadeMem = facadeMem;
        this.facadeHub = facadeHub;
        comm = new ServerCommunication(socket);
    }

    @Override
    public void run() {
        try {
            PDU pdu;

            while ((pdu = comm.readNext()) != null) {
                if (pdu.getVersion() != 0) {
                    // somefucking unsoported error
                }
                //if this pdu are fragmented, don't wory, because the the serverConnunication work on this and this pdu area already done
                System.out.println("i'm:" + name + ",attending:" + comm.getIp() + " - " + pdu);
                foward01(pdu);

            }

        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This function receive a PDU and call the correct function, also there are
     * some exceptional cases - where can't only call a method
     *
     * Note this function works for the version 01 of the PDU.
     * 
     * @param pdu
     * @throws UnknownHostException
     */
    protected void foward01(PDU pdu) throws UnknownHostException {
        if (pdu.getType().getId() != PDUType.INFO.getId()) {
            // error this is just INFOS 
        }

        PDUType[] newChallenge = {PDUType.INFO_CHALLE, PDUType.INFO_DATE, PDUType.INFO_HOUR, PDUType.INFO_NAME, PDUType.INFO_NICK};
        PDUType[] newServer = {PDUType.INFO_IPSERVER, PDUType.INFO_PORT};
        PDUType[] registerAcceptChallenge = {PDUType.INFO_NICK, PDUType.INFO_CHALLE};
        PDUType[] registerScore = {PDUType.INFO_NICK, PDUType.INFO_CHALLE};
        Object[] p;

        if ((p = checkRequest(pdu, newChallenge)) != null) {
            facadeMem.registerChallenge(
                    (String) p[0],
                    (LocalDate) p[1],
                    (LocalTime) p[2],
                    (String) p[3],
                    (String) p[4]
            );
            state.addOwner((String) p[0], comm.getIp());
        } else if ((p = checkRequest(pdu, newServer)) != null) {
            final InetAddress ip = (InetAddress) p[0];
            final Short port = (Short) p[1];

            // Test if it is exceptional cases
            if (state.hasNeighbors(comm.getIp())) {
                facadeMem.registerServer(ip, port);
            } else {
                // if the server that are announcing don't exist (are not in the neighbors):
                //     -> announce the newServer to my neighbors
            
                // register in other servers
                facadeHub.registerServer(ip, port);
                // register in the current server
                facadeMem.registerServer(ip, port);
                // register all server in the newServer
                state.getNeighbors().stream()
                        .filter((server) -> (server instanceof ServerToServerClient))
                        .map(server -> (ServerToServerClient) server)
                        // Because the new server are my neighbor, test it.
                        .filter((server) -> (!server.getServerIp().equals(ip.toString())))
                        .forEach((server) -> {
                            state.getNeighbor(ip.toString())
                            .registerServer(server.getServerIpByte(), server.getServerPort());
                        });

            }
        } else if ((p = checkRequest(pdu, registerAcceptChallenge)) != null) {
            facadeMem.registerAcceptChallenge(
                    (String) p[0],
                    (String) p[1]
            );
        } else if ((p = checkRequest(pdu, registerScore)) != null) {
            facadeMem.registerScore(
                    (String) p[0],
                    (Integer) p[1]
            );
        }
    }

    /**
     * Simple auxiliary function
     * @param pdu
     * @param required
     * @return 
     */
    private Object[] checkRequest(PDU pdu, PDUType[] required) {
        Object[] ret = new Object[required.length];
        int i = 0;
        for (PDUType p : required) {
            if (!pdu.hasParameter(p)) {
                return null;
            } else {
                ret[i++] = pdu.popParameter(p);
            }
        }
        return ret;
    }
}
