/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author paulo
 */
public class Client {
    // cliente tem de comunicar com o servidor atraves de conexão UDP unicast
    public static void main(String args[]) throws Exception
    {
        BufferedReader inResposta = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        
        InetAddress IPAddress = InetAddress.getByName("localhost");
        
        byte[] dadosEnviar = new byte[1024];
        byte[] dadosReceber = new byte[1024];
        
        String fraseTeste = inResposta.readLine();
        dadosEnviar = fraseTeste.getBytes();
        
        DatagramPacket sendPacket = new DatagramPacket(dadosEnviar, dadosEnviar.length, IPAddress, 12345);
        clientSocket.send(sendPacket);
        
        DatagramPacket receivePacket = new DatagramPacket(dadosReceber, dadosReceber.length);
        clientSocket.receive(receivePacket);
        
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
   } 
}