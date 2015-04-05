/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class UDPServer {
    private DatagramSocket s_socket;
    private int port; 
    
    //porta predefinida
    public UDPServer(int port){
        try {
            s_socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Servidor.");
        }   
    }
    
    public DatagramSocket getS_socket(){
        return this.s_socket;
    }    
    
    public void unicastConnection() throws IOException{
        String resposta = null, client_msg = null;
        byte[] dadosEnviar = new byte[1024];
        byte[] dadosReceber = new byte[1024];
        DatagramPacket send_packet = null, receive_packet = null;
        InetAddress dest_ip;
        int dest_port;
        
        while(true){
            receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
            this.getS_socket().receive(receive_packet);
            client_msg = new String(dadosReceber, "UTF-8");
            System.out.println("Cliente: "+ client_msg);
            dest_ip = receive_packet.getAddress();
            dest_port = receive_packet.getPort();
            
            resposta = "Pacote entregue com sucesso do cliente com a porta " + dest_port;

            dadosEnviar = resposta.getBytes();
            send_packet = new DatagramPacket(dadosEnviar, dadosEnviar.length, dest_ip, dest_port);
            this.getS_socket().send(send_packet);   
        }   
    }
    

    // arranjar para fazer a conexao fora do metodo
    public void multicastConnection(String group, int port){
        MulticastSocket mc_socket = null;
        InetAddress group_ip = null;
        String resposta = null, client_msg = null;
        DatagramPacket send_packet = null, receive_packet = null;
        byte[] dadosEnviar = new byte[1024];
        byte[] dadosReceber = new byte[1024];
        InetAddress dest_ip;
        int dest_port;
        
        
        try {
            group_ip = InetAddress.getByName(group);
            mc_socket = new MulticastSocket(port);
            
            mc_socket.joinGroup(group_ip);

        while(true){
            receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
            this.getS_socket().receive(receive_packet);
            client_msg = new String(dadosReceber, "UTF-8");
            System.out.println("Cliente: "+ client_msg);
            dest_ip = receive_packet.getAddress(); // desnecessario
            dest_port = receive_packet.getPort(); //
            
            resposta = "Pacote entregue com sucesso do cliente com a porta " + dest_port;

            dadosEnviar = resposta.getBytes();
            send_packet = new DatagramPacket(dadosEnviar, dadosEnviar.length, group_ip, dest_port); //aqui tem group_ip e nao dest_ip
            this.getS_socket().send(send_packet);   
        }
        
           // mc_socket.leaveGroup(group_ip);
        } catch (IOException ex) {
            System.out.println("Não foi possível obter endereço de grupo.");
        }
    }
    
    public static void main(String args[])
    {
        int server_port = 12345;
        
        UDPServer s1 = new UDPServer(12345);
        //try {
            //s1.unicastConnection();
            s1.multicastConnection("228.1.1.1", server_port);
       /* } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
}