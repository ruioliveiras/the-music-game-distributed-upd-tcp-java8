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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */

public class UDPClient {
    private InetAddress client_ip;
    private BufferedReader stdinput;
    private DatagramSocket c_socket;
    
    public UDPClient(){
        stdinput = new BufferedReader(new InputStreamReader(System.in));
        try {
            client_ip = InetAddress.getByName("localhost");
            c_socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }
    
    public DatagramSocket getC_socket(){
        return this.c_socket;
    }
    
    /**
     * Comunicação unicast com o servidor
     */
    public void unicastConnection(InetAddress dest, int port) throws IOException{
        String resposta = null, user_input = null;
        byte[] dadosEnviar = new byte[1024];
        byte[] dadosReceber = new byte[1024];
        DatagramPacket send_packet = null, receive_packet = null;
        
        while(true){
            user_input = stdinput.readLine();
            dadosEnviar = user_input.getBytes();
            send_packet = new DatagramPacket(dadosEnviar, dadosEnviar.length, dest, port);
            this.getC_socket().send(send_packet);   
            
            receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
            this.getC_socket().receive(receive_packet);
            resposta = new String(dadosReceber, "UTF-8");
            System.out.println("Servidor: "+ resposta);
        }
    }
    
    /**
     * Comunicação multicast
     */
    //public void multicastConnection();
       
    public static void main(String args[])
    {
        int server_port = 12345;
        InetAddress dest_ip = null; 
        try {
            dest_ip = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        UDPClient c1 = new UDPClient();
        try {
            c1.unicastConnection(dest_ip, server_port);
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        c1.getC_socket().close();
    } 
}