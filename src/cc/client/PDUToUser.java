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
        
        while(receive.hasParameter(PDUType.INFO_NAME)){
            name = (String)receive.popParameter(PDUType.INFO_NAME);
            score = (int) receive.popParameter(PDUType.INFO_SCORE);
            
            System.out.println("Servidor: \nNome: "+name+"\nScore: "+score);
        }
    }

    void processEnd(PDU receive) {
        int score = 0;
        
        if(receive.hasParameter(PDUType.INFO_SCORE))
            score = (int)receive.popParameter(PDUType.INFO_SCORE);
        
        System.out.println("Servidor: \nScore: "+score);
    }

    void processChallenges(PDU receive) {
        String desafio = null; LocalDate ld= null; LocalTime lt = null; 
        
        while(receive.hasParameter(PDUType.INFO_CHALLE)){
            desafio = (String)receive.popParameter(PDUType.INFO_CHALLE);
            ld = (LocalDate)receive.popParameter(PDUType.INFO_DATE);
            lt = (LocalTime)receive.popParameter(PDUType.INFO_HOUR);
            System.out.println("Servidor: \nDesafio: "+desafio+"\nData: "+ld.toString()+"\nHora: "+lt.toString());   
        }
    }

/*    void processAnswer(PDU receive) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/
    void processRankings(PDU receive) {
        String name = null; String nick = null; int score = 0; 
        
        while(receive.hasParameter(PDUType.INFO_NAME)){
            name = (String)receive.popParameter(PDUType.INFO_NAME);
            nick = (String)receive.popParameter(PDUType.INFO_NICK);
            score = (int)receive.popParameter(PDUType.INFO_SCORE);
            System.out.println("Servidor: \nNome: "+name+"\nAlcunha: "+nick+"\nPontuação: "+score);   
        }
    }
}
