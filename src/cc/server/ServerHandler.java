/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruioliveiras
 */
public class ServerHandler implements Runnable {
           
    
    private final ServerCommunication comm;
    private final ServerState state;
    private final ServerFacade facade;
    
    public ServerHandler(ServerState state, ServerFacade facade, Socket socket) throws IOException {
        this.state = state;
        this.facade = facade;
        comm = new ServerCommunication(socket);
    }
    //contrutor disto vai receber a porta a atuar e dados inciiais
    
    
    @Override
    public void run() {
        try {
            comm.init();
            
            ServerPDU pdu;
            
            while((pdu = comm.readNext()) != null){
                if(pdu.getVersion() == 0){
                    foward01(pdu);
                } else {
                    // somefucking unsoported error
                }
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void foward01(ServerPDU p){
        if (p.getType().id() != ServerPDU.TypePDU.INFO.id()){
            // error this is just INFOS 
        }
        
        
        //- Um registo dum novo desafio;
        if (p.hasArg(ServerPDU.TypePDU.INFO_CHALLE)
                && p.hasArg(ServerPDU.TypePDU.INFO_DATE)
                && p.hasArg(ServerPDU.TypePDU.INFO_HOUR)
                && p.hasArg(ServerPDU.TypePDU.INFO_NAME)
                && p.hasArg(ServerPDU.TypePDU.INFO_NICK)
        ) {
            facade.registerChallenge();
        } else
        //- Um registo dum novo servidor no sistema;
        if(p.hasArg(ServerPDU.TypePDU.INFO_IPSERVER)
                && p.hasArg(ServerPDU.TypePDU.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        } else
        //- A lista dos desafios disponíveis localmente (criados por utilizadores locais);
//        if(p.hasArg(ServerPDU.TypePDU.INFO_IPSERVER)
//                && p.hasArg(ServerPDU.TypePDU.INFO_PORT)
//        ){
//            facade.registerServer();
//            //if origin is from know server: do nothing
//            //if not: resend it to my neightbors;
//            facade.registerServer();
//            
//        } else 
        //- Um registo de aceitação dum desafio;
        if(p.hasArg(ServerPDU.TypePDU.INFO_IPSERVER)
                && p.hasArg(ServerPDU.TypePDU.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        } else
        //- Os resultados dum desafio;
                if(p.hasArg(ServerPDU.TypePDU.INFO_IPSERVER)
                && p.hasArg(ServerPDU.TypePDU.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        } else
        //- O ranking dos utilizadores locais.
        if(p.hasArg(ServerPDU.TypePDU.INFO_IPSERVER)
                && p.hasArg(ServerPDU.TypePDU.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        }

    }


    }



















































