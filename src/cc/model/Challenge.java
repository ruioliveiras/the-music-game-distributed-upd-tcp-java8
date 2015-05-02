/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.model;

import cc.server.tcpServer.facade.TcpClient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ruioliveiras
 */
public class Challenge {

    /**
     * The Challenge name
     */
    private String name;
    /**
     * The date to start the Challenge.
     */
    private LocalDate date;
    /**
     * The time (in hours) to start the Challenge.
     */
    private LocalTime time;

    /**
     * The current users that subscribe this challenge.
     */
    private final Set<User> subscribers;
    /**
     * Map from user.nick to his score.
     */
    private final Map<String, Integer> scores;
    
    private final Set<TcpClient> servers;

    /**
     * Set of the questions generated for the challenge
     */
    private final List<Question> questions;

    public Challenge() {
        subscribers = new HashSet<>();
        scores = new HashMap<>();
        questions = new ArrayList<>();
        servers = new HashSet<>();
    }

    /**
     * Main constructor
     *
     * @param name
     * @param date
     * @param time
     */
    public Challenge(String name, LocalDate date, LocalTime time) {
        this();
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDateTime getDateTime() {
        return date.atTime(time);
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void addSubscribers(User u) {
        subscribers.add(u);
        scores.put(u.getNick(), 0);
    }

    public int getScore(String userNick) {
        return scores.get(userNick);
    }

    public void answer(String userNick, boolean isCorrect) {
        if (isCorrect) {
            scores.put(userNick, scores.get(userNick) + 2);
        } else {
            scores.put(userNick, scores.get(userNick) - 1);
        }
    }

    public boolean hasQuestion(Question q) {
        return questions.contains(q);
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }
    
    
    public void addServer(TcpClient u) {
        servers.add(u);
    }
    
    public Set<TcpClient> getServers(){
        return servers;
    }
}
