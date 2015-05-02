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
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.util.Pair;
import util.T3;
import util.T2;

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
        for (i = 0; i < CHALLENGE_NUMQUESTION; i++) {
            do {
                q = state.getQuestion(r.nextInt(sizeQ - 1) + 1);
                // avoid repetead questions
            } while (challenge.hasQuestion(q));
            challenge.addQuestion(q);
        }

        for (i = 0; i < CHALLENGE_NUMQUESTION; i++) {
            q = challenge.getQuestion(i);
            q.loadImage();
            q.loadMusic();
            this.sendQuestion(challenge.getName(),i,q.getQuestion(),q.getCorrect(),
                    q.getAnwser(), q.getImageArray() ,q.getMusicArray());
            Thread.sleep(1000);
        }
    }

    public void sendQuestion(String challengeName, int nQuestion, String question,
            int correct, String[] answers, byte[] img, List<byte[]> music) {
        Challenge challenge = state.getChallenge(challengeName);
        PDU ans = makeQuestionPDU(challengeName, nQuestion, question, correct, answers, img);
        challenge.getSubscribers().stream()
                .filter(user -> user.getIP() != null)
                .forEach((user) -> {
                    socket.sendPDU(ans, user.getIP(), user.getPort());
                });
        
        IntStream.range(0, question.length())
                .mapToObj(i -> new T3<>(i, music.get(i), new PDU(PDUType.REPLY)))
                .peek(t -> t.c.addParameter(PDUType.REPLY_NUM_BLOCK,(byte)(int) t.a))
                .peek(t -> t.c.addParameter(PDUType.REPLY_BLOCK, t.b))
                .peek(t ->{ if (t.a < music.size()) t.c.addParameter(PDUType.CONTINUE,(byte) 0); })
                .map(t -> t.c)
                .flatMap(pdu -> challenge.getSubscribers().stream().map(u ->new T2<>(u,pdu)))
                .forEach(t -> {
                    socket.sendPDU(t.b, t.a.getIP(), t.a.getPort());
                });
    }

    public PDU makeQuestionPDU(String challengeName, int nQuestion, String question,
            int correct, String[] answers, byte[] img) {
        PDU questionPDU = new PDU(PDUType.REPLY);

        questionPDU.addParameter(PDUType.REPLY_CHALLE, challengeName);
        questionPDU.addParameter(PDUType.REPLY_NUM_QUESTION, (byte) nQuestion);
        questionPDU.addParameter(PDUType.REPLY_QUESTION, question);
        questionPDU.addParameter(PDUType.REPLY_CORRECT, (byte) correct);
        for (int i = 0; i < answers.length; i++) {
            questionPDU.addParameter(PDUType.REPLY_NUM_ANSWER, (byte) (i + 1));
            questionPDU.addParameter(PDUType.REPLY_ANSWER, answers[i]);
        }
        //@todo fazer o loadImage na classe Question
        questionPDU.addParameter(PDUType.REPLY_IMG, img);
        questionPDU.addParameter(PDUType.CONTINUE, (byte) 0);

        return questionPDU;
    }
}
