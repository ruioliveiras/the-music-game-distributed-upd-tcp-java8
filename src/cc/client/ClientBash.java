package cc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pc14
 */
public class ClientBash {
    
    private static UDPCommunication udpCom;

    public ClientBash(){
        udpCom = new UDPCommunication();
    }
    
    /**
     * @param nArgs número de argumentos mandados em input pelo utilizador
     * @param validNArgs número de argumentos necessarios para a funcao correr
     * @return retorna falso caso numero de argumentos do input nao coincidir com o número
     * de argumentos necessarios para a funcao; retorna verdadeiro caso contrario
     */
    public static boolean checkTotalArgs(int nArgs, int validNArgs){
        if (nArgs!=validNArgs) {
            return false;
        }
        return true; 
    }
   
    
    /**
     * Verifica se uma dada palavra corresponde a um número
     * 
     * @param number Palavra que se quer testar
     * @return Booleano que indica se a palavra corresponde a um número
     */
    public static boolean testNumeric(String number) {
        try {
            int aux=Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    
    /**
     * Interpreta e ordena a execução de um dado comando
     * 
     * @param command Comando que se pretende executar
     * @throws IOException 
     */
    public static void execute(String command) throws IOException{
        String[] args = command.split("\\s+");
        int nargs = args.length;
        String operation= args[0].toUpperCase();
        int i;
        
        switch(operation) {
            case "HELLO": 
                if (checkTotalArgs(nargs, 1)){
                    udpCom.makeDatagram(1);
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;              
            case "REGISTER":
                if (checkTotalArgs(nargs, 4)){
                    //call func
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "LOGIN":
                if (checkTotalArgs(nargs, 3)){
                    
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "LOGOUT":
                //call func
                break;
            case "LIST_CHALLENGES":
                //call func
                break;
            case "MAKE_CHALLENGE":
                if (checkTotalArgs(nargs, 4)){
                    //call func
                }   
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "ACCEPT_CHALLENGE":
                if (checkTotalArgs(nargs, 3)){
                    //call func
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "DELETE_CHALLENGE":
                if (checkTotalArgs(nargs, 3)){
                    //call func
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "ANSWER":
                if (checkTotalArgs(nargs, 4)){
                    if(testNumeric(args[1]) && testNumeric(args[3])){
                        //callfunc
                    }
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "RETRANSMIT":
                if (checkTotalArgs(nargs, 4)){
                    if(testNumeric(args[2]) && testNumeric(args[3])){
                        //callfunc
                    }
                    else {
                        System.out.println("Formato de argumentos inválido!");
                    }
                }
                else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "LIST_RANKING":
                //call func
            default:
                System.out.println("Comando não reconhecido!");
                break;
        }
    }
    
    /*
    public static void main(String[] args) {
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String command;
        
        try {
            while(!(command=in.readLine()).toUpperCase().equals("END")){
                execute(command.toUpperCase());
            }
            System.out.println("Obrigado e Até à Próxima!");
        } catch (IOException ex) {
            Logger.getLogger(ClientBash.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }*/
    
    
}
