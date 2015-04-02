/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author ruioliveiras
 */
public class ServerPDU {
    public interface ByteReadable{
        public Pair<Object,Integer> read(byte[] b,int offset);
    };
    
    public enum ArgsType implements ByteReadable{
        string {
            public Pair<Object,Integer> read(byte[] b,int offset){
                try {
                    int i;
                    for (i = 0; i < b.length && b[offset + i] != 0; i++) { }
                    String str = new String(b,offset,i,"UTF-8");
                    
                    return new Pair<>(str,i);
                } catch (UnsupportedEncodingException ex) {
                    // stupid error here?
                }
                return null;
            }
        },
        integer{
            public Pair<Object,Integer> read(byte[] b,int offset){
                return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(),4);
           }
        },
        date{
            public Pair<Object,Integer> read(byte[] b,int offset){
                
                return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(),4);
           }
        },
        hour{
            public Pair<Object,Integer> read(byte[] b,int offset){
                return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(),4);
            }

        },
        ip{
            public Pair<Object,Integer> read(byte[] b,int offset){
                return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(),4);
            }
        },
        port{
            public Pair<Object,Integer> read(byte[] b,int offset){
                return new Pair<>(ByteBuffer.wrap(b, offset, 4).getInt(),4);
            }
        };
    }
    
    public enum TypePDU {
        REPLAY(1,0),
        HELLO(1,0),
        REGISTER(1,0),
        LOGIN(1,0),
        LOGOUT(1,0),
        QUIT(1,0),
        END(1,0),
        LIST_CHALLENGES(1,0),
        MAKE_CHALLENGE(1,0),
        ACCEPT_CHALLENGE(1,0),
        DELETE_CHALLENGE(1,0),
        ANSWER(1,0),
        RETRANSMIT(1,0),
        LIST_RANKING(1,0),
        INFO(1,10),
            INFO_NAME(INFO, 1,ArgsType.string),
            INFO_NICK(INFO, 2,ArgsType.string),
            INFO_CHALLE(INFO, 7,ArgsType.string),
            INFO_DATE(INFO, 4,ArgsType.date),
            INFO_HOUR(INFO, 5,ArgsType.hour),
            INFO_NQUEST(INFO, 10,ArgsType.integer),
            INFO_QUEST(INFO, 11,ArgsType.string),
            INFO_NRESP(INFO, 12,ArgsType.integer),
            INFO_RESP(INFO, 13,ArgsType.string),
            INFO_CERT(INFO, 14,ArgsType.string),
            INFO_IMAGE(INFO, 16,ArgsType.string),
            INFO_MUSIC(INFO, 19,ArgsType.string),
            INFO_SCORE(INFO, 20,ArgsType.integer),
            INFO_IPSERVER(INFO, 30,ArgsType.ip),
            INFO_PORT(INFO, 31,ArgsType.port);
            
        //--------------------
        int id,argSize;
        TypePDU parent;
        ArgsType type;
        private TypePDU() {        
        }
        
        private TypePDU(int id, int argSize){
            this.id = id;this.argSize = argSize;
        }
        private TypePDU(TypePDU parent, int id,ArgsType type ){
            this.parent = parent;
            this.type = type;
        }
        
        
        public TypePDU parent(){return parent;}
        public int id(){return id;}
        public ArgsType type(){return type;}
        
        
        public TypePDU[] args(){
            int i = 0;
            TypePDU[] res = new TypePDU[argSize]; 
            for (TypePDU value : values()) {
                if (value.parent().id() == id){
                    res[i++] = value;
                }
            }
            return res;
        }
        
        public TypePDU arg(int id){
            for (TypePDU value : values()) {
                if (value.parent().id() == this.id 
                        && value.id == id){
                    return value;
                }
            }
            return null;
        }
        
        
        public static TypePDU getById(int id){
            for (TypePDU value : values()) {
                   if (value.id() == id){
                       return value;
                   }
            }
            return null;
        }
    }
    
    public final static int HEADER_SIZE = 8;

    // header param
    private int version; // 1 byte
    private boolean secure; // 1 byte
    private int label; // 2 byte
    private TypePDU type; // 1 byte
    private int nField; //2 bytes
    private int sizeBytes; //1 byte
    // args Param
    private Map<TypePDU,Object> args;
    
    public boolean appendHeader(byte[] b){
        if (b.length < 8){
           //throw new Exec
        }
        version =  b[0];
        secure =  b[1] != 0;
        label =  b[2] | b[3] << 8;
        type = TypePDU.getById(b[4]);
        nField = b[5] | b[6] << 8;
        sizeBytes = b[7];
        
        return true;
    }
    
    public void appendArgsBytes(byte[] b){
        int offset = 0,i =0;
        int argCode;
        TypePDU t;
        
        while(offset < b.length && i < sizeBytes){
           // argCode
            t = type.arg(b[offset++]);
            Pair<Object,Integer> obj = t.type().read(b, offset);
            offset += obj.getValue();
            args.put(t,obj.getKey());
        }
    }
    
    public int getSizeBytes(){return sizeBytes;}
    public int getFieldCount(){return nField;}
    public int getVersion(){return version;}
    public TypePDU getType(){return type;}
    public boolean isSecure() {return secure;}
    public Object getArg(TypePDU p) {return args.get(p);}
    public boolean hasArg(TypePDU p) {return args.containsKey(p);}
    
    
    
    //.. outrs funções uteis por index
    /*Versão [1 byte] (por defeito, 0)
    Segurança [1 byte] (por defeito, 0 – sem segurança)
    Label [2 bytes] (identificação que associa respostas a um pedido, 0 – broadcasting)
    Tipo [1 byte] (0 – REPLY, 1 – HELLO, 2 – REGISTER, ...)
    Número de Campos Seguintes [1 byte]
    Tamanho em bytes da Lista de Campos Seguintes [2 bytes]
    Lista de Campos Seguintes (dependente do Tipo, pode ser vazia)
    */
}
