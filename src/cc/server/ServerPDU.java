/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.server;

/**
 *
 * @author ruioliveiras
 */
public class ServerPDU {
    public final static int HEADER_SIZE = 8;
    
    private int version; // 1 byte
    private boolean secure; // 1 byte
    private int label; // 2 byte
    private int type; // 1 byte
    private int nField; //2 bytes
    private int sizeBytes; //1 byte
    
    private byte[] args;
    private int actualArg;

    public boolean appendHeader(byte[] b){
        if (b.length < 8){
           //throw new Exec
        }
        version =  b[0];
        secure =  b[1] != 0;
        label =  b[2] | b[3] << 8;
        type = b[4];
        nField = b[5] | b[6] << 8;
        sizeBytes = b[7];
        
        return true;
    }
    
    public int getSizeBytes(){return sizeBytes;}
    public int getFieldCount(){return nField;}
    public int getVersion(){return version;}
    public int getType(){return type;}
    public boolean isSecure() {return secure;}
    
    
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
