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
public class ServerHandler {
    //contrutor disto vai receber a porta a atuar e dados inciiais
    
    public void init(){
        //init ServerCommuncation blablabla
    }
    
    public void run(){
        //Start new thread
        //thread com um while serverCommm readNext()...
        //foward
    }
    
    protected void foward(ServerPDU p){
        // the foward for eacth type will parse the p and call the currect facade function, and after send response back if need.
    }
}
