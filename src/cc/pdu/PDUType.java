/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.pdu;

/**
 * This is an hierarchy Enum (This is just a programming pattern).
 *
 * 1º the first level of the hierarchy is the getDataType of The PDU and uses
 * the constructor " PDUType(int getId, int argSize)" where the getId is the
 * identifier of the PDU, and the argSize is the number of parameter.
 *
 * 2º the second level represents the possibles parameters of the PDU
 * getDataType, for instance: an INFO PDU can have: an name (INFO_NAME) or nick
 * (INFO_NICK); the constructor used are "PDUType(PDUType getParent, int getId,
 * PDUDataType dataType)" where the Parent is the correspondent getDataType of
 * the current parameter; the getId is the identifier of this parameter the
 * getDataType is the DataType of this parameter (Ex: string, integer...)
 *
 * @author ruioliveiras
 */
public enum PDUType {

    REPLAY(1, 0),
    HELLO(1, 0),
    REGISTER(1, 0),
    LOGIN(1, 0),
    LOGOUT(1, 0),
    QUIT(1, 0),
    END(1, 0),
    LIST_CHALLENGES(1, 0),
    MAKE_CHALLENGE(1, 0),
    ACCEPT_CHALLENGE(1, 0),
    DELETE_CHALLENGE(1, 0),
    ANSWER(1, 0),
    RETRANSMIT(1, 0),
    LIST_RANKING(1, 0),
    INFO(31, 10),
        INFO_NAME(INFO, 1, PDUDataType.string),
        INFO_NICK(INFO, 2, PDUDataType.string),
        INFO_CHALLE(INFO, 7, PDUDataType.string),
        INFO_DATE(INFO, 4, PDUDataType.date),
        INFO_HOUR(INFO, 5, PDUDataType.hour),
        INFO_NQUEST(INFO, 10, PDUDataType.integer),
        INFO_QUEST(INFO, 11, PDUDataType.string),
        INFO_NRESP(INFO, 12, PDUDataType.integer),
        INFO_RESP(INFO, 13, PDUDataType.string),
        INFO_CERT(INFO, 14, PDUDataType.string),
        INFO_IMAGE(INFO, 16, PDUDataType.string),
        INFO_MUSIC(INFO, 19, PDUDataType.string),
        INFO_SCORE(INFO, 20, PDUDataType.integer),
        INFO_IPSERVER(INFO, 30, PDUDataType.ip),
        INFO_PORT(INFO, 31, PDUDataType.port);

    //--------------------
    private int id, numParam;
    private PDUType parent;
    private PDUDataType dataType;

    private PDUType() {
    }

    private PDUType(int id, int numParam) {
        this.id = id;
        this.numParam = numParam;
        parent = null;
        dataType = null;

    }

    private PDUType(PDUType parent, int id, PDUDataType dataType) {
        this.parent = parent;
        this.dataType = dataType;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public PDUType getParent() {
        return parent;
    }

    public PDUDataType getDataType() {
        return dataType;
    }

    public int getNumParam() {
        return numParam;
    }

    /**
     * Get all the parameters of the current PDUType
     *
     * @return the array of the PDUType parameters
     */
    public PDUType[] getParameters() {
        int i = 0;
        PDUType[] res = new PDUType[numParam];
        // for all values of this enum (PDUType.values())
        for (PDUType value : PDUType.values()) {
            if (value.getParent() != null
                    && value.getParent().getId() == id) {
                res[i++] = value;
            }
        }
        return res;
    }

    /**
     * Get the parameter of the current PDUType with a certain Id
     *
     * @param paramId wanted id
     * @return the wanted PDUType Parameter or null if don't exists.
     */
    public PDUType getParameterById(int paramId) {
        for (PDUType value : values()) {
            if (value.getParent() != null
                    && value.getParent().getId() == this.id
                    && value.id == paramId) {
                return value;
            }
        }
        return null;
    }

    /**
     * Get a PDUType from all the values with a certain id
     *
     * @param id wanted id
     * @return the wanted PDUType or null if don't exists.
     */
    public static PDUType getById(int id) {
        for (PDUType value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }
}
