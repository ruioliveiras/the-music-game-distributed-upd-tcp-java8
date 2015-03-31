/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

/**
 *
 * @author ruioliveiras
 */
public interface ServerInterface  {
    
    /**
     * This function will be called in the init of the server to register than 
     * in a branch of server
     * @return 
     */
    public boolean registerMySelfServer();
    
    /**
     * This function is to ask the server to registry a new server,  
     * @return 
     */
    public boolean registerServer();
    
    /**
     * Register a Challenge to a server
     * @return 
     */
    public boolean regirsterChallenge();
    
    /**
     * Accept challenge, and ask to receive the challenge info.
     * @return 
     */
    public boolean acceptChallenge();
    
    /**
     * Get the challengeResult
     * @return 
     */
    public String[] challengeResult();
    
    /**
     * Get the userRating
     * @return 
     */
    public String[] userResult();
    
    
}
