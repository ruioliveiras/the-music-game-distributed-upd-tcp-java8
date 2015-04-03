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
    private final ServerToServerFacade facade;
    
    public ServerHandler(ServerState state, ServerToServer facade, Socket socket) throws IOException {
        this.state = state;
        this.facade = facade;
        comm = new ServerCommunication(socket);
    }
    //contrutor disto vai receber a porta a atuar e dados inciiais
    
    
    @Override
    public void run() {
        try {
            comm.init();
            
            PDU pdu;
            
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
    
    protected void foward01(PDU p){
        if (p.getType().getId() != PDUType.INFO.getId()){
            // error this is just INFOS 
        }
        
        
        //- Um registo dum novo desafio;
        if (p.hasParameter(PDUType.INFO_CHALLE)
                && p.hasParameter(PDUType.INFO_DATE)
                && p.hasParameter(PDUType.INFO_HOUR)
                && p.hasParameter(PDUType.INFO_NAME)
                && p.hasParameter(PDUType.INFO_NICK)
        ) {
            facade.registerChallenge();
        } else
        //- Um registo dum novo servidor no sistema;
        if(p.hasParameter(PDUType.INFO_IPSERVER)
                && p.hasParameter(PDUType.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        } else
        //- A lista dos desafios disponíveis localmente (criados por utilizadores locais);
//        if(p.hasParameter(PDU.TypePDU.INFO_IPSERVER)
//                && p.hasParameter(PDU.TypePDU.INFO_PORT)
//        ){
//            facade.registerServer();
//            //if origin is from know server: do nothing
//            //if not: resend it to my neightbors;
//            facade.registerServer();
//            
//        } else 
        //- Um registo de aceitação dum desafio;
        if(p.hasParameter(PDUType.INFO_IPSERVER)
                && p.hasParameter(PDUType.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        } else
        //- Os resultados dum desafio;
                if(p.hasParameter(PDUType.INFO_IPSERVER)
                && p.hasParameter(PDUType.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        } else
        //- O ranking dos utilizadores locais.
        if(p.hasParameter(PDUType.INFO_IPSERVER)
                && p.hasParameter(PDUType.INFO_PORT)
        ){
            facade.registerServer();
            //if origin is from know server: do nothing
            //if not: resend it to my neightbors;
            facade.registerServer();
            
        }

    }


    }



















































