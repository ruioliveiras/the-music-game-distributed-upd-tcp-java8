/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server.communication;

import cc.pdu.PDU;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class as purpose encapsulate a socket - to use PDU. So it will read PDU
 * objects, and send PDU objects
 *
 * @author ruioliveiras
 */
public class ServerCommunication {

    private final Socket socket;
    private InputStream is;
    private OutputStream os;

    /** Creates a new ServerCommunication using a pre-initialized socket.
     * @param socket
     */
    public ServerCommunication(Socket socket) {
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
    public PDU readNext() throws IOException {
        PDU pdu = new PDU();
        byte[] headerBuffer = new byte[8];
        byte[] bodyBuffer = new byte[1024];

        if (is.read(headerBuffer, 0, 8) == 8) {
            pdu.initHeaderFromBytes(headerBuffer);
            if (pdu.getSizeBytes() > bodyBuffer.length) {
                //error
            }
//          if has label then is fragmented
//          if (pdu.getLabel()){
//                
//          }

            if (is.read(bodyBuffer, 0, pdu.getSizeBytes()) != pdu.getSizeBytes()) {
                //error
            }
            pdu.initParametersFromBytes(bodyBuffer);
        } else {
            //error
        }

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
        return socket.getInetAddress().toString();
    }

    /**
     * Get Ip as InetAdress, where the socket are connected
     *
     * @return
     */
    public InetAddress getIpByte() {
        return socket.getInetAddress();
    }
}
