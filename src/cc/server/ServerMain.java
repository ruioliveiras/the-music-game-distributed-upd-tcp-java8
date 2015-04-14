/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import cc.client.ClientBash;
import cc.server.tcpServer.ServerState;
import cc.model.Question;
import cc.server.tcpServer.facade.TcpLocal;
import cc.server.tcpServer.communication.ServerHandler;
import cc.server.tcpServer.facade.TcpHub;
import cc.server.udpServer.UDPServer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruioliveiras
 */
public class ServerMain {

    private final static int DEFAULT_TCP_PORT = 8080;
    private final static int DEFAULT_UDP_PORT = 5050;
    private final ServerState state;
    private final ServerSocket ss;
    private final TcpLocal facadeMem;
    private final TcpHub facadeHub;
    private final UDPServer UDPServer;
    private final String name;

    //vai estar declarado static aqui um server state, e um server facade.
    //vai ter um metodo para começar o server handler 
    public ServerMain(int udpPort, int tcpListingPort, InetAddress address) throws IOException {
        this.ss = new ServerSocket(tcpListingPort, 0, address);
        this.state = new ServerState();
        this.facadeMem = new TcpLocal(state);
        this.facadeHub = new TcpHub(state);
        UDPServer = new UDPServer(udpPort, address, state);
        name = "" + tcpListingPort;
    }

    public void init(String initIp, String initPort) throws UnknownHostException {
        this.parseChallengeFile("desafio-000001.txt");
        facadeMem.registerServer(InetAddress.getByName(initIp), Integer.parseInt(initPort));
        state.getNeighbor(InetAddress.getByName(initIp).toString())
                .registerServer(this.ss.getInetAddress(), this.ss.getLocalPort());
    }

    public void startTCP() throws IOException {
        while (true) {
            Socket cn = ss.accept();
            ServerHandler handler = new ServerHandler(name, state, cn, facadeMem, facadeHub);
            Thread t = new Thread(handler, name + "|handle:" + cn.getRemoteSocketAddress().toString());
            t.start();
        }
    }

    public void startUdp() throws IOException {
        this.UDPServer.unicastConnection();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int portTcp, portUdp;
        if (args.length >= 3) {
            portTcp = Integer.parseInt(args[1]);
            portUdp = Integer.parseInt(args[2]);
        } else {
            portTcp = DEFAULT_TCP_PORT;
            portUdp = DEFAULT_UDP_PORT;
        }
        ServerMain main = new ServerMain(portUdp, portTcp, InetAddress.getByName("127.0.0.1"));
        startInOtherThread(main,"MainServer1");
        testServers();
        testClient();

        //main2.facadeHub.registerChallenge(, LocalDate.MIN, LocalTime.MIN, null, null);
    }

    public static void testServers() throws IOException, InterruptedException {
        int portTcp = 8080;
        int portUdp = 5050;

        Thread.sleep(400);
        System.out.println("2-------");

        // comment this is want to testServers
        ServerMain main2 = new ServerMain(portUdp + 1, portTcp + 1, InetAddress.getByName("127.0.0.2"));
        main2.init("127.0.0.1", portTcp + "");
        startInOtherThread(main2, "MainServer2");

        Thread.sleep(1000);
        System.out.println("3-------");

        ServerMain main3 = new ServerMain(portUdp + 2, portTcp + 2, InetAddress.getByName("127.0.0.3"));
        main3.init("127.0.0.1", portTcp + "");
        startInOtherThread(main3, "MainServer2");

        Thread.sleep(1000);
        System.out.println("4-------");

        ServerMain main4 = new ServerMain(portUdp + 3, portTcp + 3, InetAddress.getByName("127.0.0.4"));
        main4.init("127.0.0.1", portTcp + "");
        startInOtherThread(main4, "MainServer4");
    }

    public static void testClient() throws IOException {
        testClient01();
        testClient02();
        testClient03();
    }
    
