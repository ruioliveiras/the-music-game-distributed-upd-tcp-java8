package cc.server.udpServer;

import cc.model.Challenge;
import cc.model.Question;
import cc.model.User;
import cc.pdu.PDU;
import cc.pdu.PDUType;
import cc.server.tcpServer.ServerState;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author paulo
 */
public class UDPChallengeProvider {
    
    
    
    
    public PDU makeChallenge(String ip, ServerState state, PDU pdu) {
        PDU answer = new PDU(PDUType.REPLY);
        
        String challengeName = (String) pdu.popParameter(PDUType.MAKE_CHALLENGE_CHALLENGE);
        LocalDate date = (LocalDate) pdu.popParameter(PDUType.MAKE_CHALLENGE_DATE);
        LocalTime time = (LocalTime) pdu.popParameter(PDUType.MAKE_CHALLENGE_HOUR);
        Challenge challenge = new Challenge(challengeName, date, time);
        
        challenge.addSubscribers(state.getSession(ip));

        state.addChallenge(challengeName, challenge);
        state.addOwner(challengeName, "localhost");
        
        answer.addParameter(PDUType.REPLY_CHALLE, challengeName);
        answer.addParameter(PDUType.REPLY_DATE, date);
        answer.addParameter(PDUType.REPLY_HOUR, time);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1024);
        executor.schedule(() -> {
            if (challenge.getSubscribers().size() > 1) {
                startChallenge(challenge, state);
            } else {
                answer.addParameter(PDUType.REPLY_ERRO, "Numero insuficiente de jogadores para iniciar desafio");
            }

        }, LocalDateTime.now().until(date.atTime(time), ChronoUnit.NANOS), TimeUnit.NANOSECONDS);

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

    public PDU answer(String ip, ServerState state, PDU pdu) {
        PDU answer = new PDU(PDUType.REPLY);
        String nickname = state.getSession(ip).getNick();

        int choice = (Integer) pdu.popParameter(PDUType.ANSWER_NQUESTION);
        String challengeName = (String) pdu.popParameter(PDUType.ANSWER_CHALLENGE);
        int questionId = (Integer) pdu.popParameter(PDUType.ANSWER_NQUESTION);
        
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

    
    
    
//    <<<<<<< HEAD
//    private void startChallenge(Challenge challenge) {
//        try {
//
//            int i, nQuestion;
//            int sizeQ = state.getQuestions().size();
//            Random r = new Random();
//
//            /**
//             * generate all the questions for this challenge
//             */
//            for (i = 1; i <= 5; i++) {
//                Question q;
//                do {
//                    q = state.getQuestion(r.nextInt(sizeQ - 1) + 1);
//                    // avoid repetead questions
//                } while (challenge.hasQuestion(q));
//                challenge.addQuestion(q);
//            }
//
//            for (i = 1; i <= 5; i++) {
//                PDU ans = makeQuestion(challenge.getName(), i);
//=======
    
    
    
    public void startChallenge(Challenge challenge, ServerState state) {

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
            PDU ans = makeQuestion(challenge.getName(), i, state);
            
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

    public PDU makeQuestion(String challengeName, int number, ServerState state) {
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
