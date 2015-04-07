/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.pdu;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author ruioliveiras
 */
public class PDU {
    public final static int HEADER_SIZE_BYTES = 8;

    // header parameters
    private int version; // 1 byte
    private boolean secure; // 1 byte
    private int label; // 2 byte
    private PDUType pduType; // 1 byte
    private int nField; //2 bytes
    private int sizeBytes; //1 byte
    // Other Parameters
    private Map<PDUType, Object> parameters;

    public PDU() {
        parameters = new HashMap<>();
    }

    public PDU(PDUType type) {
        this();
        this.pduType = type;
    }
    
    public PDU(int version, boolean secure, int label, PDUType pduType, int nField) {
        this.version = version;
        this.secure = secure;
        this.label = label;
        this.pduType = pduType;
        this.nField = nField;
        this.parameters = new HashMap<>();
    }
    

    /**
     * This function initialize the headers of the PDU from a buffer of Bytes.
     * After call this function should call initParametersFromBytes, to
     * initialize the parameters
     *
     * @param b
     * @return boolean with success
     */
    public boolean initHeaderFromBytes(byte[] b) {
        if (b.length < 8) {
            //throw new Exec
        }
        version = b[0];
        secure = b[1] != 0;
        label = b[2] | b[3] << 8;
        pduType = PDUType.getById(b[4]);
        nField = b[5] | b[6] << 8;
        sizeBytes = b[7];

        return true;
    }

    public void initParametersFromBytes(byte[] b) {
        int offset = 0, i = 0;

        while (offset < b.length && i < sizeBytes) {
            // the first byte are the identifier of the Parameter Type.
            PDUType parameter = pduType.getParameterById(b[offset++]);

            Pair<Object, Integer> obj = parameter.getDataType().read(b, offset);

            offset += obj.getValue();
            parameters.put(parameter, obj.getKey());
        }
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public int getFieldCount() {
        return nField;
    }

    public int getVersion() {
        return version;
    }

    public PDUType getType() {
        return pduType;
    }

    public boolean isSecure() {
        return secure;
    }

    public Object getParameter(PDUType p) {
        return parameters.get(p);
    }

    public boolean hasParameter(PDUType p) {
        return parameters.containsKey(p);
    }
    
    public void addParameter(PDUType pduType, Object obj) {
        parameters.put(pduType, obj);
    }
    
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
