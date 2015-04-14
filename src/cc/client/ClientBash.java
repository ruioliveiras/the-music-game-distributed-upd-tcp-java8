package cc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author pc14
 */
public class ClientBash {

    private static UDPClient client;

    public ClientBash(String sourceIp, String destIp, int destPort) {
        client = new UDPClient(sourceIp, 2020, destIp, destPort);
    }

    public ClientBash() {
        client = new UDPClient("127.0.0.65", 2020, "127.0.0.1", 5050);
    }

    public UDPClient getUDPClient() {
        return client;
    }

    /**
     * @param nArgs número de argumentos mandados em input pelo utilizador
     * @param validNArgs número de argumentos necessarios para a funcao correr
     * @return retorna falso caso numero de argumentos do input nao coincidir
     * com o número de argumentos necessarios para a funcao; retorna verdadeiro
     * caso contrario
     */
    public boolean checkTotalArgs(int nArgs, int validNArgs) {
        if (nArgs != validNArgs) {
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
    public boolean testNumeric(String number) {
        try {
            int aux = Integer.parseInt(number);
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
    public void execute(String command) throws IOException {
        String[] args = command.split("\\s+");
        int nargs = args.length;
        String operation = args[0].toUpperCase();
        int i;

        switch (operation) {
            case "HELLO":
                if (checkTotalArgs(nargs, 1)) {
                    client.makeDatagramHello();
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "REGISTER":
                if (checkTotalArgs(nargs, 4)) {
                    client.makeDatagramRegister(args[1], args[2], args[3].getBytes());
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "LOGIN":
                if (checkTotalArgs(nargs, 3)) {
                    client.makeDatagramLogin(args[1], args[2].getBytes());
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "LOGOUT":
                if (checkTotalArgs(nargs, 1)) {
                    client.makeDatagramLogout();
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "LIST_CHALLENGES":
                if (checkTotalArgs(nargs, 1)) {
                    client.makeDatagramList_Challenges();
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;

            //é preciso fazer o parsing da data    
            case "MAKE_CHALLENGE":
                if (checkTotalArgs(nargs, 4)) {
                    LocalDate d = LocalDate.parse(args[2], DateTimeFormatter.ISO_DATE);
                    LocalTime t = LocalTime.parse(args[3], DateTimeFormatter.ISO_TIME);

                    client.makeDatagramMake_Challenge(args[1], d, t);
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "ACCEPT_CHALLENGE":
                if (checkTotalArgs(nargs, 2)) {
                    client.makeDatagramAccept_Challenge(args[1]);
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            case "DELETE_CHALLENGE":
                if (checkTotalArgs(nargs, 2)) {
                    client.makeDatagramDelete_Challenge(args[1]);
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;

            //estes dois nao é o utilizar que envia, mas sim a aplicação que efetua consoante necessário    
            /*case "ANSWER":
             if (checkTotalArgs(nargs, 4)){
             if(testNumeric(args[1]) && testNumeric(args[3])){
             client.makeDatagramAnswer(args[1], args[2], args[3]);
             }
             }
             else {
             System.out.println("Número de argumentos inválido!");
             }
             break;
             case "RETRANSMIT":
             if (checkTotalArgs(nargs, 4)){
             if(testNumeric(args[2]) && testNumeric(args[3])){
             client.makeDatagramRetransmit();
             }
             else {
             System.out.println("Formato de argumentos inválido!");
             }
             }
             else {
             System.out.println("Número de argumentos inválido!");
             }
             break;*/
            case "LIST_RANKING":
                if (checkTotalArgs(nargs, 1)) {
                    client.makeDatagramList_Ranking();
                } else {
                    System.out.println("Número de argumentos inválido!");
                }
                break;
            default:
                System.out.println("Comando não reconhecido!");
                break;
        }
    }

    public static void main(String[] args) {

        ClientBash c_bash = new ClientBash();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String command;

        try {
            while (!(command = in.readLine()).toUpperCase().equals("END")) {
                c_bash.execute(command.toUpperCase());

                c_bash.getUDPClient().receive_packet();
            }
            System.out.println("Obrigado e Até à Próxima!");
        } catch (IOException ex) {
            System.out.println("Erro ao ler do stdin na Bash");
        }

        c_bash.getUDPClient().closeC_Socket();
    }

}
