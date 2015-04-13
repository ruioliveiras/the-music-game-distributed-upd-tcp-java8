package cc.client;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 
 * To fragment a pdu, assuming the existence of 'int currentLabel' and 'pdu lastPDU.'
 *  
 * on readDatagram do something like this:
 * msg_received.initHeaderFromBytes(headerBuffer);
 * if (msg_received.getLabel == currentLabel){
 *   lastPDU.initHeaderFromBytes(headerBuffer);
 *   msg_received = lastPDU;
 * } 
 * msg_received.initParametersFromBytes(bodyBuffer);
 * 
 * @author paulo
 */
public class UDPClientCommunication {

    //receber PDU -> ler -> construir objeto
    //construir PDU -> enviar
    
    public PDU readDatagram(byte[] pData){
        PDU msg_received = new PDU();
        int i, j;
                
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[1024];
        
        for(i=0;i<8;i++) headerBuffer[i] = pData[i];
        for(j=0;i<pData.length;i++,j++) bodyBuffer[j] = pData[i];
        
        msg_received.initHeaderFromBytes(headerBuffer);
        msg_received.initParametersFromBytes(bodyBuffer);
        
        return msg_received;
    }
    
    
    public PDU makeDatagramHello(){
        PDU send = new PDU(PDUType.HELLO); 
        
        return send;
    }
        
    public PDU makeDatagramRegister(String name, String alcunha, byte[] sec_info){
        PDU send = new PDU(PDUType.REGISTER);
        
        send.addParameter(PDUType.REGISTER_NAME, name);
        send.addParameter(PDUType.REGISTER_NICK, alcunha);
        send.addParameter(PDUType.REGISTER_PASS, sec_info);
    
        return send;
    }
    
    public PDU makeDatagramLogin(String alcunha, byte[] sec_info){
        PDU send = new PDU(PDUType.LOGIN);
        
        send.addParameter(PDUType.LOGIN_NICK, alcunha);
        send.addParameter(PDUType.LOGIN_PASS, sec_info);
    
        return send;
    }
    
    public PDU makeDatagramLogout(){
        PDU send = new PDU(PDUType.LOGOUT);
    
        return send;
    }
    
    public PDU makeDatagramQuit(){
        PDU send = new PDU(PDUType.QUIT);
    
        return send;
    }
    
    public PDU makeDatagramEnd(){
        PDU send = new PDU(PDUType.END);
        
        return send;
    }
    
    public PDU makeDatagramList_Challenges(){
        PDU send = new PDU(PDUType.LIST_CHALLENGES);
        
        return send;
    }
    
    public PDU makeDatagramMake_Challenge(String desafio, LocalDate data, LocalTime hora){
        PDU send = new PDU(PDUType.MAKE_CHALLENGE);
        
        send.addParameter(PDUType.MAKE_CHALLENGE_CHALLENGE, desafio);
        send.addParameter(PDUType.MAKE_CHALLENGE_DATE, data);
        send.addParameter(PDUType.MAKE_CHALLENGE_HOUR, hora);
    
        return send;
    }
    
    public PDU makeDatagramAccept_Challenge(String desafio){
        PDU send = new PDU(PDUType.ACCEPT_CHALLENGE);
    
        send.addParameter(PDUType.ACCEPT_CHALLENGE_CHALLENGE, send);
        
        return send;
    }
    
    public PDU makeDatagramDelete_Challenge(String desafio){
        PDU send = new PDU(PDUType.DELETE_CHALLENGE);
        
        send.addParameter(PDUType.DELETE_CHALLENGE_CHALLENGE, desafio);
    
        return send;
    }
    
    public PDU makeDatagramAnswer(Byte escolha, String desafio, Byte questao){
        PDU send = new PDU(PDUType.ANSWER);
        
        send.addParameter(PDUType.ANSWER_CHOOSE, escolha);
        send.addParameter(PDUType.ANSWER_CHALLENGE, desafio);
        send.addParameter(PDUType.ANSWER_NQUESTION, questao);
    
        return send;
    }
    
    public PDU makeDatagramRetransmit(String desafio, Byte questao, Byte bloco){
        PDU send = new PDU(PDUType.RETRANSMIT);
        
        send.addParameter(PDUType.RETRANSMIT_CHALLENGE, desafio);
        send.addParameter(PDUType.RETRANSMIT_NQUESTION, questao);
        send.addParameter(PDUType.RETRANSMIT_NBLOCK, bloco);
        
        return send;
    }
    
    public PDU makeDatagramList_Ranking(){
        PDU send = new PDU(PDUType.LIST_RANKING);
    
        return send;
    }
   
    
    
    
}
