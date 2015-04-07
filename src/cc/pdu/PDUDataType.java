/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.pdu;

import cc.thegame.Testing;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
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
                    int year = 2000 + (b[offset + 0] << 8) + (b[offset + 1]);
                    int month = (b[offset + 2] << 8) + (b[offset +3]);
                    int day = (b[offset + 4] << 8) + (b[offset + 5]);

                    LocalDate ld = LocalDate.of(year, month, day);
                    return new Pair<>(ld, 6);
                }
            },
    hour {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    int hour = 2000 + (b[offset + 0] << 8) + (b[ offset + 1]);
                    int min = (b[offset + 2] << 8) + (b[ offset + 3]);
                    int sec = (b[offset + 4] << 8) + (b[offset+ 5]);

                    LocalTime lt = LocalTime.of(hour, min, sec);
                    return new Pair<>(lt, 6);
                }

            },
    ip {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    byte[] ip =  new byte[4];
                    for (int i = 0; i < 5; i++) {
                        ip[i] = b[offset + i];    
                    }
                    return new Pair<>(ip, 4);
                }
            },
    port {
                public Pair<Object, Integer> read(byte[] b, int offset) {
                    int port = (b[0] << 8) + (b[1]);
                    return new Pair<>(port, 4);
                }
            };

    /**
     * Parses an PDUDataType from a certain buffer
     *
     * @param b the buffer array
     * @param offset the offset of the buffer array
     * @return Pair<Object, Integer> where the Object is the read object and the
     * Integer is the number of read bytes
     */
    public abstract Pair<Object, Integer> read(byte[] b, int offset);
}
