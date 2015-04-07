/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This interface declare all the business that can be done in server to server
 * communication.
 *
 * @author ruioliveiras
 */
public interface ServerToServerFacade {

    /**
     * This function will be called in the init of the server to register than
     * in a branch of server
     *
     * @param ip
     * @param port
     * @return
     */
    public boolean registerMySelfServer(byte[] ip, int port);

    /**
     * This function is to ask the server to registry a new server,
     *
     * @param ip
     * @param port
     * @return
     */
    public boolean registerServer(byte[] ip, int port);

    /**
     * Register a Challenge to a server
     *
     * @param challeName
     * @param d
     * @param time
     * @param user
     * @param nick
     * @return
     */
    public boolean registerChallenge(String challeName, LocalDate d, LocalTime time,
            String user, String nick);

    /**
     * Accept challenge, and ask to receive the challenge info. This request
     * should be send only to the server owner of the Challenge
     *
     * @param challeName
     * @param nick
     * @return
     */
    public boolean registerAcceptChallenge(String challeName, String nick);

    /**
     * In the end of challenge should register the score of each person
     *
     * @param nick
     * @param score
     * @return
     */
    public boolean registerScore(String nick, int score);
}
