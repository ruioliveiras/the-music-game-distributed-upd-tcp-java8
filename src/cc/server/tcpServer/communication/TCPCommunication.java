/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.tcpServer.communication;

import cc.pdu.PDU;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class as purpose encapsulate a socket - to use PDU. So it will read PDU
 * objects, and send PDU objects
 *
 * @author ruioliveiras
 */
public class TCPCommunication {

    private final Socket socket;
    private InputStream is;
    private OutputStream os;

    /**
     * Creates a new ServerCommunication using a pre-initialized socket.
     *
     * @param socket
     */
    public TCPCommunication(Socket socket) {
        try {
            this.socket = socket;
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException ex) {
            throw new RuntimeException("a");
        }
    }

    /**
     * Get the Next PDU, if there aren't wait.
     *
     * @return
     * @throws IOException
     */
    public PDU nextPDU() throws IOException {
        PDU pdu = new PDU();
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer;

        do {
            if (is.read(headerBuffer, 0, 8) == 8) {
                pdu.initHeaderFromBytes(headerBuffer, 0);
                bodyBuffer = new byte[pdu.getSizeBytes()];
                if (pdu.getParameterSizeBytes() > bodyBuffer.length) {

                }
//          if has label then is fragmented
//          if (pdu.getLabel()){
//                
//          }

                if (is.read(bodyBuffer, 0, pdu.getParameterSizeBytes()) != pdu.getParameterSizeBytes()) {
                    //error
                }
                pdu.initParametersFromBytes(bodyBuffer, 0);
            } else {
                //error
            }
        } while (pdu.hasContinue());
        
        return pdu;
    }

    /**
     * This method sends a PDU
     *
     * @param p
     */
    public void sendPDU(PDU p) {
        try {
            os.write(p.toByte());
            os.flush();
        } catch (IOException ex) {
            throw new RuntimeException("a");
        }
    }

    /**
     * Get the port number where the socket are connected
     *
     * @return
     */
    public int getPort() {
        return socket.getPort();
    }

    /**
     * Get Ip as String, where the socket are connected
     *
     * @return
     */
    public String getIp() {
        return getIpByte().toString();
    }

    /**
     * Get Ip as InetAdress, where the socket are connected
     *
     * @return
     */
    public InetAddress getIpByte() {
        if (socket.getRemoteSocketAddress() instanceof InetSocketAddress) {
            return ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress();
        }
        return null;
    }
}
