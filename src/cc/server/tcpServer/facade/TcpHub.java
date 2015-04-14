/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.tcpServer.facade;

import cc.server.tcpServer.ServerState;
import cc.server.ServerToServerFacade;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ruioliveiras
 */
public class TcpHub implements ServerToServerFacade {

    private final ServerState state;

    public TcpHub(ServerState s) {
        this.state = s;
    }

    @Override
    public void registerServer(InetAddress ip, int port) {
        for (ServerToServerFacade sts : state.getNeighbors()) {
            sts.registerServer(ip, port);
        }
    }

    @Override
    public void registerChallenge(String challeName, LocalDate d, LocalTime time, String user, String nick) {
        for (ServerToServerFacade sts : state.getNeighbors()) {
            sts.registerChallenge(challeName, d, time, user, nick);
        }
    }

    @Override
    public void registerAcceptChallenge(String challeName, String nick) {
        for (ServerToServerFacade sts : state.getNeighbors()) {
            sts.registerAcceptChallenge(challeName, nick);
        }
    }

    @Override
    public void registerScore(String nick, int score) {
        for (ServerToServerFacade sts : state.getNeighbors()) {
            sts.registerScore(nick, score);
        }
    }

}
