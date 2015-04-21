/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.udpServer;

import cc.pdu.PDU;
import cc.pdu.PDUType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author ruioliveiras
 */
public class UDPComunication {

    private DatagramSocket socket;
    private int currentLabel;
    private boolean labelMode;
    private InetAddress destIp;
    private int destPort;

    public UDPComunication(int sourcePort, InetAddress sourceIp, int destPort, InetAddress destIp) {
        try {
            socket = new DatagramSocket(sourcePort, sourceIp);
            this.destPort = destPort;
            this.destIp = destIp;
            labelMode = true;
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    /**
     * Constructor to server case when there are not destiny
     */
    public UDPComunication() {
        try {
            socket = new DatagramSocket();
            labelMode = false;
        } catch (SocketException ex) {
            System.out.println("Não foi possível criar Cliente.");
        }
    }

    public void sendPDU(PDU send_pdu) {
        DatagramPacket send_packet = new DatagramPacket(send_pdu.toByte(), send_pdu.getSizeBytes(), this.destIp, this.destPort);

        try {
            this.socket.send(send_packet);
        } catch (IOException exception) {
            System.out.println("Erro ao enviar o datagrama para o servidor");
        }
    }

    public PDU nextPDU() {
        byte[] pData;
        boolean hasNext = true;
        PDU lastPDU = new PDU(), pduAux;

        do {
            hasNext = false;
            pduAux = new PDU();
            pData = connectionReceiveBytes();
            //reading the pdu just to see the label
            pduAux.initHeaderFromBytes(pData, 0);
            // if is the currect response
            if (!labelMode) {
                currentLabel = pduAux.getLabel();
            }

            if (pduAux.getLabel() == currentLabel) {
                lastPDU.initHeaderFromBytes(pData, 0);
                lastPDU.initParametersFromBytes(pData, 8);
                hasNext = lastPDU.hasParameter(PDUType.CONTINUE);
            } else {
                //try again until has the currect label
                hasNext = true;
            }
        } while (hasNext);

        if (labelMode) {
            currentLabel++;
        }

        return lastPDU;
    }

    private byte[] connectionReceiveBytes() {
        DatagramPacket receive_packet = null;
        byte[] dadosReceber = new byte[1024 * 128];

        receive_packet = new DatagramPacket(dadosReceber, dadosReceber.length);
        try {
            this.socket.receive(receive_packet);
            destPort = receive_packet.getPort();
            destIp = receive_packet.getAddress();
        } catch (IOException ex) {
            System.out.println("Erro ao receber o pacote no UDPClientCommunication.");
        }

        return dadosReceber;
    }

    public void setLabelMode(boolean labelMode) {
        this.labelMode = labelMode;
    }

    public void setDest(InetAddress destIp, int destPort) {
        this.destIp = destIp;
        this.destPort = destPort;
    }

    public int getCurrentLabel() {
        return currentLabel;
    }

    public InetAddress getDestIp() {
        return destIp;
    }

    public int getDestPort() {
        return destPort;
    }
}
