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
import java.net.UnknownHostException;

/**
 *
 * @author paulo
 */
public class UDPMulticastTest {
    
    public static void main(String[] args) throws UnknownHostException, IOException{
        DatagramSocket serverSocket = new DatagramSocket(12346);
        InetAddress group = InetAddress.getByName("228.5.6.7");
        byte[] dadosEnviar = new byte[1024];
        BufferedReader inResposta = new BufferedReader(new InputStreamReader(System.in));   
       
        while(true)
        {
            String cenas = inResposta.readLine();
            dadosEnviar = cenas.getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(dadosEnviar, dadosEnviar.length, group);
            serverSocket.send(sendPacket);         
        }
    }
}
