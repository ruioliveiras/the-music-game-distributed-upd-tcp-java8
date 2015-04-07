/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import cc.server.facade.ServerToServerClient;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author ruioliveiras
 */
public class ServerState {
    public static Map<String, ServerToServerClient> neighbors;
    
    //this will have every data that we need  

    public boolean hasNeighbors(String who) {
        return neighbors.containsKey(who);
    }
    
    public void addNeighbors(String who, String ip, int port){
        neighbors.put(who,new ServerToServerClient(ip, port));
    }
    
    public ServerToServerClient getNeighbors(String who){
        return neighbors.get(who);
    }
    
    public Collection<ServerToServerClient> getNeighbors(){
        return neighbors.values();
    }
    
    public ServerToServerClient get(String ip){
        return neighbors.get(ip);
    }
    
}
