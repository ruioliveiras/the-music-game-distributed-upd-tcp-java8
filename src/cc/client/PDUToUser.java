package cc.client;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author paulo
 */
public class PDUToUser {

    
    public void processOk() {
        System.out.println("Servidor: OK");        
    }

    void processLogin(PDU receive) {
        String name = null; int score = 0;
        
        while(receive.hasParameter(PDUType.REPLY_NAME)){
            name = (String)receive.popParameter(PDUType.REPLY_NAME);
            score = (Short) receive.popParameter(PDUType.REPLY_SCORE);
            
            System.out.println("Servidor: \nNome: "+name+"\nScore: "+score);
        }
        
    }

    void processEnd(PDU receive) {
        int score = 0;
        
        if(receive.hasParameter(PDUType.REPLY_SCORE))
            score = (Short) receive.popParameter(PDUType.REPLY_SCORE);
        
        System.out.println("Servidor: \nScore: "+score);
        
    }

    void processChallenges(PDU receive) {
        String desafio = null; LocalDate ld= null; LocalTime lt = null; 
        
        while(receive.hasParameter(PDUType.REPLY_CHALLE)){
            desafio = (String)receive.popParameter(PDUType.REPLY_CHALLE);
            ld = (LocalDate)receive.popParameter(PDUType.REPLY_DATE);
            lt = (LocalTime)receive.popParameter(PDUType.REPLY_HOUR);
            System.out.println("Servidor: \nDesafio: "+desafio+"\nData: "+ld.toString()+"\nHora: "+lt.toString());   
        }
    }

/*    void processAnswer(PDU receive) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/
    void processRankings(PDU receive) {
        String name = null; String nick = null; int score = 0; 
        
        while(receive.hasParameter(PDUType.REPLY_NAME)){
            name = (String)receive.popParameter(PDUType.REPLY_NAME);
            nick = (String)receive.popParameter(PDUType.REPLY_NICK);
            score = (int)receive.popParameter(PDUType.REPLY_SCORE);
            System.out.println("Servidor: \nNome: "+name+"\nAlcunha: "+nick+"\nPontuação: "+score);   
        }
    }
}