    public static void testClient01() throws IOException {
        ClientBash c1 = new ClientBash("127.0.0.65","127.0.0.1",5050);
        //cb.execute("REGISTAR nome nick pass");
        c1.execute("REGISTER rui ruioliveiras 123");
        c1.execute("LOGIN ruioliveiras 123");
        c1.execute("LOGOUT");
        
        c1.execute("REGISTER paulo prc 123");
        c1.execute("LOGIN prc 123");
        c1.execute("LOGOUT");

        c1.execute("REGISTER orlando orlando 123");
        c1.execute("LOGIN orlando 123");
        c1.execute("LOGOUT");
    }
    
    public static void testClient02() throws IOException {
        ClientBash c1 = new ClientBash("127.0.0.66","127.0.0.1",5050);
        ClientBash c2 = new ClientBash("127.0.0.67","127.0.0.1",5050);
        
        c2.execute("LOGIN prc 123");
        c2.execute("MAKE_CHALLENGE Circo 2015-05-02 15:15");
        c2.execute("LOGOUT");
        
        
        //cb.execute("REGISTAR nome nick pass");
        c1.execute("LOGIN ruioliveiras 123");
        c1.execute("MAKE_CHALLENGE oliveirasChallenge 2015-05-02 15:00");
        c1.execute("LIST_CHALLENGES");
        c1.execute("LOGOUT");
    }

    public static void testClient03() throws IOException {
        ClientBash c1 = new ClientBash("127.0.0.68","127.0.0.1",5050);
        ClientBash c2 = new ClientBash("127.0.0.69","127.0.0.1",5050);
        
        c2.execute("LOGIN ruioliveiras 123");
        c2.execute("ACCEPT_CHALLENGE Circo");
        c2.execute("LOGOUT");
        
        
        //cb.execute("REGISTAR nome nick pass");
        c1.execute("LOGIN orlando 123");
        c1.execute("ACCEPT_CHALLENGE Circo");
        c1.execute("LOGOUT");
    }

    
    public static void startInOtherThread(final ServerMain main, String name) {
        Thread t1 = new Thread(() -> {
            try {
                main.startTCP();
            } catch (IOException ex) {
                throw new RuntimeException("howad");
            }
        }, "TCP" + name);
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                main.startUdp();
            } catch (IOException ex) {
                throw new RuntimeException("howad");
            }
        }, "UDP" + name);

        t2.start();
    }

    public void parseChallengeFile(String filepath) {
        String assetsFolder = "assets";
        String imgPath, imgDir, musicPath, musicDir, questionText, line;
        String[] answers, aux;
        int nrQuestions, correctAnsIndex;
        BufferedReader reader;
        Question question;

        answers = new String[3];
        imgDir = musicDir = null;

        try {
            reader = new BufferedReader(new FileReader("assets/"+filepath));
            while ((line = reader.readLine()) != null) {
                if (line.contains("music_DIR=")) {
                    musicDir = line.split("=")[1];
                } else if (line.contains("images_DIR=")) {
                    imgDir = line.split("=")[1];
                } else if (line.contains("questions_#=")) {
                    nrQuestions = Integer.parseInt(line.split("=")[1]);
                } else {
                    aux = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                    if (musicDir != null) {
                        musicPath = musicDir + "/" + aux[0];
                    } else {
                        musicPath = aux[0];
                    }
                    if (imgDir != null) {
                        imgPath = imgDir + "/" + aux[1];
                    } else {
                        imgPath = aux[1];
                    }
                    aux[2] = aux[2].trim();
                    questionText = aux[2].substring(1, aux[2].length() - 1);
                    aux[3] = aux[3].trim();
                    answers[0] = aux[3].substring(1, aux[3].length() - 1);
                    aux[4] = aux[4].trim();
                    answers[1] = aux[4].substring(1, aux[4].length() - 1);
                    aux[5] = aux[5].trim();
                    answers[2] = aux[5].substring(1, aux[5].length() - 1);
                    correctAnsIndex = Integer.parseInt(aux[6]);
                    // question = new Question(questionText, answers.clone(), correctAnsIndex,assetsFolder +"/" + imgPath,assetsFolder + "/" + musicPath);
                    // state.addQuestion(question);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
