/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.facade;

import cc.model.Challenge;
import cc.model.User;
import cc.server.ServerState;
import cc.server.ServerToServerFacade;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ruioliveiras
 */
public class ServerToServer implements ServerToServerFacade {

    private final ServerState state;

    public ServerToServer(ServerState s) {
        this.state = s;
    }

    @Override
    public void registerServer(InetAddress ip, int port) {
        state.addNeighbors(ip, port);
    }

    @Override
    public void registerChallenge(String challeName, LocalDate d, LocalTime time, String user, String nick) {
        state.addChallenge(challeName, new Challenge(challeName, d, time));
    }

    @Override
    public void registerAcceptChallenge(String challeName, String nick) {
        String ip = state.getOwnerIp(challeName);
        if (ip.equals("localhost")) {
            state.getChallenge(challeName)
                    .addSubscribers(new User(nick, nick));
        } else {
            state.getNeighbor(ip).registerAcceptChallenge(challeName, nick);
        }
    }

    @Override
    public void registerScore(String nick, int score) {

    }

    // this guy will has a copy of the serverState
    // for each action recieve the currect arguments, this don't know that is a PDUServer
}
