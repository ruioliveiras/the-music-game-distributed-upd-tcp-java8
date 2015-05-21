/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.swinggame;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 *
 * @author paulo
 */
public class MusicRunnable implements Runnable {
    private JFXPanel fxPanel;
    //private String music_url;
    private volatile boolean musicRunning;
    private byte[] musicArray;
    
    
    public MusicRunnable(){
        fxPanel = new JFXPanel();
        //this.music_url = null;
        this.musicRunning = false;
        this.musicArray = null;
    }
    
    public void terminateMusic(){
        this.musicRunning = false;
    }
    
    public void setSong(byte[] byteArray){
        //this.music_url = url;
        musicRunning = true;
        musicArray = byteArray; 
    }
    
    //alternativa...
    public void playByteMusic(byte[] byteArray){
        AudioData audiodata = new AudioData(byteArray);
         
        //AudioData audioData = new AudioStream(url.openStream()).getData();
        AudioDataStream audioStream = new AudioDataStream(audiodata);
        // play()
        AudioPlayer.player.start(audioStream);
        
        // loop()
        /*ContinuousAudioDataStream continuousStream = 
            new ContinuousAudioDataStream(audioStream);
        AudioPlayer.player.start(continuousStream);*/

    }
    
    
    
    /*
    Quando for para tratar da musica em arrays de bytes, provavelmente vai ser necessario guardar as coisas em ficheiro
    */        
    @Override
    public void run() {
        AudioData audiodata = new AudioData(musicArray);
         
        //AudioData audioData = new AudioStream(url.openStream()).getData();
        AudioDataStream audioStream = new AudioDataStream(audiodata);
        // play()
        AudioPlayer.player.start(audioStream);
        
        while(musicRunning);      
        
        AudioPlayer.player.stop(audioStream);
    }
}
