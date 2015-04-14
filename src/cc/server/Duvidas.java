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
public class Duvidas {
    /**
     * DUVIDAS BY OLIVEIRAS, PARA ESCLARECER.
     *
     * ServerState tem 2 maps localUsers e globalUser, faz sentido?
     *     *
     * Sockets? é necessario fechar os sockets? mas entao vai-se estar sempre a
     * abrir sockets para toda a gente? Para ser mais facil de gerir posso
     * utilizar os sockets num so sentido? - porque? pensar em caso em que se um
     * fecha mas o outro continua a enviar, e depois ja quer enviar.
     * Entao umas vezes usa o mesmo outras vezes nao?.
     * 
     * Servidor comessar desaficos
     *      
     * cliente recever iniciar a interface
     *      -> UDPClient.makeDatagramAcceptChallenge (explicado la)
     * 
     * Servidor Falta transmitir musicas, e imamgens e questoes,
     * Cliente transmitir a sua resposta
     *  
     * Servidor tem que transmitir a resposta correta e pontos
     * 
     * /!\ na parte da trasmição é necessario retrasmimição.
     * 
     * 
     * 
     * 
     */
}
