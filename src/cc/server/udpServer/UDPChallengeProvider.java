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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class UDPChallengeProvider {
    public static final int CHALLENGE_NUMQUESTION = 3;
    
    public UDPComunication socket;
    public ServerState state;

    public UDPChallengeProvider(UDPComunication socket, ServerState state) {
        this.socket = socket;
        this.state = state;
    }

    public void startChallenge(Challenge challenge) {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1024);
        executor.schedule(() -> {
            try {
                if (challenge.getSubscribers().size() > 1) {
                    this.startChallengeNow(challenge);
                } else {
                    // error
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println(ex.toString());
            }
        //}, LocalDateTime.now().until(challenge.getDateTime(), ChronoUnit.NANOS), TimeUnit.NANOSECONDS);
            }, 2000, TimeUnit.MILLISECONDS);

    }

    public void startChallengeNow(Challenge challenge) throws InterruptedException {
        Question q;
        int i, nQuestion;
        int sizeQ = state.getQuestions().size();
        Random r = new Random();

        /**
         * generate all the questions for this challenge
         */
        for (i = 1; i <= CHALLENGE_NUMQUESTION; i++) {
            do {
                q = state.getQuestion(r.nextInt(sizeQ - 1) + 1);
                // avoid repetead questions
            } while (challenge.hasQuestion(q));
            challenge.addQuestion(q);
        }

        for (i = 1; i <= CHALLENGE_NUMQUESTION; i++) {
            q = challenge.getQuestion(i);
            PDU ans = makeQuestion(challenge.getName(), i, state);
            challenge.getSubscribers().stream()
                    .filter(user -> user.getIP() != null)
                    .forEach((user) -> {
                        socket.sendPDU(ans, user.getIP(), user.getPort());
                    });

            //music: 
            q.loadMusic();

            i = 0;
            boolean hasNext = true;
            while (hasNext) {
                PDU musicPDU = new PDU(PDUType.REPLY);
                //@todo: ruioliveiras continuar aqui, esta merda precisa de continue se nao for o ultimo
                hasNext = UDPClientHandler.createMusicBlockPDU(musicPDU, q, i++);
                if (hasNext) {
                    musicPDU.addParameter(PDUType.CONTINUE, (byte) 0);
                }

                challenge.getSubscribers().stream()
                        .filter(user -> user.getIP() != null)
                        .forEach((user) -> {
                            socket.sendPDU(musicPDU, user.getIP(), user.getPort());
                        });
            };
            Thread.sleep(1000);
        }

    }

    public PDU makeQuestion(String challengeName, int number, ServerState state) {
        PDU questionPDU = new PDU(PDUType.REPLY);
        Question question = state.getChallenge(challengeName).getQuestion(number - 1);
        String questionText = question.getQuestion();
        question.loadImage();
        question.loadMusic();
        
        String[] answers = question.getAnwser();
        int correct = question.getCorrect(), i;
        byte[] img = question.getImageArray();

        questionPDU.addParameter(PDUType.REPLY_CHALLE, challengeName);
        questionPDU.addParameter(PDUType.REPLY_NUM_QUESTION, (byte) number);
        questionPDU.addParameter(PDUType.REPLY_QUESTION, questionText);
        questionPDU.addParameter(PDUType.REPLY_CORRECT, (byte) question.getCorrect());
        for (i = 0; i < 3; i++) {
            questionPDU.addParameter(PDUType.REPLY_NUM_ANSWER,(byte) (i + 1));
            questionPDU.addParameter(PDUType.REPLY_ANSWER, answers[i]);
        }
        //@todo fazer o loadImage na classe Question
        questionPDU.addParameter(PDUType.REPLY_IMG, img);
        questionPDU.addParameter(PDUType.CONTINUE, (byte) 0);

        return questionPDU;

    }
}
