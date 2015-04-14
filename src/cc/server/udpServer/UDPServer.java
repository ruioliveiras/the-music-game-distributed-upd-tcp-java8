package cc.server.udpServer;

import cc.client.UDPClientCommunication;
import cc.pdu.PDU;
import cc.server.tcpServer.ServerState;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class UDPServer {
    private DatagramSocket s_socket;
    private int port; 
    private UDPServerCommunication com;
    private UDPServerHandler handler;
    private ServerState state;
    
    //porta predefinida
    public UDPServer(int port, ServerState state){
        try {
            //Constructs a datagram socket and binds it to the specified port on the local host machine.
            //É possivel definir outro endereço ip
            s_socket = new DatagramSocket(port);
            com = new UDPServerCommunication();
            this.state = state;
            handler = new UDPServerHandler(state);
            
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Servidor.");
        }   
    }

    public UDPServer(int udpPort, InetAddress updAdress, ServerState state) {
        
        try {
            //Constructs a datagram socket and binds it to the specified port on the local host machine.
            //É possivel definir outro endereço ip
            s_socket = new DatagramSocket(port,updAdress);
            com = new UDPServerCommunication();
            this.state = state;
            handler = new UDPServerHandler(state);
            
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
        PDU pdu_received = null, pdu_response;
        
        
        while(true){
            receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
            this.getS_socket().receive(receive_packet); //fica à espera de receber o pacote
            
            pdu_received = com.readDatagram(receive_packet.getData());

            dest_ip = receive_packet.getAddress();  // obtem o endereço ip e porta do cliente que enviou o datagrama para enviar resposta
            dest_port = receive_packet.getPort();
            
            pdu_response = handler.decodePacket(pdu_received, dest_ip.toString());
            
            dadosEnviar = pdu_response.toByte();
            send_packet = new DatagramPacket(dadosEnviar, dadosEnviar.length, dest_ip, dest_port);
            this.getS_socket().send(send_packet);   
        }   
    }
    

    // arranjar para fazer a conexao fora do metodo
    public void multicastConnection() throws UnknownHostException, IOException{
        //MulticastSocket mc_socket = null;
        this.getS_socket().setBroadcast(true);
        String resposta = null, client_msg = null;
        DatagramPacket send_packet = null, receive_packet = null;
        byte[] dadosEnviar = new byte[1024];
        byte[] dadosReceber = new byte[1024];
        InetAddress dest_ip = InetAddress.getByName("255.255.255.255");
        int dest_port;
        
        while(true){
            receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
            this.getS_socket().receive(receive_packet);
            client_msg = new String(dadosReceber, "UTF-8");
            //dest_ip = receive_packet.getAddress(); // desnecessario
            dest_port = receive_packet.getPort(); //
            
            
            
            //System.out.println("Cliente " +dest_port+": "+ client_msg);
            
            //resposta = "Pacote entregue com sucesso. Porta: " + dest_port;

            //dadosEnviar = resposta.getBytes();
            send_packet = new DatagramPacket(dadosEnviar, dadosEnviar.length, dest_ip, dest_port);
            this.getS_socket().send(send_packet);   
        }

    }
    
    public static void main(String args[])
    {
        int server_port = 12345;
        
        UDPServer s1 = new UDPServer(12345,new ServerState());
        try {
            //s1.unicastConnection();
            s1.multicastConnection();
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}