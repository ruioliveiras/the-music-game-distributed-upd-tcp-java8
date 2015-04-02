/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author paulo
 */
public class MultiCastClient {
   
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException{
        InetAddress group = InetAddress.getByName("228.5.6.7");
        MulticastSocket s = new MulticastSocket(12345);
        // Entra no grupo. A partir deste momento as mensagens
        //para 228.5.6.7 serao recebidas em s.
        s.joinGroup(group);
        
        while(true){
        byte[] dadosReceber = new byte[1024];
        
        DatagramPacket receivePacket = new DatagramPacket(dadosReceber, dadosReceber.length);
        s.receive(receivePacket);
        
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("From algum sitio:" + modifiedSentence);
        }
        // Envia e recebe mensagens UDP conforme apresentado anteriormente...
        // Retira-se do grupo. Mensagens para 228.5.6.7
        //não mais chegarão até o socket s.
        //s.leaveGroup(group);
    }
}
