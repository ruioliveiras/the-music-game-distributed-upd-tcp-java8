/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.udpServer;

import cc.model.Challenge;
import cc.model.Question;
import cc.model.User;
import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.tcpServer.ServerState;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class UDPClientHandler {

    private final ServerState state;
    private final DatagramSocket socket;

    public UDPClientHandler(ServerState serverState, DatagramSocket socket) {
        this.state = serverState;
        this.socket = socket;
    }

    public PDU decodePacket(PDU pdu, String ip) {

        PDU answer = new PDU(PDUType.REPLY);

        switch (pdu.getType()) {
            case HELLO:
                answer.addParameter(PDUType.REPLY_OK, 0);
                break;
            case REGISTER:
                String name = (String) pdu.popParameter(PDUType.REGISTER_NAME);
                String nick = (String) pdu.popParameter(PDUType.REGISTER_NICK);
                byte[] secInfo = (byte[]) pdu.popParameter(PDUType.REGISTER_PASS);
                answer = register(name, nick, secInfo);
                break;
            case LOGIN:
                nick = (String) pdu.popParameter(PDUType.LOGIN_NICK);
                secInfo = (byte[]) pdu.popParameter(PDUType.LOGIN_PASS);
                answer = login(ip, nick, secInfo);
                break;
            case LOGOUT:
                answer = logout(ip);
                break;
            case QUIT:
                answer.addParameter(PDUType.REPLY_OK, 0);
                break;
            case END:
                answer = end(ip);
                break;
            case LIST_CHALLENGES:
                answer = listChallenges();
                break;
            case MAKE_CHALLENGE:
                String challengeName = (String) pdu.popParameter(PDUType.MAKE_CHALLENGE_CHALLENGE);
                LocalDate date = (LocalDate) pdu.popParameter(PDUType.MAKE_CHALLENGE_DATE);
                LocalTime time = (LocalTime) pdu.popParameter(PDUType.MAKE_CHALLENGE_HOUR);
                answer = makeChallenge(ip, challengeName, date, time);
                break;
            case ACCEPT_CHALLENGE:
                challengeName = (String) pdu.popParameter(PDUType.ACCEPT_CHALLENGE_CHALLENGE);
                answer = acceptChallenge(ip, challengeName);
                break;
            case DELETE_CHALLENGE:
                challengeName = (String) pdu.popParameter(PDUType.DELETE_CHALLENGE_CHALLENGE);
                answer = deleteChallenge(challengeName);
                break;
            case ANSWER:
                int choice = (Integer) pdu.popParameter(PDUType.ANSWER_NQUESTION);
                challengeName = (String) pdu.popParameter(PDUType.ANSWER_CHALLENGE);
                int questionId = (Integer) pdu.popParameter(PDUType.ANSWER_NQUESTION);
                answer = answer(ip, challengeName, choice, questionId);
                break;
            case RETRANSMIT:
                challengeName = (String) pdu.popParameter(PDUType.RETRANSMIT_CHALLENGE);
                questionId = (Integer) pdu.popParameter(PDUType.RETRANSMIT_NQUESTION);
                int nblock = (Integer) pdu.popParameter(PDUType.RETRANSMIT_NBLOCK);
                answer = retransmit(challengeName, questionId, nblock);
                break;
            case LIST_RANKING:
                answer = listRanking();
                break;
        }

        return answer;
    }

    private PDU register(String name, String nick, byte[] secInfo) {
        PDU answer = new PDU(PDUType.REPLY);

        User user = new User(name, secInfo, nick);
        if (!state.hasLocalUser(nick)) {
            state.addLocalUser(nick, user);
            answer.addParameter(PDUType.REPLY_OK, 0);
        } else {
            answer.addParameter(PDUType.REPLY_ERRO, "Utilizador já existe");
        }
        return answer;
    }

    private PDU login(String ip, String nick, byte[] secInfo) {
        User user;
        String name;
        PDU answer = new PDU(PDUType.REPLY);

        if (state.hasLocalUser(nick)) {
            boolean check = Arrays.equals(state.getLocalUser(nick).getPass(), secInfo);
            if (check) {
                user = state.getLocalUser(nick);
                user.setIP(ip);
                state.addSession(ip, user);
                name = state.getLocalUser(nick).getName();
                //ver se é isto que é para enviar no reply
                int score = state.getLocalUser(nick).getRating();
                answer.addParameter(PDUType.REPLY_NAME, name);
                answer.addParameter(PDUType.REPLY_SCORE, (short) score);
            } else {
                answer.addParameter(PDUType.REPLY_ERRO, "Palavra-passe errada");
            }
        } else {
            answer.addParameter(PDUType.REPLY_ERRO, "Utilizador não registado");
        }

        return answer;
    }

    private PDU logout(String ip) {
        PDU answer = new PDU(PDUType.REPLY);

        state.getSession(ip).setIP("");
        state.removeSession(ip);
        answer.addParameter(PDUType.REPLY_OK, 0);

        return answer;
    }

    private PDU end(String ip) {
        PDU answer = new PDU(PDUType.REPLY);
        int score = 0;

        //nao sei como vou buscar o jogo que acabou de terminar
        answer.addParameter(PDUType.REPLY_SCORE, score);

        return answer;
    }

    private PDU listChallenges() {
        PDU answer = new PDU(PDUType.REPLY);
        LocalTime time;
        LocalDate date;
        String challengeName;

        for (Challenge challenge : state.getChallenges()) {
            challengeName = challenge.getName();
            time = challenge.getTime();
            date = challenge.getDate();
            answer.addParameter(PDUType.REPLY_CHALLE, challengeName);
            answer.addParameter(PDUType.REPLY_DATE, date);
            answer.addParameter(PDUType.REPLY_HOUR, time);
        }

        return answer;
    }

    private PDU makeChallenge(String ip, String name, LocalDate date, LocalTime time) {
        PDU answer = new PDU(PDUType.REPLY);
        Challenge challenge = new Challenge(name, date, time);

        challenge.addSubscribers(state.getSession(ip));

        state.addChallenge(name, challenge);
        state.addOwner(name, "localhost");
        answer.addParameter(PDUType.REPLY_CHALLE, name);
        answer.addParameter(PDUType.REPLY_DATE, date);
        answer.addParameter(PDUType.REPLY_HOUR, time);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1024);
        executor.schedule(() -> {
            if (challenge.getSubscribers().size() > 1) {
                startChallenge(challenge);
            } else {
                answer.addParameter(PDUType.REPLY_ERRO, "Numero insuficiente de jogadores para iniciar desafio");
            }

        }, LocalDate.now().until(date.atTime(time), ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);

        //@todo: criar thread que irá acordar quando for date,time
        //       esta thread irá verificar se existe pessoas suficientes, 
        //       caso exista envia a primeira questao.
        //              Após enviar a primeira questao aguarda tempo X por resposta do User
        //              caso  user nao responda resposta errada será assumida
        //              Envia proxima Questao, caso não seja a ultima.
        //       caso não exista dá erro. 
        // criar thread aqui que confirma a existencia de pessoas       
        // criar função sendQuestion(int numero da questao)
        //      caso seja a ultima questao:
        //           responder com o rating
        //      caso não seja a ultima questao:
        //          envia questao, espera X tempo, quem respondeu respondeu, quem nao respondeu marca errada
        //          /!\ sistema de lock para nao deixar responder a answer fora do tempo
        //          chama proxima questao
        // do lado do servidor
        // Quando se envia uma questão terá que se enviar 1 pdu com cenas normais (ver pagina 5 do enunciado)
        // e também tera que se enviar a musica criar função sendFramentedMusic:
        // que irá criar os framentos e enviar somehow...
        // do lado do cliente:
        // quando se fica a espera de uma questão, a seguir irá-se esperar a musica.
        // a seguir a ter estas duas coisas irá por na interface,
        // depois espera-se por a proxima questao, entretanto posse-se enviar a resposta.
        return answer;
    }

    private PDU acceptChallenge(String ip, String challengeName) {
        PDU answer = new PDU(PDUType.REPLY);

        User user = state.getSession(ip);
        //a key das challenges do state é o nome da challenge ou do owner?
        state.getChallenge(challengeName).addSubscribers(user);
        answer.addParameter(PDUType.REPLY_OK, 0);

        return answer;
    }

    private PDU deleteChallenge(String challengeName) {
        PDU answer = new PDU(PDUType.REPLY);
        Challenge challenge = state.getChallenge(challengeName);

        answer.addParameter(PDUType.REPLY_CHALLE, challengeName);
        answer.addParameter(PDUType.REPLY_DATE, challenge.getDate());
        answer.addParameter(PDUType.REPLY_HOUR, challenge.getTime());

        state.getChallenges().remove(challenge);

        return answer;
    }

    private PDU answer(String ip, String challengeName, int choice, int questionId) {
        PDU answer = new PDU(PDUType.REPLY);
        String nickname = state.getSession(ip).getNick();

        answer.addParameter(PDUType.REPLY_CHALLE, challengeName);
        answer.addParameter(PDUType.REPLY_NUM_QUESTION, questionId);

        if (state.getQuestion(questionId).getCorrect() == choice) {
            answer.addParameter(PDUType.REPLY_CORRECT, 1);
            answer.addParameter(PDUType.REPLY_POINTS, 2);
            state.getChallenge(challengeName).answer(nickname, true);
        } else {
            answer.addParameter(PDUType.REPLY_CORRECT, 0);
            answer.addParameter(PDUType.REPLY_POINTS, -1);
            state.getChallenge(challengeName).answer(nickname, false);
        }

        return answer;
    }

    private PDU listRanking() {
        PDU answer = new PDU(PDUType.REPLY);

        for (User user : state.getLocalUsers()) {
            answer.addParameter(PDUType.REPLY_NAME, user.getName());
            answer.addParameter(PDUType.REPLY_NICK, user.getNick());
            answer.addParameter(PDUType.REPLY_SCORE, user.getRating());
        }

        return answer;
    }

    private PDU musicPDU(Question q) {
        PDU aux = new PDU(PDUType.REPLY), p;
        q.loadMusic();
        int i = 0;

        do {
            p = aux;
            aux = musicBlockAux(p, q, i++);
        } while (aux != null);

        return p;
    }

    public PDU retransmit(String challengeName, int questionId, int nblock) {
        Question q = state.getQuestion(questionId);
        q.loadMusic();
        if (q == null) {
            //erro;
        }
        return musicBlockAux(new PDU(PDUType.REPLY), q, nblock);
    }

    private PDU musicBlockAux(PDU pduAux, Question q, int index) {
        int sizeofBlock = 48 * 1024;
        int size = sizeofBlock;
        if ((sizeofBlock * index + sizeofBlock) > q.getMusicArray().length) {
            size = q.getMusicArray().length - (sizeofBlock * index + sizeofBlock);
        }
        if (size < 0) {
            return null;
        }
        byte[] res = ByteBuffer.allocate(size).put(q.getMusicArray(), index * sizeofBlock, size).array();

        pduAux.addParameter(PDUType.REPLY_NUM_BLOCK, (byte) index);
        pduAux.addParameter(PDUType.REPLY_BLOCK, res);
        pduAux.addParameter(PDUType.CONTINUE, (byte) 0);

        return pduAux;
    }

    private void startChallenge(Challenge challenge) {

        int i, nQuestion;
        int sizeQ = state.getQuestions().size();
        Random r = new Random();

        /**
         * generate all the questions for this challenge
         */
        for (i = 1; i <= 10; i++) {
            Question q;
            do {
                q = state.getQuestion(r.nextInt(sizeQ - 1) + 1);
                // avoid repetead questions
            } while (challenge.hasQuestion(q));
            challenge.addQuestion(q);
        }

        for (i = 1; i <= 10; i++) {
            PDU ans = makeQuestion(challenge.getName(), i);
//@todo nesta parte de escolher perguntas, não podem haver repetidas
            
            //@todo fazer merge do ans com o pdu do makeQuestion(nQuestion)

//            SOLUCÇÂO? TALVEZ SIM TALVEZ NAO....
//            byte[] dadosEnviar = ans.toByte();
//            while (dadosEnviar != null) {
//                DatagramPacket send_packet = new DatagramPacket(dadosEnviar, dadosEnviar.length, dest_ip, dest_port);
//                try {
//                    socket.send(send_packet);
//                } catch (IOException ex) {
//                    Logger.getLogger(UDPClientHandler.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
        }

    }

    private PDU makeQuestion(String challengeName, int number) {
        PDU question = new PDU(PDUType.REPLY);
        Question q = state.getChallenge(challengeName).getQuestion(number-1);
        String questionText = q.getQuestion();
        String[] answers = q.getAnwser();
        int correct = q.getCorrect(), i;
        byte[] img = q.getImageArray();

        question.addParameter(PDUType.REPLY_CHALLE, challengeName);
        question.addParameter(PDUType.REPLY_NUM_QUESTION, number);
        question.addParameter(PDUType.REPLY_QUESTION, questionText);
        for (i = 0; i < 3; i++) {
            question.addParameter(PDUType.REPLY_NUM_ANSWER, i + 1);
            question.addParameter(PDUType.REPLY_ANSWER, answers[i]);
        }
        //@todo fazer o loadImage na classe Question
        question.addParameter(PDUType.REPLY_IMG, img);

        return question;

    }

}
