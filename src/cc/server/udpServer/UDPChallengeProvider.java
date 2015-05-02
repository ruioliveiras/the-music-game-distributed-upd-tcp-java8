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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.util.Pair;

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
            PDU ans = makeQuestionPDU(challenge, i);
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
//                challenge.getServers().stream()
//                        .forEach((server)->{ server.});
            };
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
                .mapToObj(i -> new Pair<>(i, music.get(i)))
                .map((pair) -> {
                    PDU pdu = new PDU(PDUType.REPLY);

                    return pdu;
                })
                .flatMap((pdu) -> challenge.getSubscribers().stream().map(u -> new Pair<>(u, pdu)))
                .filter(pair -> pair.getKey().getIP() != null)
                .forEach((pair) -> {
                    socket.sendPDU(pair.getValue(), pair.getKey().getIP(), pair.getKey().getPort());
                });

        int i = 0;
        boolean hasNext = true;
        while (music) {
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
//                challenge.getServers().stream()
//                        .forEach((server)->{ server.});
        };
    }

    public PDU makeQuestionPDU(Challenge challenge, int nQuestion) {
        Question q = challenge.getQuestion(nQuestion);
        q.loadImage();
        return makeQuestionPDU(challenge.getName(), nQuestion, q.getQuestion(),
                q.getCorrect(), q.getAnwser(), q.getImageArray());
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
