/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc;

/**
 *
 * @author ruioliveiras
 */
public class ApplicationException extends RuntimeException{
    private int lvl; //define the level's (lvl will has an action associated) send error back, just log, stop, restart, send 
    private String msg;
    
}

