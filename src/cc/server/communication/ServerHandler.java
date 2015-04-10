/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.communication;

import cc.server.ServerState;
import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.ServerToServerFacade;
import cc.server.facade.ServerToServer;
import cc.server.facade.ServerToServerHub;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruioliveiras
 */
public class ServerHandler implements Runnable {

    private final ServerCommunication comm;
    private final ServerState state;
    private final ServerToServer facadeMem;
    private final ServerToServerHub facadeHub;

    public ServerHandler(ServerState state, Socket socket, ServerToServer facadeMem, ServerToServerHub facadeHub ) throws IOException {
        this.state = state;
        this.facadeMem = facadeMem;
        this.facadeHub =  facadeHub;
        comm = new ServerCommunication(socket);
    }
    //contrutor disto vai receber a porta a atuar e dados inciiais

    @Override
    public void run() {
        try {
            comm.init();

            PDU pdu;

            while ((pdu = comm.readNext()) != null) {
                if (pdu.getVersion() != 0) {
                    // somefucking unsoported error
                }
                //if this pdu are fragmented, don't wory, because the the serverConnunication work on this and this pdu area already done
                
                foward01(pdu);
               
            }

        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
        } else  if ((p = checkRequest(pdu, newServer)) != null) {
            
            // if the server that are announcing don't exist announce the the other server
            if (!state.hasNeighbors(comm.who())) {
                facadeHub.registerServer(  (byte[]) p[0], (Integer) p[1]);
                facadeMem.registerServer((byte[]) p[0], (Integer) p[1] );
                for (String ip : state.getNeighborIps()) {
                    byte[] ipBytes = InetAddress.getByName(ip.split(":")[0]).getAddress();
                    int port = Integer.parseInt(ip.split(":")[1]);
                    state.getNeighbor(ip).registerServer(ipBytes, port);
                }
            }else{
                facadeMem.registerServer((byte[]) p[0], (Integer) p[1] );
            }
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
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

    private Object[] checkRequest(PDU pdu, PDUType[] required) {
        Object[] ret = new Object[required.length];
        int i = 0;
        for (PDUType p : required) {
            if (!pdu.hasParameter(p)) {
                return null;
            } else {
                ret[i++] = pdu.getParameter(p);
            }
        }
        return ret;
    }
}
