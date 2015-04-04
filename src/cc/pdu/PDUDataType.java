/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.pdu;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javafx.util.Pair;

/**
 *
 * @author ruioliveiras
 */
public enum PDUDataType {
    string {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    try {
                        int i;
                        for (i = 0; i < b.length && b[offset + i] != 0; i++) {
                        }
                        String str = new String(b, offset, i, "UTF-8");

                        return new Pair<>(str, i);
                    } catch (UnsupportedEncodingException ex) {
                        // stupid error here?
                    }
                    return null;
                }
            },
    integer {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(), 4);
                }
            },
    date {
                public Pair<Object, Integer> read(byte[] b, int offset) {

                    return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(), 4);
                }
            },
    hour {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(), 4);
                }

            },
    ip {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(), 4);
                }
            },
    port {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(), 4);
                }
            };
    
    /** Parses an PDUDataType from a certain buffer
     * 
     * @param b the buffer array
     * @param offset the offset of the buffer array
     * @return Pair<Object, Integer> where the Object is the read object and
     *  the Integer is the number of read bytes
     */
    public abstract Pair<Object, Integer> read(byte[] b, int offset);
}
