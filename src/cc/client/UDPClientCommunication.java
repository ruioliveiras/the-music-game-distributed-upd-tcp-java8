package cc.client;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * To fragment a pdu, assuming the existence of 'int currentLabel' and 'pdu
 * lastPDU.'
 *
 * on initPDUfromDatagram do something like this:
 * msg_received.initHeaderFromBytes(headerBuffer); if (msg_received.getLabel ==
 * currentLabel){ lastPDU.initHeaderFromBytes(headerBuffer); msg_received =
 * lastPDU; } msg_received.initParametersFromBytes(bodyBuffer);
 *
 * o initPDUfromDatagram esta na classe UDPClient
 *
 * @author paulo
 */
public class UDPClientCommunication {

    private DatagramSocket c_socket;
    private PDU lastPDU;
    private int current_label;

    public UDPClientCommunication(int sourcePort, InetAddress sourceIp) {
        try {
            c_socket = new DatagramSocket(sourcePort, sourceIp);
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public UDPClientCommunication() {
        try {
            c_socket = new DatagramSocket();
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public void sendPDU(InetAddress dest, int port, PDU send_pdu) {
        byte[] dadosEnviar = new byte[1024];

        DatagramPacket send_packet = new DatagramPacket(send_pdu.toByte(), send_pdu.getSizeBytes(), dest, port);

        try {
            this.getC_socket().send(send_packet);
        } catch (IOException ex) {
            System.out.println("Erro ao enviar o datagrama para o servidor");
        }
    }

    public PDU nextPDU() {
        PDU pdu;
        byte[] pData;
        boolean hasNext = true;
        lastPDU = new PDU();
        
        do {
            pdu = new PDU();
            pData = connectionReceiveBytes();
            //reading the pdu just to see the label
            pdu.initHeaderFromBytes(pData, 0);
            
            // if is the currect response
            if (pdu.getLabel() == current_label) {
                lastPDU.initHeaderFromBytes(pData, 0);
                lastPDU.initParametersFromBytes(pData, 8);
            } else {
            // do some work
            }
            hasNext = pdu.hasParameter(PDUType.CONTINUE);

        } while (pdu.getLabel() == current_label && hasNext);

        current_label++;
        
        return lastPDU;
    }

    public DatagramSocket getC_socket() {
        return c_socket;
    }

    private byte[] connectionReceiveBytes() {
        DatagramPacket receive_packet = null;
        byte[] dadosReceber = new byte[1024];

        receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
        try {
            this.getC_socket().receive(receive_packet);
        } catch (IOException ex) {
            System.out.println("Erro ao receber o pacote no UDPClientCommunication.");
        }

        return dadosReceber;
    }

    public int getCurrentLabel() {
        return current_label;
    }

    public PDU getLastPDU() {
        return lastPDU;
    }
}
