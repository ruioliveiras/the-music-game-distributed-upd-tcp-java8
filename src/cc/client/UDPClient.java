package cc.client;

import cc.model.Question;
import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.udpServer.UDPComunication;
import cc.swinggame.MainInterface;
import cc.thegame.AppController;
import cc.thegame.AppMain;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.util.Pair;

/**
 *
 * @author paulo
 */
public class UDPClient {

    private InetAddress dest_ip;
    private int dest_port;
    //private UDPClientCommunication udp_com;
    private UDPComunication udp_com;
    private PDUToUser ptu;
    final AppController iGraf = new AppController();

    public UDPClient(String sourceIp, int sourcePort, String dest, int port) {
        try {
            dest_ip = InetAddress.getByName(dest);
            dest_port = port;
            udp_com = new UDPComunication(sourcePort, InetAddress.getByName(sourceIp), port, InetAddress.getByName(dest));
            ptu = new PDUToUser(true, "C"+sourceIp+"]");
        } catch (UnknownHostException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public UDPClient(String dest, int port) {
        try {
            dest_ip = InetAddress.getByName(dest);
            dest_port = port;
            udp_com = new UDPComunication(0, null, port, InetAddress.getByName(dest));
            ptu = new PDUToUser();
        } catch (UnknownHostException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public UDPComunication getUDPClientCom() {
        return udp_com;
    }

    public PDUToUser getPDUToUser() {
        return ptu;
    }

    public void makeDatagramHello() {
        PDU send = new PDU(PDUType.HELLO);
        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();
//        if(receive.getLabel() == current_label) {
//            
//            current_label++;
//        }        
    }

    public void makeDatagramRegister(String name, String alcunha, byte[] sec_info) {
        PDU send = new PDU(PDUType.REGISTER);

        send.addParameter(PDUType.REGISTER_NAME, name);
        send.addParameter(PDUType.REGISTER_NICK, alcunha);
        send.addParameter(PDUType.REGISTER_PASS, sec_info);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();
//        if(receive.getLabel() == current_label) {
//            
//            current_label++;
//        }   
    }

    public void makeDatagramLogin(String alcunha, byte[] sec_info) {
        PDU send = new PDU(PDUType.LOGIN);

        send.addParameter(PDUType.LOGIN_NICK, alcunha);
        send.addParameter(PDUType.LOGIN_PASS, sec_info);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processLogin(receive);
    }

    public void makeDatagramLogout() {
        PDU send = new PDU(PDUType.LOGOUT);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();

    }

    public void makeDatagramQuit() {
        PDU send = new PDU(PDUType.QUIT);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processOk();
    }

    public void makeDatagramEnd() {
        PDU send = new PDU(PDUType.END);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processEnd(receive);
    }

    public void makeDatagramListChallenges() {
        PDU send = new PDU(PDUType.LIST_CHALLENGES);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processChallenges(receive);
    }

    public void makeDatagramMakeChallenge(String desafio, LocalDate data, LocalTime hora) {
        PDU send = new PDU(PDUType.MAKE_CHALLENGE);

        send.addParameter(PDUType.MAKE_CHALLENGE_CHALLENGE, desafio);
        send.addParameter(PDUType.MAKE_CHALLENGE_DATE, data);
        send.addParameter(PDUType.MAKE_CHALLENGE_HOUR, hora);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processChallenges(receive);
    }

    public void makeDatagramAcceptChallenge(String desafio) {
        /*PDU send = new PDU(PDUType.ACCEPT_CHALLENGE);

        send.addParameter(PDUType.ACCEPT_CHALLENGE_CHALLENGE, desafio);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();*/
        //determinar se receive é uma questão.
        
        ptu.processOk();

        doChallenge(desafio);
              
        //@todo: ficar a espera de resposta do servidor com proxima questao ou erro
        //esta funcionalidade talvez melhor implementar no desafio...
    }

    public void makeDatagramDeleteChallenge(String desafio) {
        PDU send = new PDU(PDUType.DELETE_CHALLENGE);

        send.addParameter(PDUType.DELETE_CHALLENGE_CHALLENGE, desafio);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        ptu.processChallenges(receive);
    }

    //falta implementar a lógica desta
    //
    //
    //
    //
    public void makeDatagramAnswer(Byte escolha, String desafio, Byte questao) {
        PDU send = new PDU(PDUType.ANSWER);

        send.addParameter(PDUType.ANSWER_CHOOSE, escolha);
        send.addParameter(PDUType.ANSWER_CHALLENGE, desafio);
        send.addParameter(PDUType.ANSWER_NQUESTION, questao);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        receive.popParameter(PDUType.REPLY_CORRECT);
        //ptu.processAnswer(receive);
    }

    //
    public byte[] makeDatagramRetransmit(String desafio, byte questao, byte bloco) {
        PDU send = new PDU(PDUType.RETRANSMIT);

        send.addParameter(PDUType.RETRANSMIT_CHALLENGE, desafio);
        send.addParameter(PDUType.RETRANSMIT_NQUESTION, questao);
        send.addParameter(PDUType.RETRANSMIT_NBLOCK, bloco);

        udp_com.sendPDU(send);
        PDU response = udp_com.nextPDU();

        if (response.hasParameter(PDUType.REPLY_BLOCK)) {
            return (byte[]) response.popParameter(PDUType.REPLY_BLOCK);
        } else {
            return null;
        }
    }

    public void makeDatagramList_Ranking(){
        PDU send = new PDU(PDUType.LIST_RANKING);

        udp_com.sendPDU(send);

        PDU receive = udp_com.nextPDU();
        
        ptu.processRankings(receive);
    }

    public void closeCSocket() {
        udp_com.close();
    }

    public void doChallenge(String desafio){
        
        int currentPoints = 0, correctAnswer = 0, correctAnswer_index=0, pointsWon = 0;
        String args[] = null;
        int pergunta = 0, answerGiven = 0;
   
        Question actualQ = null;
        
        String s1[] = {"resposta p11", "resposta p12", "resposta p13"};
        String s2[] = {"resposta p21", "resposta p22", "resposta p23"};
        String s3[] = {"resposta p31", "resposta p32", "resposta p33"};
        String s4[] = {"resposta p41", "resposta p42", "resposta p43"};
        String s5[] = {"resposta p51", "resposta p52", "resposta p53"};
        String s6[] = {"resposta p61", "resposta p62", "resposta p63"};
        String s7[] = {"resposta p71", "resposta p72", "resposta p73"};
        String s8[] = {"resposta p81", "resposta p82", "resposta p83"};
        String s9[] = {"resposta p91", "resposta p92", "resposta p93"};
        String s10[] = {"resposta p101", "resposta p102", "resposta p103"};
        
        Question q1 = new Question("Pergunta 1", s1, 1, "", "");
        Question q2 = new Question("Pergunta 2", s2, 1, "", "");
        Question q3 = new Question("Pergunta 3", s3, 1, "", "");
        Question q4 = new Question("Pergunta 4", s4, 1, "", "");
        Question q5 = new Question("Pergunta 5", s5, 1, "", "");
        Question q6 = new Question("Pergunta 6", s6, 1, "", "");
        Question q7 = new Question("Pergunta 7", s7, 1, "", "");
        Question q8 = new Question("Pergunta 8", s8, 1, "", "");
        Question q9 = new Question("Pergunta 9", s9, 1, "", "");
        Question q10 = new Question("Pergunta 10", s10, 1, "", "");
        
        List<Question> l = new ArrayList<>();
        l.add(q1); l.add(q2); l.add(q3); l.add(q4); l.add(q5); l.add(q6); l.add(q7); l.add(q8); l.add(q9); l.add(q10); 
        
        MainInterface mInt = new MainInterface(this);
        mInt.setVisible(true);
             
        
        /*for(pergunta=0; pergunta<10; pergunta++){
            
            actualQ = getNextQuestion();
            answerGiven = mInt.createQuestion(actualQ);
            correctAnswer_index = actualQ.getCorrect();
            
            
            //falta enviar o datagrama com os valores passados como bytes
            //valor escolhar é variavel answerGiven, valor questao é a variavel pergunta
            //makeDatagramAnswer(Byte escolha, desafio, Byte questao);
            
            PDU receive = udp_com.nextPDU();

            correctAnswer = (byte) receive.popParameter(PDUType.REPLY_CORRECT);
            pointsWon = (byte) receive.popParameter(PDUType.REPLY_POINTS);
            
            currentPoints += pointsWon;
            
            mInt.showResult(answerGiven, correctAnswer_index);
            mInt.updateScore(currentPoints);
            
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
            //For testing
            for(Question q : l){
                answerGiven = mInt.createQuestion(q);
                correctAnswer_index = q.getCorrect();
                System.out.println("Resposta dada: " + answerGiven);
                mInt.showResult(answerGiven, correctAnswer_index);
                currentPoints++;
                mInt.updateScore(currentPoints);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        //ao inicio do metodo, iniciar a interface
        //meter a esperar pelo proximo pdu... se calhar mete-se o controlo do erro no metodo de receber a questao
        //enquanto nao tiver feito as 10 perguntas, fazer sempre o mesmo ciclo
        //depois do ciclo, receber o pdu com os pontos que amealhou
        
        
    }
    
    
    
    public Question getNextQuestion() {
        Question question;
        int numberBlocks = 0;

        udp_com.setLabelMode(false);
        PDU receive = udp_com.nextPDU();
        udp_com.setLabelMode(true);

        String questionText = (String) receive.popParameter(PDUType.REPLY_QUESTION);
        String challengeName = (String) receive.popParameter(PDUType.REPLY_CHALLE);
        int questionId = (byte) receive.popParameter(PDUType.REPLY_NUM_QUESTION);
        int correct = (byte) receive.popParameter(PDUType.REPLY_CORRECT);
        byte[] img = (byte[]) receive.popParameter(PDUType.REPLY_IMG);
        String[] answers = (String[]) receive.getAllParameter(PDUType.REPLY_ANSWER)
                .toArray(new String[receive.getAllParameter(PDUType.REPLY_ANSWER).size()]);

        // MUSIC WOWOW
        TreeMap<Integer, byte[]> musicBytes = new TreeMap<>();

        while (receive.hasParameter(PDUType.REPLY_BLOCK)) {
            if (!receive.hasParameter(PDUType.REPLY_NUM_BLOCK)) {
                //error
            }
            int blockNumber = (byte) receive.popParameter(PDUType.REPLY_NUM_BLOCK);
            byte[] blockMusic = (byte[]) receive.popParameter(PDUType.REPLY_BLOCK);
            if (numberBlocks < blockNumber) {
                numberBlocks = blockNumber;
            }

            musicBytes.put(blockNumber, blockMusic);
        }

        IntStream.rangeClosed(0, numberBlocks)
                // not in the list
                .filter((a) -> !musicBytes.containsKey(a))
                // prepare the id and get de bytes
                .mapToObj((a) -> new Pair<>(a, makeDatagramRetransmit(challengeName, (byte) questionId, (byte) a)))
                .filter((pair) -> pair.getValue() != null)
                // add
                .forEach((t) -> musicBytes.put(t.getKey(), t.getValue()));

//        ByteBuffer buffer = ByteBuffer.allocate((numberBlocks + 1) * musicBytes.firstEntry().getValue().length);
//        musicBytes.entrySet().stream().sequential()
//                .forEach((a) -> buffer.put(a.getValue()));

        List<byte[]> buffer = musicBytes.entrySet().stream().sequential()
                .map(pair -> pair.getValue())
                .collect(Collectors.toList());
// percorrer todos e se for maior que o anterior entao faz pede retrasmit do bloco
//@todo receber os blocos da musica
        question = new Question(questionText, answers, correct, img, buffer);

        return question;
    }

}
